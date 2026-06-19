package GCMS.JSONMassBankReader;

public class SpectrumPeak {

    private final double mz;
    private final double intensity;

    public SpectrumPeak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }

    public double getMz() {
        return mz;
    }

    public double getIntensity() {
        return intensity;
    }

    @Override
    public String toString() {
        return "SpectrumPeak{" +
                "mz=" + mz +
                ", intensity=" + intensity +
                '}';
    }
}
