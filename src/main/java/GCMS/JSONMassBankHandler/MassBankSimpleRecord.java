package GCMS.JSONMassBankHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
        this.accession = clean(accession);
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = clean(compoundName);
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = clean(formula);
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
        this.cas = clean(cas);
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = clean(inchi);
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = clean(inchiKey);
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = clean(smiles);
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

    public void setRetentionTimeMinutes(Double retentionTimeTimeMinutes) {
        this.retentionTimeMinutes = retentionTimeTimeMinutes;
    }

    public String getDerivatizationType() {
        return derivatizationType;
    }

    public void setDerivatizationType(String derivatizationType) {
        this.derivatizationType = clean(derivatizationType);
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = clean(instrumentType);
    }

    public List<SpectrumPeak> getPeaks() {
        return peaks;
    }

    public void setPeaks(List<SpectrumPeak> peaks) {
        if (peaks == null) {
            this.peaks = new ArrayList<>();
        } else {
            this.peaks = peaks;
        }
    }

    public int getNumberOfPeaks() {
        return peaks == null ? 0 : peaks.size();
    }

    public boolean hasInchi() {
        return !isBlank(inchi);
    }

    public boolean hasInchiKey() {
        return !isBlank(inchiKey);
    }

    public boolean hasChemicalIdentifier() {
        return hasInchi() || hasInchiKey();
    }

    public String getSpectrumAsText() {
        if (peaks == null || peaks.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");
        for (SpectrumPeak peak : peaks) {
            joiner.add(peak.toString());
        }
        return joiner.toString();
    }

    public String getTruncatedSpectrumAsText(int maxCharacters) {
        String spectrum = getSpectrumAsText();

        if (maxCharacters <= 0 || spectrum.length() <= maxCharacters) {
            return spectrum;
        }

        return spectrum.substring(0, maxCharacters) + " ... [TRUNCATED]";
    }

    public static String clean(String value) {
        if (value == null) {
            return null;
        }

        String cleanValue = value.trim();
        if (cleanValue.isEmpty()) {
            return null;
        }

        return cleanValue;
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "MassBankSimpleRecord{" +
                "accession='" + accession + '\'' +
                ", compoundName='" + compoundName + '\'' +
                ", formula='" + formula + '\'' +
                ", exactMass=" + exactMass +
                ", cas='" + cas + '\'' +
                ", inchi='" + inchi + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                ", smiles='" + smiles + '\'' +
                ", retentionIndex=" + retentionIndex +
                ", retentionTimeSeconds=" + retentionTimeSeconds +
                ", retentionTimeMinutes=" + retentionTimeMinutes +
                ", derivatizationType='" + derivatizationType + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                ", peaks=" + getNumberOfPeaks() +
                '}';
    }
}
