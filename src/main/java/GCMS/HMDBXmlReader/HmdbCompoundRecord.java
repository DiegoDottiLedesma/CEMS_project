package GCMS.HMDBXmlReader;

public class HmdbCompoundRecord {
    private final String hmdbId;
    private final String name;
    private final String formula;
    private final String monoisotopicMass;
    private final String smiles;
    private final String inchi;
    private final String inchiKey;
    private final int spectraCount;

    public HmdbCompoundRecord(
            String hmdbId,
            String name,
            String formula,
            String monoisotopicMass,
            String smiles,
            String inchi,
            String inchiKey,
            int spectraCount
    ) {
        this.hmdbId = hmdbId;
        this.name = name;
        this.formula = formula;
        this.monoisotopicMass = monoisotopicMass;
        this.smiles = smiles;
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.spectraCount = spectraCount;
    }

    public String getHmdbId() {
        return hmdbId;
    }

    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public String getMonoisotopicMass() {
        return monoisotopicMass;
    }

    public String getSmiles() {
        return smiles;
    }

    public String getInchi() {
        return inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public int getSpectraCount() {
        return spectraCount;
    }

    public HmdbCompoundRecord withSpectraCount(int newSpectraCount) {
        return new HmdbCompoundRecord(
                hmdbId,
                name,
                formula,
                monoisotopicMass,
                smiles,
                inchi,
                inchiKey,
                newSpectraCount
        );
    }

    @Override
    public String toString() {
        return "HmdbCompoundRecord{" +
                "hmdbId='" + hmdbId + '\'' +
                ", name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", monoisotopicMass='" + monoisotopicMass + '\'' +
                ", smiles='" + smiles + '\'' +
                ", inchi='" + inchi + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                ", spectraCount=" + spectraCount +
                '}';
    }
}
