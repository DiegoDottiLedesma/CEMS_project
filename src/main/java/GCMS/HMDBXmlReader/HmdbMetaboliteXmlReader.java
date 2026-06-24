package GCMS.HMDBXmlReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HmdbMetaboliteXmlReader {

    public static Map<String, HmdbCompoundRecord> readIdentifiers(
            String hmdbMetabolitesXmlPath,
            Set<String> targetHmdbIds
    ) throws Exception {

        Map<String, HmdbCompoundRecord> recordsByHmdbId = new HashMap<>();

        if (targetHmdbIds == null || targetHmdbIds.isEmpty()) {
            return recordsByHmdbId;
        }

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (FileInputStream inputStream = new FileInputStream(Path.of(hmdbMetabolitesXmlPath).toFile())) {
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            boolean insideMetabolite = false;
            int metaboliteDepth = 0;
            String currentTag = null;
            StringBuilder currentText = new StringBuilder();

            String accession = null;
            String name = null;
            String formula = null;
            String monoisotopicMass = null;
            String smiles = null;
            String inchi = null;
            String inchiKey = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();

                    if ("metabolite".equals(tagName)) {
                        insideMetabolite = true;
                        metaboliteDepth = 1;
                        currentTag = null;
                        currentText.setLength(0);

                        accession = null;
                        name = null;
                        formula = null;
                        monoisotopicMass = null;
                        smiles = null;
                        inchi = null;
                        inchiKey = null;
                    } else if (insideMetabolite) {
                        metaboliteDepth++;

                        // Only direct children of <metabolite> are read.
                        // This avoids confusing the main <accession> with secondary accessions.
                        if (metaboliteDepth == 2 && isInterestingTag(tagName)) {
                            currentTag = tagName;
                            currentText.setLength(0);
                        }
                    }
                } else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
                    if (currentTag != null) {
                        currentText.append(reader.getText());
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    String tagName = reader.getLocalName();

                    if (insideMetabolite && currentTag != null && currentTag.equals(tagName)) {
                        String value = clean(currentText.toString());

                        switch (tagName) {
                            case "accession":
                                accession = value;
                                break;
                            case "name":
                                name = value;
                                break;
                            case "chemical_formula":
                                formula = value;
                                break;
                            case "monisotopic_molecular_weight":
                            case "monoisotopic_molecular_weight":
                                monoisotopicMass = value;
                                break;
                            case "smiles":
                                smiles = value;
                                break;
                            case "inchi":
                                inchi = value;
                                break;
                            case "inchikey":
                                inchiKey = value;
                                break;
                            default:
                                break;
                        }

                        currentTag = null;
                        currentText.setLength(0);
                    }

                    if ("metabolite".equals(tagName)) {
                        insideMetabolite = false;
                        metaboliteDepth = 0;

                        if (accession != null && targetHmdbIds.contains(accession)) {
                            HmdbCompoundRecord record = new HmdbCompoundRecord(
                                    accession,
                                    name,
                                    formula,
                                    monoisotopicMass,
                                    smiles,
                                    inchi,
                                    inchiKey,
                                    0
                            );

                            recordsByHmdbId.put(accession, record);

                            if (recordsByHmdbId.size() == targetHmdbIds.size()) {
                                break;
                            }
                        }
                    } else if (insideMetabolite) {
                        metaboliteDepth--;
                    }
                }
            }

            reader.close();
        }

        return recordsByHmdbId;
    }

    private static boolean isInterestingTag(String tagName) {
        return "accession".equals(tagName)
                || "name".equals(tagName)
                || "chemical_formula".equals(tagName)
                || "monisotopic_molecular_weight".equals(tagName)
                || "monoisotopic_molecular_weight".equals(tagName)
                || "smiles".equals(tagName)
                || "inchi".equals(tagName)
                || "inchikey".equals(tagName);
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.trim();
        return cleaned.isBlank() ? null : cleaned;
    }
}
