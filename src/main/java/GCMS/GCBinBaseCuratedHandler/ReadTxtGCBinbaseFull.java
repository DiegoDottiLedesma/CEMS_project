package GCMS.GCBinBaseCuratedHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReadTxtGCBinbaseFull {

    public static List<GCBinbaseSpectrumRecord> readTxtGCBinbaseFull(String filePath) throws IOException {
        List<GCBinbaseSpectrumRecord> spectra = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            String line;
            GCBinbaseSpectrumRecord currentSpectrum = null;
            boolean readingPeaks = false;
            int recordCounter = 0;

            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty()) {
                    continue;
                }

                String lowerLine = trimmedLine.toLowerCase(Locale.ROOT);

                if (lowerLine.startsWith("name:")) {
                    if (currentSpectrum != null) {
                        spectra.add(currentSpectrum);
                    }

                    recordCounter++;
                    currentSpectrum = new GCBinbaseSpectrumRecord();
                    currentSpectrum.setRecordNumber(recordCounter);
                    currentSpectrum.setCompound(new GCBinbaseCompoundRecord());
                    currentSpectrum.getCompound().setName(extractValue(trimmedLine));
                    readingPeaks = false;
                    continue;
                }

                if (currentSpectrum == null) {
                    continue;
                }

                if (readingPeaks) {
                    SpectrumPeak peak = parsePeakLine(trimmedLine);
                    if (peak != null) {
                        currentSpectrum.addPeak(peak);
                        continue;
                    }
                }

                if (lowerLine.startsWith("inchikey:")) {
                    currentSpectrum.getCompound().setInchiKey(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("binid:")) {
                    currentSpectrum.setBinId(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("ri:")) {
                    currentSpectrum.setRetentionIndex(parseDouble(extractValue(trimmedLine)));
                } else if (lowerLine.startsWith("ion mode:")) {
                    currentSpectrum.setIonMode(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("derivative type:")) {
                    currentSpectrum.setDerivativeType(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("instrument type:")) {
                    currentSpectrum.setInstrumentType(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("instrument:")) {
                    currentSpectrum.setInstrument(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("ms type:")) {
                    currentSpectrum.setMsType(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("ionization energy:")) {
                    currentSpectrum.setIonizationEnergy(parseDouble(extractValue(trimmedLine)));
                } else if (lowerLine.startsWith("uniqueion:")) {
                    currentSpectrum.setUniqueIon(parseInteger(extractValue(trimmedLine)));
                } else if (lowerLine.startsWith("signalnoise:")) {
                    currentSpectrum.setSignalNoise(parseDouble(extractValue(trimmedLine)));
                } else if (lowerLine.startsWith("column temperature:")) {
                    currentSpectrum.setColumnTemperature(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("column:")) {
                    currentSpectrum.setColumn(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("guard column:")) {
                    currentSpectrum.setGuardColumn(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("flow-rate:")) {
                    currentSpectrum.setFlowRate(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("mobile phase:")) {
                    currentSpectrum.setMobilePhase(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("oven temperature:")) {
                    currentSpectrum.setOvenTemperature(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("transfer line temperature:")) {
                    currentSpectrum.setTransferLineTemperature(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("detector voltage:")) {
                    currentSpectrum.setDetectorVoltage(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("mass resolution:")) {
                    currentSpectrum.setMassResolution(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("scan range m/z:")) {
                    currentSpectrum.setScanRangeMz(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("ion source temperature:")) {
                    currentSpectrum.setIonSourceTemperature(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("injection volume:")) {
                    currentSpectrum.setInjectionVolume(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("purity:")) {
                    currentSpectrum.setPurity(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("sample:")) {
                    currentSpectrum.setSample(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("injection:")) {
                    currentSpectrum.setInjection(extractValue(trimmedLine));
                } else if (lowerLine.startsWith("num peaks:")) {
                    currentSpectrum.setNumPeaksDeclared(parseInteger(extractValue(trimmedLine)));
                    readingPeaks = true;
                }
            }

            if (currentSpectrum != null) {
                spectra.add(currentSpectrum);
            }
        }

        return spectra;
    }

    private static String extractValue(String line) {
        int index = line.indexOf(':');
        if (index < 0 || index == line.length() - 1) {
            return null;
        }
        String value = line.substring(index + 1).trim();
        return value.isEmpty() ? null : value;
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return (int) Math.round(Double.parseDouble(value.trim().replace(",", ".")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static SpectrumPeak parsePeakLine(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2) {
            return null;
        }

        Double mz = parseDouble(parts[0]);
        Double intensity = parseDouble(parts[1]);

        if (mz == null || intensity == null) {
            return null;
        }

        return new SpectrumPeak(mz, intensity);
    }
}
