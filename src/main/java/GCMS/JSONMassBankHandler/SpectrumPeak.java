package GCMS.JSONMassBankHandler;

import java.util.Locale;

public class SpectrumPeak {

    private double mz;
    private double intensity;

    public SpectrumPeak() {
    }

    public SpectrumPeak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.6f:%.6f", mz, intensity);
    }
}
