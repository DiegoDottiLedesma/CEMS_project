package GCMS.JSONMassBankHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MassBankCompoundGroup {

    private final String groupingKey;
    private final String groupingKeyType;
    private final MassBankSimpleRecord representativeRecord;
    private final List<MassBankSimpleRecord> spectrumRecords = new ArrayList<>();

    public MassBankCompoundGroup(String groupingKey, String groupingKeyType, MassBankSimpleRecord representativeRecord) {
        this.groupingKey = groupingKey;
        this.groupingKeyType = groupingKeyType;
        this.representativeRecord = representativeRecord;
    }

    public String getGroupingKey() {
        return groupingKey;
    }

    public String getGroupingKeyType() {
        return groupingKeyType;
    }

    public MassBankSimpleRecord getRepresentativeRecord() {
        return representativeRecord;
    }

    public List<MassBankSimpleRecord> getSpectrumRecords() {
        return Collections.unmodifiableList(spectrumRecords);
    }

    public void addSpectrumRecord(MassBankSimpleRecord record) {
        if (record != null) {
            spectrumRecords.add(record);
        }
    }

    public int getNumberOfSpectra() {
        return spectrumRecords.size();
    }

    @Override
    public String toString() {
        return "MassBankCompoundGroup{" +
                "groupingKey='" + groupingKey + '\'' +
                ", groupingKeyType='" + groupingKeyType + '\'' +
                ", compoundName='" + representativeRecord.getCompoundName() + '\'' +
                ", numberOfSpectra=" + getNumberOfSpectra() +
                '}';
    }
}
