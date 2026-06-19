package GCMS.PCDLExcelReader;

public class PCDLCompoundRecord {
    private final String name;
    private final String formula;
    private final Double mass;
    private final Double retentionTime;
    private final Double retentionIndex;
    private final String cas;
    private final Integer pubChemId;
    private final Integer numSpectra;

    public PCDLCompoundRecord(
            String name,
            String formula,
            Double mass,
            Double retentionTime,
            Double retentionIndex,
            String cas,
            Integer pubChemId,
            Integer numSpectra
    ) {
        this.name = name;
        this.formula = formula;
        this.mass = mass;
        this.retentionTime = retentionTime;
        this.retentionIndex = retentionIndex;
        this.cas = cas;
        this.pubChemId = pubChemId;
        this.numSpectra = numSpectra;
    }

    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public Double getMass() {
        return mass;
    }

    public Double getRetentionTime() {
        return retentionTime;
    }

    public Double getRetentionIndex() {
        return retentionIndex;
    }

    public String getCas() {
        return cas;
    }

    public Integer getPubChemId() {
        return pubChemId;
    }

    public Integer getNumSpectra() {
        return numSpectra;
    }

    @Override
    public String toString() {
        return "PCDLCompoundRecord{" +
                "name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", mass=" + mass +
                ", retentionTime=" + retentionTime +
                ", retentionIndex=" + retentionIndex +
                ", cas='" + cas + '\'' +
                ", pubChemId=" + pubChemId +
                ", numSpectra=" + numSpectra +
                '}';
    }
}
