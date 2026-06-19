package GCMS.JSONMassBankReader;

import java.util.ArrayList;
import java.util.List;

public class MassBankSimpleRecord {

    private String accession;
    private String compoundName;

    private String formula;
    private Double exactMass;
    private String cas;
    private String inchi;
    private String inchiKey;
    private String smiles;

    private Double retentionIndex;
    private Double retentionTimeSeconds;
    private Double retentionTimeMinutes;

    private String derivatizationType;
    private String instrumentType;

    private List<SpectrumPeak> peaks = new ArrayList<>();

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Double getExactMass() {
        return exactMass;
    }

    public void setExactMass(Double exactMass) {
        this.exactMass = exactMass;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public Double getRetentionIndex() {
        return retentionIndex;
    }

    public void setRetentionIndex(Double retentionIndex) {
        this.retentionIndex = retentionIndex;
    }

    public Double getRetentionTimeSeconds() {
        return retentionTimeSeconds;
    }

    public void setRetentionTimeSeconds(Double retentionTimeSeconds) {
        this.retentionTimeSeconds = retentionTimeSeconds;
    }

    public Double getRetentionTimeMinutes() {
        return retentionTimeMinutes;
    }

    public void setRetentionTimeMinutes(Double retentionTimeMinutes) {
        this.retentionTimeMinutes = retentionTimeMinutes;
    }

    public String getDerivatizationType() {
        return derivatizationType;
    }

    public void setDerivatizationType(String derivatizationType) {
        this.derivatizationType = derivatizationType;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public List<SpectrumPeak> getPeaks() {
        return peaks;
    }

    public void setPeaks(List<SpectrumPeak> peaks) {
        this.peaks = peaks;
    }

    @Override
    public String toString() {
        return "MassBankSimpleRecord{" +
                "accession='" + accession + '\'' +
                ", compoundName='" + compoundName + '\'' +
                ", formula='" + formula + '\'' +
                ", exactMass=" + exactMass +
                ", cas='" + cas + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                ", retentionIndex=" + retentionIndex +
                ", retentionTimeSeconds=" + retentionTimeSeconds +
                ", retentionTimeMinutes=" + retentionTimeMinutes +
                ", derivatizationType='" + derivatizationType + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                ", peaks=" + peaks.size() +
                '}';
    }
}
