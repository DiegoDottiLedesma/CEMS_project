package GCMS.GCBinBaseCuratedHandler;

import java.util.ArrayList;
import java.util.List;

public class GCBinbaseCompoundGroup {

    private String compoundKey;
    private GCBinbaseCompoundRecord compound;
    private List<GCBinbaseSpectrumRecord> spectra = new ArrayList<>();

    public GCBinbaseCompoundGroup() {
    }

    public GCBinbaseCompoundGroup(String compoundKey, GCBinbaseCompoundRecord compound) {
        this.compoundKey = compoundKey;
        this.compound = compound;
    }

    public String getCompoundKey() {
        return compoundKey;
    }

    public void setCompoundKey(String compoundKey) {
        this.compoundKey = compoundKey;
    }

    public GCBinbaseCompoundRecord getCompound() {
        return compound;
    }

    public void setCompound(GCBinbaseCompoundRecord compound) {
        this.compound = compound;
    }

    public List<GCBinbaseSpectrumRecord> getSpectra() {
        return spectra;
    }

    public void setSpectra(List<GCBinbaseSpectrumRecord> spectra) {
        this.spectra = spectra;
    }

    public void addSpectrum(GCBinbaseSpectrumRecord spectrum) {
        this.spectra.add(spectrum);
    }

    @Override
    public String toString() {
        return "GCBinbaseCompoundGroup{" +
                "compoundKey='" + compoundKey + '\'' +
                ", compound=" + compound +
                ", spectraCount=" + spectra.size() +
                '}';
    }
}
