package GCMS.GCBinBaseCuratedHandler;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GCBinbaseFullParserMain {

    public static void main(String[] args) {
        String inputTxtPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\GCBinbase_knowns_curated\\GCBinbase_knowns_curated.msp.txt";
        int recordsToPrint = 10;

        if (args.length >= 1) {
            inputTxtPath = args[0];
        }

        if (args.length >= 2) {
            if ("all".equalsIgnoreCase(args[1])) {
                recordsToPrint = Integer.MAX_VALUE;
            } else {
                try {
                    recordsToPrint = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Segundo argumento no válido. Se imprimirán 10 registros.");
                }
            }
        }

        try {
            List<GCBinbaseSpectrumRecord> spectra = ReadTxtGCBinbaseFull.readTxtGCBinbaseFull(inputTxtPath);
            Map<String, GCBinbaseCompoundGroup> compoundGroups = groupByCompoundKey(spectra);

            printSummary(spectra, compoundGroups);
            printRepeatedNames(spectra);
            printFirstRecords(spectra, recordsToPrint);

        } catch (Exception e) {
            System.out.println("Error leyendo GCBinBase:");
            e.printStackTrace();
        }
    }

    private static Map<String, GCBinbaseCompoundGroup> groupByCompoundKey(List<GCBinbaseSpectrumRecord> spectra) {
        Map<String, GCBinbaseCompoundGroup> groups = new LinkedHashMap<>();

        for (GCBinbaseSpectrumRecord spectrum : spectra) {
            if (spectrum.getCompound() == null) {
                continue;
            }

            String key = spectrum.getCompound().getCompoundKey();

            groups.computeIfAbsent(
                    key,
                    ignored -> new GCBinbaseCompoundGroup(key, spectrum.getCompound())
            ).addSpectrum(spectrum);
        }

        return groups;
    }

    private static void printSummary(List<GCBinbaseSpectrumRecord> spectra,
                                     Map<String, GCBinbaseCompoundGroup> compoundGroups) {

        long recordsWithInchiKey = spectra.stream()
                .filter(s -> s.getCompound() != null)
                .map(s -> s.getCompound().getInchiKey())
                .filter(value -> value != null && !value.isBlank())
                .count();

        long recordsWithRI = spectra.stream()
                .filter(s -> s.getRetentionIndex() != null)
                .count();

        long recordsWithPeaks = spectra.stream()
                .filter(s -> s.getPeaks() != null && !s.getPeaks().isEmpty())
                .count();

        int totalPeaks = spectra.stream()
                .mapToInt(s -> s.getPeaks() == null ? 0 : s.getPeaks().size())
                .sum();

        double averagePeaks = spectra.isEmpty() ? 0.0 : (double) totalPeaks / spectra.size();

        long peakCountMismatches = spectra.stream()
                .filter(GCBinbaseSpectrumRecord::hasPeakCountMismatch)
                .count();

        long compoundsWithMoreThanOneSpectrum = compoundGroups.values().stream()
                .filter(group -> group.getSpectra().size() > 1)
                .count();

        int maxSpectraForOneCompound = compoundGroups.values().stream()
                .mapToInt(group -> group.getSpectra().size())
                .max()
                .orElse(0);

        System.out.println("========== RESUMEN GCBinBase ==========");
        System.out.println("Registros espectrales leídos: " + spectra.size());
        System.out.println("Compuestos únicos por InChIKey/nombre: " + compoundGroups.size());
        System.out.println("Registros con InChIKey: " + recordsWithInchiKey);
        System.out.println("Registros con RI: " + recordsWithRI);
        System.out.println("Registros con picos: " + recordsWithPeaks);
        System.out.println("Picos totales leídos: " + totalPeaks);
        System.out.printf(Locale.ROOT, "Media de picos por espectro: %.2f%n", averagePeaks);
        System.out.println("Registros con Num Peaks distinto a picos leídos: " + peakCountMismatches);
        System.out.println("Compuestos con más de un espectro: " + compoundsWithMoreThanOneSpectrum);
        System.out.println("Máximo de espectros asociados a un compuesto: " + maxSpectraForOneCompound);

        printCounts("Ion mode", spectra.stream()
                .map(GCBinbaseSpectrumRecord::getIonMode)
                .collect(Collectors.toList()));

        printCounts("Derivative type", spectra.stream()
                .map(GCBinbaseSpectrumRecord::getDerivativeType)
                .collect(Collectors.toList()));
    }

    private static void printCounts(String title, List<String> values) {
        Map<String, Long> counts = values.stream()
                .map(value -> value == null || value.isBlank() ? "NULL" : value.trim())
                .collect(Collectors.groupingBy(value -> value, LinkedHashMap::new, Collectors.counting()));

        System.out.println("\n" + title + ":");
        counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue()));
    }

    private static void printRepeatedNames(List<GCBinbaseSpectrumRecord> spectra) {
        Map<String, List<GCBinbaseSpectrumRecord>> byName = spectra.stream()
                .filter(s -> s.getCompound() != null)
                .filter(s -> s.getCompound().getName() != null && !s.getCompound().getName().isBlank())
                .collect(Collectors.groupingBy(
                        s -> s.getCompound().getName().trim().toLowerCase(Locale.ROOT),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<Map.Entry<String, List<GCBinbaseSpectrumRecord>>> repeatedNames = byName.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(Collectors.toList());

        System.out.println("\nNombres repetidos: " + repeatedNames.size());

        for (Map.Entry<String, List<GCBinbaseSpectrumRecord>> entry : repeatedNames) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue().size() + " registros");
            for (GCBinbaseSpectrumRecord spectrum : entry.getValue()) {
                String inchiKey = spectrum.getCompound() == null ? null : spectrum.getCompound().getInchiKey();
                System.out.println("     record=" + spectrum.getRecordNumber()
                        + ", InChIKey=" + inchiKey
                        + ", RI=" + spectrum.getRetentionIndex()
                        + ", derivativeType=" + spectrum.getDerivativeType()
                        + ", peaks=" + spectrum.getPeaks().size());
            }
        }
    }

    private static void printFirstRecords(List<GCBinbaseSpectrumRecord> spectra, int recordsToPrint) {
        System.out.println("\nPrimeros registros:");


        for (int i = 0; i < spectra.size(); i++) {
            System.out.println("Registro " + (i + 1) + ":");
            System.out.println(spectra.get(i));
            printFirstPeaks(spectra.get(i), 5);
            System.out.println("----------------------------------------");
        }
    }

    private static void printFirstPeaks(GCBinbaseSpectrumRecord spectrum, int maxPeaks) {
        if (spectrum.getPeaks() == null || spectrum.getPeaks().isEmpty()) {
            System.out.println("Primeros picos: []");
            return;
        }

        String peaksPreview = spectrum.getPeaks().stream()
                .filter(Objects::nonNull)
                .limit(maxPeaks)
                .map(peak -> "(" + peak.getMz() + ", " + peak.getIntensity() + ")")
                .collect(Collectors.joining(", "));

        System.out.println("Primeros picos: " + peaksPreview);
    }
}
