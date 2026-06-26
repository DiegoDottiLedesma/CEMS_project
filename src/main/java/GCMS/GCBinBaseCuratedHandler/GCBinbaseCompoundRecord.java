package GCMS.GCBinBaseCuratedHandler;

public class GCBinbaseCompoundRecord {

    private String name;
    private String inchiKey;

    public GCBinbaseCompoundRecord() {
    }

    public GCBinbaseCompoundRecord(String name, String inchiKey) {
        this.name = name;
        this.inchiKey = inchiKey;
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

    public String getCompoundKey() {
        if (inchiKey != null && !inchiKey.isBlank()) {
            return inchiKey.trim();
        }
        if (name != null && !name.isBlank()) {
            return "NAME:" + name.trim().toLowerCase();
        }
        return "UNKNOWN";
    }

    @Override
    public String toString() {
        return "GCBinbaseCompoundRecord{" +
                "name='" + name + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                '}';
    }
}
