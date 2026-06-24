package GCMS.HMDBXmlReader.enrichedHandler;
import java.util.List;
public class HMDBEnrichedXMLToExcelMain {

    public static void main(String[] args) {
        String enrichedXmlFolderPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\HMDB_GCMS_XML_ENRICHED";
        String outputExcelPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\HMDB_GCMS_spectra_summary.xlsx";

        if (args.length == 2) {
            enrichedXmlFolderPath = args[0];
            outputExcelPath = args[1];
        }

        try {
            List<EnrichedSpectrumRecord> records =
                    HmdbEnrichedXmlFolderReader.readFolder(enrichedXmlFolderPath);

            System.out.println("XML enriquecidos leídos: " + records.size());

            HmdbGcmsSpectraExcelWriter.write(records, outputExcelPath);

            System.out.println("Excel creado correctamente en:");
            System.out.println(outputExcelPath);

            System.out.println("\nPrimeros registros leídos:");
            for (int i = 0; i < Math.min(10, records.size()); i++) {
                System.out.println(records.get(i));
            }

        } catch (Exception e) {
            System.out.println("Error extrayendo información de los XML enriquecidos:");
            e.printStackTrace();
        }
    }
}
