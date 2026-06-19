package GCMS.JSONMassBankReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MassBankSimpleJsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<MassBankSimpleRecord> readJsonLines(String filePath) throws IOException {
        List<MassBankSimpleRecord> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    MassBankSimpleRecord record = parseLine(line);
                    records.add(record);
                } catch (Exception e) {
                    System.err.println("Línea ignorada por error en línea " + lineNumber + ": " + e.getMessage());
                }
            }
        }

        return records;
    }

    public static MassBankSimpleRecord parseLine(String jsonLine) throws IOException {
        JsonNode root = objectMapper.readTree(jsonLine);

        MassBankSimpleRecord record = new MassBankSimpleRecord();

        JsonNode compound = getFirstCompound(root);
        JsonNode compoundMetaData = compound.path("metaData");
        JsonNode generalMetaData = root.path("metaData");

        record.setAccession(firstNonBlank(
                getText(root, "id"),
                getMetaValue(generalMetaData, "accession")
        ));

        record.setCompoundName(getFirstName(compound));

        record.setFormula(getMetaValue(compoundMetaData, "molecular formula"));

        record.setExactMass(parseDoubleOrNull(firstNonBlank(
                getMetaValue(generalMetaData, "exact mass"),
                getMetaValue(compoundMetaData, "total exact mass")
        )));

        record.setCas(getMetaValue(compoundMetaData, "cas"));

        record.setInchi(firstNonBlank(
                getText(compound, "inchi"),
                getMetaValue(compoundMetaData, "InChI")
        ));

        record.setInchiKey(firstNonBlank(
                getText(compound, "inchiKey"),
                getMetaValue(compoundMetaData, "InChIKey")
        ));

        record.setSmiles(getMetaValue(compoundMetaData, "SMILES"));

        record.setRetentionIndex(parseDoubleOrNull(
                getMetaValue(generalMetaData, "retention index")
        ));

        Double rtSeconds = parseRetentionTimeToSeconds(
                getMetaValue(generalMetaData, "retention time")
        );

        record.setRetentionTimeSeconds(rtSeconds);

        if (rtSeconds != null) {
            record.setRetentionTimeMinutes(rtSeconds / 60.0);
        }

        record.setDerivatizationType(getMetaValue(generalMetaData, "derivatization type"));
        record.setInstrumentType(getMetaValue(generalMetaData, "instrument type"));

        record.setPeaks(parseSpectrum(getText(root, "spectrum")));

        return record;
    }

    private static JsonNode getFirstCompound(JsonNode root) {
        JsonNode compounds = root.path("compound");

        if (compounds.isArray() && compounds.size() > 0) {
            return compounds.get(0);
        }

        return objectMapper.createObjectNode();
    }

    private static String getFirstName(JsonNode compound) {
        JsonNode names = compound.path("names");

        if (names.isArray() && names.size() > 0) {
            JsonNode firstName = names.get(0);
            return getText(firstName, "name");
        }

        return null;
    }

    private static List<SpectrumPeak> parseSpectrum(String spectrumText) {
        List<SpectrumPeak> peaks = new ArrayList<>();

        if (spectrumText == null || spectrumText.isBlank()) {
            return peaks;
        }

        String[] pairs = spectrumText.trim().split("\\s+");

        for (String pair : pairs) {
            String[] parts = pair.split(":");

            if (parts.length != 2) {
                continue;
            }

            try {
                double mz = Double.parseDouble(parts[0].replace(",", "."));
                double intensity = Double.parseDouble(parts[1].replace(",", "."));

                peaks.add(new SpectrumPeak(mz, intensity));
            } catch (NumberFormatException ignored) {
                // Pico mal formado. Se ignora.
            }
        }

        return peaks;
    }

    private static String getMetaValue(JsonNode metaDataArray, String targetName) {
        if (metaDataArray == null || !metaDataArray.isArray()) {
            return null;
        }

        String computedValue = null;

        for (JsonNode metaNode : metaDataArray) {
            String name = getText(metaNode, "name");

            if (name == null || !name.equalsIgnoreCase(targetName)) {
                continue;
            }

            String value = getText(metaNode, "value");

            if (value == null) {
                continue;
            }

            boolean computed = metaNode.path("computed").asBoolean(false);

            if (!computed) {
                return value;
            }

            computedValue = value;
        }

        return computedValue;
    }

    private static String getText(JsonNode node, String fieldName) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }

        JsonNode value = node.get(fieldName);

        if (value == null || value.isNull()) {
            return null;
        }

        String text = value.asText();

        if (text == null || text.isBlank()) {
            return null;
        }

        return text.trim();
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return null;
    }

    private static Double parseDoubleOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parseRetentionTimeToSeconds(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String clean = value.trim().toLowerCase().replace(",", ".");

        String numericPart = clean.replaceAll("[^0-9.\\-+]", "");

        if (numericPart.isBlank()) {
            return null;
        }

        try {
            double number = Double.parseDouble(numericPart);

            if (clean.contains("min")) {
                return number * 60.0;
            }

            return number;

        } catch (NumberFormatException e) {
            return null;
        }
    }
}
