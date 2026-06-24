package GCMS.HMDBXmlReader.enrichedHandler;

public class EnrichedSpectrumRecord {
    private final String sourceFileName;
    private final String instrumentType;
    private final Integer peakCounter;
    private final String createdAt;
    private final String updatedAt;
    private final String chromatographyType;
    private final Double retentionIndex;
    private final String retentionTime;
    private final String ionizationMode;
    private final String columnType;
    private final String derivativeType;
    private final String databaseId;
    private final String inchi;
    private final String inchiKey;
    private final String hmdbName;
    private final String hmdbChemicalFormula;
    private final String hmdbSmiles;

    public EnrichedSpectrumRecord(
            String sourceFileName,
            String instrumentType,
            Integer peakCounter,
            String createdAt,
            String updatedAt,
            String chromatographyType,
            Double retentionIndex,
            String retentionTime,
            String ionizationMode,
            String columnType,
            String derivativeType,
            String databaseId,
            String inchi,
            String inchiKey,
            String hmdbName,
            String hmdbChemicalFormula,
            String hmdbSmiles
    ) {
        this.sourceFileName = sourceFileName;
        this.instrumentType = instrumentType;
        this.peakCounter = peakCounter;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.chromatographyType = chromatographyType;
        this.retentionIndex = retentionIndex;
        this.retentionTime = retentionTime;
        this.ionizationMode = ionizationMode;
        this.columnType = columnType;
        this.derivativeType = derivativeType;
        this.databaseId = databaseId;
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.hmdbName = hmdbName;
        this.hmdbChemicalFormula = hmdbChemicalFormula;
        this.hmdbSmiles = hmdbSmiles;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public Integer getPeakCounter() {
        return peakCounter;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getChromatographyType() {
        return chromatographyType;
    }

    public Double getRetentionIndex() {
        return retentionIndex;
    }

    public String getRetentionTime() {
        return retentionTime;
    }

    public String getIonizationMode() {
        return ionizationMode;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getDerivativeType() {
        return derivativeType;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public String getInchi() {
        return inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public String getHmdbName() {
        return hmdbName;
    }

    public String getHmdbChemicalFormula() {
        return hmdbChemicalFormula;
    }

    public String getHmdbSmiles() {
        return hmdbSmiles;
    }

    @Override
    public String toString() {
        return "HmdbGcmsSpectrumRecord{" +
                "sourceFileName='" + sourceFileName + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                ", peakCounter=" + peakCounter +
                ", chromatographyType='" + chromatographyType + '\'' +
                ", retentionIndex=" + retentionIndex +
                ", columnType='" + columnType + '\'' +
                ", derivativeType='" + derivativeType + '\'' +
                ", databaseId='" + databaseId + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                ", hmdbName='" + hmdbName + '\'' +
                ", hmdbChemicalFormula='" + hmdbChemicalFormula + '\'' +
                '}';
    }
}
