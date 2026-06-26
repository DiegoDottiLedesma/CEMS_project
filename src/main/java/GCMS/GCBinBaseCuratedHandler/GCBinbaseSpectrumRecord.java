package GCMS.GCBinBaseCuratedHandler;

import java.util.ArrayList;
import java.util.List;

public class GCBinbaseSpectrumRecord {

    private Integer recordNumber;
    private GCBinbaseCompoundRecord compound;

    private String binId;
    private Double retentionIndex;
    private String ionMode;
    private String derivativeType;

    private String instrument;
    private String instrumentType;
    private String msType;
    private Double ionizationEnergy;
    private Integer uniqueIon;
    private Integer numPeaksDeclared;
    private Double signalNoise;

    // These fields are kept as optional metadata. They are not modeled as separate entities.
    private String column;
    private String guardColumn;
    private String flowRate;
    private String mobilePhase;
    private String ovenTemperature;
    private String columnTemperature;
    private String transferLineTemperature;
    private String detectorVoltage;
    private String massResolution;
    private String scanRangeMz;
    private String ionSourceTemperature;
    private String injectionVolume;
    private String purity;
    private String sample;
    private String injection;

    private List<SpectrumPeak> peaks = new ArrayList<>();

    public GCBinbaseSpectrumRecord() {
    }

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public GCBinbaseCompoundRecord getCompound() {
        return compound;
    }

    public void setCompound(GCBinbaseCompoundRecord compound) {
        this.compound = compound;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
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

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getMsType() {
        return msType;
    }

    public void setMsType(String msType) {
        this.msType = msType;
    }

    public Double getIonizationEnergy() {
        return ionizationEnergy;
    }

    public void setIonizationEnergy(Double ionizationEnergy) {
        this.ionizationEnergy = ionizationEnergy;
    }

    public Integer getUniqueIon() {
        return uniqueIon;
    }

    public void setUniqueIon(Integer uniqueIon) {
        this.uniqueIon = uniqueIon;
    }

    public Integer getNumPeaksDeclared() {
        return numPeaksDeclared;
    }

    public void setNumPeaksDeclared(Integer numPeaksDeclared) {
        this.numPeaksDeclared = numPeaksDeclared;
    }

    public Double getSignalNoise() {
        return signalNoise;
    }

    public void setSignalNoise(Double signalNoise) {
        this.signalNoise = signalNoise;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getGuardColumn() {
        return guardColumn;
    }

    public void setGuardColumn(String guardColumn) {
        this.guardColumn = guardColumn;
    }

    public String getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(String flowRate) {
        this.flowRate = flowRate;
    }

    public String getMobilePhase() {
        return mobilePhase;
    }

    public void setMobilePhase(String mobilePhase) {
        this.mobilePhase = mobilePhase;
    }

    public String getOvenTemperature() {
        return ovenTemperature;
    }

    public void setOvenTemperature(String ovenTemperature) {
        this.ovenTemperature = ovenTemperature;
    }

    public String getColumnTemperature() {
        return columnTemperature;
    }

    public void setColumnTemperature(String columnTemperature) {
        this.columnTemperature = columnTemperature;
    }

    public String getTransferLineTemperature() {
        return transferLineTemperature;
    }

    public void setTransferLineTemperature(String transferLineTemperature) {
        this.transferLineTemperature = transferLineTemperature;
    }

    public String getDetectorVoltage() {
        return detectorVoltage;
    }

    public void setDetectorVoltage(String detectorVoltage) {
        this.detectorVoltage = detectorVoltage;
    }

    public String getMassResolution() {
        return massResolution;
    }

    public void setMassResolution(String massResolution) {
        this.massResolution = massResolution;
    }

    public String getScanRangeMz() {
        return scanRangeMz;
    }

    public void setScanRangeMz(String scanRangeMz) {
        this.scanRangeMz = scanRangeMz;
    }

    public String getIonSourceTemperature() {
        return ionSourceTemperature;
    }

    public void setIonSourceTemperature(String ionSourceTemperature) {
        this.ionSourceTemperature = ionSourceTemperature;
    }

    public String getInjectionVolume() {
        return injectionVolume;
    }

    public void setInjectionVolume(String injectionVolume) {
        this.injectionVolume = injectionVolume;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getInjection() {
        return injection;
    }

    public void setInjection(String injection) {
        this.injection = injection;
    }

    public List<SpectrumPeak> getPeaks() {
        return peaks;
    }

    public void setPeaks(List<SpectrumPeak> peaks) {
        this.peaks = peaks;
    }

    public void addPeak(SpectrumPeak peak) {
        this.peaks.add(peak);
    }

    public boolean hasPeakCountMismatch() {
        return numPeaksDeclared != null && numPeaksDeclared != peaks.size();
    }

    @Override
    public String toString() {
        String compoundName = compound != null ? compound.getName() : null;
        String inchiKey = compound != null ? compound.getInchiKey() : null;

        return "GCBinbaseSpectrumRecord{" +
                "recordNumber=" + recordNumber +
                ", name='" + compoundName + '\'' +
                ", inchiKey='" + inchiKey + '\'' +
                ", binId='" + binId + '\'' +
                ", retentionIndex=" + retentionIndex +
                ", ionMode='" + ionMode + '\'' +
                ", derivativeType='" + derivativeType + '\'' +
                ", instrumentType='" + instrumentType + '\'' +
                ", ionizationEnergy=" + ionizationEnergy +
                ", uniqueIon=" + uniqueIon +
                ", numPeaksDeclared=" + numPeaksDeclared +
                ", peaksRead=" + peaks.size() +
                '}';
    }
}
