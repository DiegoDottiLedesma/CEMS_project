package GCMS.HMDBXmlReader.enrichedHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HmdbEnrichedXmlFolderReader {

    public static List<EnrichedSpectrumRecord> readFolder(String enrichedXmlFolderPath) throws Exception {
        List<EnrichedSpectrumRecord> records = new ArrayList<>();

        File folder = new File(enrichedXmlFolderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("La ruta no es una carpeta válida: " + enrichedXmlFolderPath);
        }

        File[] xmlFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (xmlFiles == null || xmlFiles.length == 0) {
            System.out.println("No se encontraron archivos XML en la carpeta.");
            return records;
        }

        List<File> sortedFiles = new ArrayList<>(List.of(xmlFiles));
        sortedFiles.sort(Comparator.comparing(File::getName));

        for (File xmlFile : sortedFiles) {
            EnrichedSpectrumRecord record = readSingleXml(xmlFile);
            records.add(record);
        }

        return records;
    }

    private static EnrichedSpectrumRecord readSingleXml(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Seguridad básica para evitar lectura de entidades externas.
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        String instrumentType = getText(document, "instrument-type");
        Integer peakCounter = getInteger(document, "peak-counter");
        String createdAt = getText(document, "created-at");
        String updatedAt = getText(document, "updated-at");
        String chromatographyType = getText(document, "chromatography-type");
        Double retentionIndex = getDouble(document, "retention-index");
        String retentionTime = getText(document, "retention-time");
        String ionizationMode = getText(document, "ionization-mode");
        String columnType = getText(document, "column-type");
        String derivativeType = getText(document, "derivative-type");
        String databaseId = getText(document, "database-id");
        String inchi = getText(document, "inchi");
        String inchiKey = getText(document, "inchikey");
        String hmdbName = getText(document, "hmdb-name");
        String hmdbChemicalFormula = getText(document, "hmdb-chemical-formula");
        String hmdbSmiles = getText(document, "hmdb-smiles");

        return new EnrichedSpectrumRecord(
                xmlFile.getName(),
                instrumentType,
                peakCounter,
                createdAt,
                updatedAt,
                chromatographyType,
                retentionIndex,
                retentionTime,
                ionizationMode,
                columnType,
                derivativeType,
                databaseId,
                inchi,
                inchiKey,
                hmdbName,
                hmdbChemicalFormula,
                hmdbSmiles
        );
    }

    private static String getText(Document document, String tagName) {
        Node node = document.getElementsByTagName(tagName).item(0);

        if (node == null) {
            return null;
        }

        if (node instanceof Element element && element.hasAttribute("nil")) {
            String nilValue = element.getAttribute("nil");

            if ("true".equalsIgnoreCase(nilValue)) {
                return null;
            }
        }

        String value = node.getTextContent();

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private static Double getDouble(Document document, String tagName) {
        String value = getText(document, tagName);

        if (value == null) {
            return null;
        }

        try {
            value = value.replace(",", ".");
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer getInteger(Document document, String tagName) {
        String value = getText(document, tagName);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}