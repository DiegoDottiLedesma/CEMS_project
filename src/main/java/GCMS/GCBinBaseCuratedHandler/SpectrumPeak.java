package GCMS.GCBinBaseCuratedHandler;

public class SpectrumPeak {

    private Double mz;
    private Double intensity;

    public SpectrumPeak() {
    }

    public SpectrumPeak(Double mz, Double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }

    public Double getMz() {
        return mz;
    }

    public void setMz(Double mz) {
        this.mz = mz;
    }

    public Double getIntensity() {
        return intensity;
    }

    public void setIntensity(Double intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "SpectrumPeak{" +
                "mz=" + mz +
                ", intensity=" + intensity +
                '}';
    }
}
