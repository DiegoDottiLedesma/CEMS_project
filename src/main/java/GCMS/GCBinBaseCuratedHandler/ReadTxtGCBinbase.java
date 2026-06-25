package GCMS.GCBinBaseCuratedHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Reader for the GCBinbase MSP/TXT file.
 *
 * The file is formed by repeated compound blocks. Each block starts with "name:"
 * and contains metadata lines such as "InChIKey:", "RI:", "ion mode:" and
 * "derivative type:". After "Num Peaks:" the spectral peaks appear as plain
 * numeric pairs, so they are ignored by this parser.
 */
public class ReadTxtGCBinbase {

    public static List<GCBinbaseCompoundRecord> readTxtGCBinbase(String txtPath) throws IOException {
        return readTxtGCBinbase(Paths.get(txtPath));
    }

    public static List<GCBinbaseCompoundRecord> readTxtGCBinbase(Path txtPath) throws IOException {
        List<GCBinbaseCompoundRecord> compounds = new ArrayList<>();
        GCBinbaseCompoundRecord currentCompound = null;

        try (BufferedReader reader = Files.newBufferedReader(txtPath, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String cleanedLine = line.trim();

                if (cleanedLine.isEmpty()) {
                    continue;
                }

                String normalizedLine = cleanedLine.toLowerCase(Locale.ROOT);

                if (normalizedLine.startsWith("name:")) {
                    if (currentCompound != null && hasUsefulData(currentCompound)) {
                        compounds.add(currentCompound);
                    }

                    currentCompound = new GCBinbaseCompoundRecord();
                    currentCompound.setName(clean(getValueAfterColon(cleanedLine)));
                    continue;
                }

                if (currentCompound == null) {
                    continue;
                }

                if (normalizedLine.startsWith("inchikey:")) {
                    currentCompound.setInchiKey(clean(getValueAfterColon(cleanedLine)));
                } else if (normalizedLine.startsWith("ri:")) {
                    currentCompound.setRetentionIndex(parseDoubleOrNull(getValueAfterColon(cleanedLine)));
                } else if (normalizedLine.startsWith("ion mode:")) {
                    currentCompound.setIonMode(clean(getValueAfterColon(cleanedLine)));
                } else if (normalizedLine.startsWith("derivative type:")) {
                    currentCompound.setDerivativeType(clean(getValueAfterColon(cleanedLine)));
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading GCBinbase TXT/MSP file: " + txtPath, e);
        }

        if (currentCompound != null && hasUsefulData(currentCompound)) {
            compounds.add(currentCompound);
        }

        return compounds;
    }

    private static boolean hasUsefulData(GCBinbaseCompoundRecord compound) {
        return compound.getName() != null && !compound.getName().isEmpty();
    }

    private static String getValueAfterColon(String line) {
        int colonIndex = line.indexOf(':');
        if (colonIndex < 0 || colonIndex == line.length() - 1) {
            return null;
        }
        return line.substring(colonIndex + 1);
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.trim();
        if (cleaned.isEmpty() || "-".equals(cleaned) || "NA".equalsIgnoreCase(cleaned) || "N/A".equalsIgnoreCase(cleaned)) {
            return null;
        }
        return cleaned;
    }

    private static Double parseDoubleOrNull(String value) {
        String cleaned = clean(value);
        if (cleaned == null) {
            return null;
        }

        try {
            return Double.parseDouble(cleaned.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
