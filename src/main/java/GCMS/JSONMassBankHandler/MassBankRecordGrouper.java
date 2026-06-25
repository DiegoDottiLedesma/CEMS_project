package GCMS.JSONMassBankHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MassBankRecordGrouper {

    private static final String INCHI_KEY_TYPE = "InChIKey";
    private static final String INCHI_TYPE = "InChI";
    private static final String ACCESSION_FALLBACK_TYPE = "AccessionFallback";

    private MassBankRecordGrouper() {
        // Utility class.
    }

    /**
     * Groups MassBank spectrum records at compound level.
     *
     * Main criterion:
     * 1. InChIKey, when it is available.
     * 2. InChI, when InChIKey is not available.
     * 3. Accession fallback, when no chemical identifier is available.
     *
     * This method does not delete spectra. It creates one compound group and keeps all
     * MassBank records/spectra associated with that compound.
     */
    public static List<MassBankCompoundGroup> groupByChemicalIdentifier(List<MassBankSimpleRecord> records) {
        Map<String, String> inchiToInchiKey = buildInchiToInchiKeyMap(records);
        Map<String, MassBankCompoundGroup> groups = new LinkedHashMap<>();

        for (MassBankSimpleRecord record : records) {
            GroupingIdentifier identifier = getGroupingIdentifier(record, inchiToInchiKey);
            String internalKey = identifier.getType() + "::" + identifier.getValue();

            MassBankCompoundGroup group = groups.get(internalKey);
            if (group == null) {
                group = new MassBankCompoundGroup(identifier.getValue(), identifier.getType(), record);
                groups.put(internalKey, group);
            }

            group.addSpectrumRecord(record);
        }

        return new ArrayList<>(groups.values());
    }

    private static Map<String, String> buildInchiToInchiKeyMap(List<MassBankSimpleRecord> records) {
        Map<String, String> inchiToInchiKey = new LinkedHashMap<>();

        for (MassBankSimpleRecord record : records) {
            String inchi = normalize(record.getInchi());
            String inchiKey = normalize(record.getInchiKey());

            if (inchi != null && inchiKey != null) {
                inchiToInchiKey.putIfAbsent(inchi, inchiKey);
            }
        }

        return inchiToInchiKey;
    }

    private static GroupingIdentifier getGroupingIdentifier(MassBankSimpleRecord record, Map<String, String> inchiToInchiKey) {
        String inchiKey = normalize(record.getInchiKey());
        if (inchiKey != null) {
            return new GroupingIdentifier(INCHI_KEY_TYPE, inchiKey);
        }

        String inchi = normalize(record.getInchi());
        if (inchi != null) {
            String mappedInchiKey = inchiToInchiKey.get(inchi);
            if (mappedInchiKey != null) {
                return new GroupingIdentifier(INCHI_KEY_TYPE, mappedInchiKey);
            }

            return new GroupingIdentifier(INCHI_TYPE, inchi);
        }

        String accession = normalize(record.getAccession());
        if (accession != null) {
            return new GroupingIdentifier(ACCESSION_FALLBACK_TYPE, accession);
        }

        return new GroupingIdentifier(ACCESSION_FALLBACK_TYPE, "NO_IDENTIFIER_" + System.identityHashCode(record));
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }

        String cleanValue = value.trim();
        if (cleanValue.isEmpty()) {
            return null;
        }

        return cleanValue;
    }

    private static class GroupingIdentifier {
        private final String type;
        private final String value;

        private GroupingIdentifier(String type, String value) {
            this.type = type;
            this.value = value;
        }

        private String getType() {
            return type;
        }

        private String getValue() {
            return value;
        }
    }
}
