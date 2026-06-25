package GCMS.miniNDBHandler;

/**
 * Java object that stores the Mini-N GC-MS compound information selected from the Excel file.
 *
 * Fields extracted:
 * - Name
 * - SMILES
 * - InChI
 * - Retention Index for SSNP
 * - Retention Index for SP
 * - InChIKey
 * - MF (molecular formula)
 */
public class MiniNCompoundRecord {

    private String name;
    private String smiles;
    private String inchi;
    private Double retentionIndexSSNP;
    private Double retentionIndexSP;
    private String inchiKey;
    private String molecularFormula;

    public MiniNCompoundRecord() {
    }

    public MiniNCompoundRecord(String name,
                               String smiles,
                               String inchi,
                               Double retentionIndexSSNP,
                               Double retentionIndexSP,
                               String inchiKey,
                               String molecularFormula) {
        this.name = name;
        this.smiles = smiles;
        this.inchi = inchi;
        this.retentionIndexSSNP = retentionIndexSSNP;
        this.retentionIndexSP = retentionIndexSP;
        this.inchiKey = inchiKey;
        this.molecularFormula = molecularFormula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public Double getRetentionIndexSSNP() {
        return retentionIndexSSNP;
    }

    public void setRetentionIndexSSNP(Double retentionIndexSSNP) {
        this.retentionIndexSSNP = retentionIndexSSNP;
    }

    public Double getRetentionIndexSP() {
        return retentionIndexSP;
    }

    public void setRetentionIndexSP(Double retentionIndexSP) {
        this.retentionIndexSP = retentionIndexSP;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    @Override
    public String toString() {
        return "MiniNCompoundRecord{" +
                "name='" + name + '\'' +
                ", smiles='" + smiles + '\'' +
                ", inchi='" + inchi + '\'' +
                ", retentionIndexSSNP=" + retentionIndexSSNP +
                ", retentionIndexSP=" + retentionIndexSP +
                ", inchiKey='" + inchiKey + '\'' +
                ", molecularFormula='" + molecularFormula + '\'' +
                '}';
    }
}
