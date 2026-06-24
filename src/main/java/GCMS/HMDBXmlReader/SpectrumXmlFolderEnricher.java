package GCMS.HMDBXmlReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpectrumXmlFolderEnricher {

    public static EnrichmentResult enrichFolder(
            String spectraFolderPath,
            String hmdbMetabolitesXmlPath,
            String outputFolderPath
    ) throws Exception {

        Path spectraFolder = Path.of(spectraFolderPath);
        Path outputFolder = Path.of(outputFolderPath);
        Files.createDirectories(outputFolder);

        List<Path> spectrumXmlFiles = listXmlFiles(spectraFolder);

        Map<Path, String> hmdbIdBySpectrumFile = new LinkedHashMap<>();
        Map<String, Integer> spectraCountByHmdbId = new HashMap<>();
        Set<String> uniqueHmdbIds = new LinkedHashSet<>();

        for (Path spectrumXmlFile : spectrumXmlFiles) {
            String hmdbId = readDatabaseId(spectrumXmlFile);

            if (hmdbId == null || hmdbId.isBlank()) {
                System.out.println("Archivo ignorado porque no tiene <database-id>: " + spectrumXmlFile.getFileName());
                continue;
            }

            hmdbIdBySpectrumFile.put(spectrumXmlFile, hmdbId);
            uniqueHmdbIds.add(hmdbId);
            spectraCountByHmdbId.put(hmdbId, spectraCountByHmdbId.getOrDefault(hmdbId, 0) + 1);
        }

        System.out.println("XML de espectros encontrados: " + spectrumXmlFiles.size());
        System.out.println("XML de espectros con <database-id>: " + hmdbIdBySpectrumFile.size());
        System.out.println("Compuestos HMDB únicos: " + uniqueHmdbIds.size());

        Map<String, HmdbCompoundRecord> hmdbRecords =
                HmdbMetaboliteXmlReader.readIdentifiers(hmdbMetabolitesXmlPath, uniqueHmdbIds);

        List<HmdbCompoundRecord> uniqueCompounds = new ArrayList<>();
        Set<String> missingHmdbIds = new HashSet<>();

        for (String hmdbId : uniqueHmdbIds) {
            HmdbCompoundRecord record = hmdbRecords.get(hmdbId);

            if (record == null) {
                missingHmdbIds.add(hmdbId);
            } else {
                int spectraCount = spectraCountByHmdbId.getOrDefault(hmdbId, 0);
                uniqueCompounds.add(record.withSpectraCount(spectraCount));
            }
        }

        int enrichedFiles = 0;

        for (Map.Entry<Path, String> entry : hmdbIdBySpectrumFile.entrySet()) {
            Path inputXml = entry.getKey();
            String hmdbId = entry.getValue();
            HmdbCompoundRecord record = hmdbRecords.get(hmdbId);

            Path relativePath = spectraFolder.relativize(inputXml);
            Path outputXml = outputFolder.resolve(relativePath);
            Files.createDirectories(outputXml.getParent());

            if (record == null) {
                Files.copy(inputXml, outputXml, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("No se encontró en HMDB general, se copia sin modificar: " + hmdbId);
                continue;
            }

            boolean enriched = enrichSingleSpectrumXml(inputXml, outputXml, record);

            if (enriched) {
                enrichedFiles++;
            }
        }

        return new EnrichmentResult(
                spectrumXmlFiles.size(),
                enrichedFiles,
                uniqueCompounds,
                missingHmdbIds
        );
    }

    private static List<Path> listXmlFiles(Path folder) throws Exception {
        try (Stream<Path> stream = Files.walk(folder)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".xml"))
                    .collect(Collectors.toList());
        }
    }

    private static String readDatabaseId(Path spectrumXmlFile) throws Exception {
        Document document = parseXml(spectrumXmlFile);
        Element root = document.getDocumentElement();
        Element databaseIdElement = getDirectChild(root, "database-id");

        if (databaseIdElement == null) {
            return null;
        }

        String value = databaseIdElement.getTextContent();
        return value == null ? null : value.trim();
    }

    private static boolean enrichSingleSpectrumXml(
            Path inputXml,
            Path outputXml,
            HmdbCompoundRecord record
    ) throws Exception {

        Document document = parseXml(inputXml);
        Element root = document.getDocumentElement();

        Element databaseIdElement = getDirectChild(root, "database-id");

        if (databaseIdElement == null) {
            Files.copy(inputXml, outputXml, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return false;
        }

        // These fields are inserted/updated in the GC-MS spectrum XML.
        // They refer to the base HMDB compound, not necessarily to the derivatized GC-MS form.
        Node lastInsertedNode = databaseIdElement;
        lastInsertedNode = upsertDirectChildAfter(document, root, "inchi", record.getInchi(), lastInsertedNode);
        lastInsertedNode = upsertDirectChildAfter(document, root, "inchikey", record.getInchiKey(), lastInsertedNode);

        // Optional useful fields from the general HMDB metabolite XML.
        lastInsertedNode = upsertDirectChildAfter(document, root, "hmdb-name", record.getName(), lastInsertedNode);
        lastInsertedNode = upsertDirectChildAfter(document, root, "hmdb-chemical-formula", record.getFormula(), lastInsertedNode);
        lastInsertedNode = upsertDirectChildAfter(document, root, "hmdb-smiles", record.getSmiles(), lastInsertedNode);

        writeXml(document, outputXml);
        return true;
    }

    private static Document parseXml(Path xmlPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlPath.toFile());
    }

    private static Element getDirectChild(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child instanceof Element && tagName.equals(child.getNodeName())) {
                return (Element) child;
            }
        }

        return null;
    }

    private static Node upsertDirectChildAfter(
            Document document,
            Element root,
            String tagName,
            String value,
            Node afterNode
    ) {
        if (value == null || value.isBlank()) {
            return afterNode;
        }

        Element existingElement = getDirectChild(root, tagName);

        if (existingElement != null) {
            existingElement.setTextContent(value.trim());
            return existingElement;
        }

        Element newElement = document.createElement(tagName);
        newElement.setTextContent(value.trim());

        Node nextSibling = afterNode.getNextSibling();

        if (nextSibling == null) {
            root.appendChild(document.createTextNode("\n  "));
            root.appendChild(newElement);
            return newElement;
        }

        root.insertBefore(document.createTextNode("\n  "), nextSibling);
        root.insertBefore(newElement, nextSibling);

        return newElement;
    }

    private static void writeXml(Document document, Path outputXml) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(new DOMSource(document), new StreamResult(outputXml.toFile()));
    }
}
