package GCMS.GCBinBaseCuratedHandler;

/**
 * Java object that stores the selected compound information from the GCBinbase MSP/TXT file.
 *
 * Fields extracted:
 * - Name
 * - InChIKey
 * - RI
 * - Ion mode
 * - Derivative type
 */
public class GCBinbaseCompoundRecord {

    private String name;
    private String inchiKey;
    private Double retentionIndex;
    private String ionMode;
    private String derivativeType;

    public GCBinbaseCompoundRecord() {
    }

    public GCBinbaseCompoundRecord(String name,
                                   String inchiKey,
                                   Double retentionIndex,
                                   String ionMode,
                                   String derivativeType) {
        this.name = name;
        this.inchiKey = inchiKey;
        this.retentionIndex = retentionIndex;
        this.ionMode = ionMode;
        this.derivativeType = derivativeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public Double getRetentionIndex() {
        return retentionIndex;
    }

    public void setRetentionIndex(Double retentionIndex) {
        this.retentionIndex = retentionIndex;
    }

    public String getIonMode() {
        return ionMode;
    }

    public void setIonMode(String ionMode) {
        this.ionMode = ionMode;
    }

    public String getDerivativeType() {
        return derivativeType;
    }

    public void setDerivativeType(String derivativeType) {
        this.derivativeType = derivativeType;
    }

    @Override
    public String toString() {
        return "GCBinbaseCompoundRecord{" +
                "name='" + name + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                ", retentionIndex=" + retentionIndex +
                ", ionMode='" + ionMode + '\'' +
                ", derivativeType='" + derivativeType + '\'' +
                '}';
    }
}
