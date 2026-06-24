package GCMS.PCDLExcelReader;

public class PCDLCorrectedCompoundRecord {
    private final String name;
    private final String formula;
    private final Double mass;
    private final Double retentionTime;
    private final Double retentionIndex;
    private final String cation;
    private final String anion;
    private final String cas;
    private final Integer chemSpiderId;
    private final Integer pubChemId;
    private final String synonyms;
    private final String iupac;
    private final Integer numSpectra;
    private final Integer ccsCount;
    private final String inchiKey;
    private final String inchi;

    public PCDLCorrectedCompoundRecord(
            String name,
            String formula,
            Double mass,
            Double retentionTime,
            Double retentionIndex,
            String cation,
            String anion,
            String cas,
            Integer chemSpiderId,
            Integer pubChemId,
            String synonyms,
            String iupac,
            Integer numSpectra,
            Integer ccsCount,
            String inchiKey,
            String inchi
    ) {
        this.name = name;
        this.formula = formula;
        this.mass = mass;
        this.retentionTime = retentionTime;
        this.retentionIndex = retentionIndex;
        this.cation = cation;
        this.anion = anion;
        this.cas = cas;
        this.chemSpiderId = chemSpiderId;
        this.pubChemId = pubChemId;
        this.synonyms = synonyms;
        this.iupac = iupac;
        this.numSpectra = numSpectra;
        this.ccsCount = ccsCount;
        this.inchiKey = inchiKey;
        this.inchi = inchi;
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

    public String getCation() {
        return cation;
    }

    public String getAnion() {
        return anion;
    }

    public String getCas() {
        return cas;
    }

    public Integer getChemSpiderId() {
        return chemSpiderId;
    }

    public Integer getPubChemId() {
        return pubChemId;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public String getIupac() {
        return iupac;
    }

    public Integer getNumSpectra() {
        return numSpectra;
    }

    public Integer getCcsCount() {
        return ccsCount;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public String getInchi() {
        return inchi;
    }

    @Override
    public String toString() {
        return "PCDLCorrectedCompoundRecord{" +
                "name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", mass=" + mass +
                ", retentionTime=" + retentionTime +
                ", retentionIndex=" + retentionIndex +
                ", cas='" + cas + '\'' +
                ", chemSpiderId=" + chemSpiderId +
                ", pubChemId=" + pubChemId +
                ", numSpectra=" + numSpectra +
                ", ccsCount=" + ccsCount +
                ", inchiKey='" + inchiKey + '\'' +
                ", inchi='" + inchi + '\'' +
                '}';
    }
}
