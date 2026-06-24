package GCMS.HMDBXmlReader;

import java.util.List;
import java.util.Set;

public class EnrichmentResult {
    private final int totalXmlFiles;
    private final int enrichedXmlFiles;
    private final List<GCMS.HMDBXmlReader.HmdbCompoundRecord> uniqueCompounds;
    private final Set<String> missingHmdbIds;

    public EnrichmentResult(
            int totalXmlFiles,
            int enrichedXmlFiles,
            List<GCMS.HMDBXmlReader.HmdbCompoundRecord> uniqueCompounds,
            Set<String> missingHmdbIds
    ) {
        this.totalXmlFiles = totalXmlFiles;
        this.enrichedXmlFiles = enrichedXmlFiles;
        this.uniqueCompounds = uniqueCompounds;
        this.missingHmdbIds = missingHmdbIds;
    }

    public int getTotalXmlFiles() {
        return totalXmlFiles;
    }

    public int getEnrichedXmlFiles() {
        return enrichedXmlFiles;
    }

    public List<GCMS.HMDBXmlReader.HmdbCompoundRecord> getUniqueCompounds() {
        return uniqueCompounds;
    }

    public Set<String> getMissingHmdbIds() {
        return missingHmdbIds;
    }
}
