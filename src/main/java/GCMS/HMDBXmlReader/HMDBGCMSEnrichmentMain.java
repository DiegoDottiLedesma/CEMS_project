package GCMS.HMDBXmlReader;
import GCMS.HMDBXmlReader.*;
public class HMDBGCMSEnrichmentMain {

    public static void main(String[] args) {
        /*
         * Usage option 1: edit these paths directly.
         */
        String spectraFolderPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\hmdb_experimental_cms_spectra";
        String hmdbMetabolitesXmlPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\hmdb_metabolites.xml";
        String outputFolderPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\HMDB_GCMS_XML_ENRICHED_V2";
        String outputExcelPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\HMDB_GCMS_unique_compounds.xlsx";

        /*
         * Usage option 2: pass the four paths from the command line.
         * args[0] = folder containing GC-MS spectrum XML files
         * args[1] = general HMDB metabolites XML file
         * args[2] = output folder for enriched XML files
         * args[3] = output Excel path for the unique compounds list
         */
        if (args.length == 4) {
            spectraFolderPath = args[0];
            hmdbMetabolitesXmlPath = args[1];
            outputFolderPath = args[2];
            outputExcelPath = args[3];
        }

        try {
            EnrichmentResult result = SpectrumXmlFolderEnricher.enrichFolder(
                    spectraFolderPath,
                    hmdbMetabolitesXmlPath,
                    outputFolderPath
            );

            System.out.println("\nResumen:");
            System.out.println("XML de espectros leídos: " + result.getTotalXmlFiles());
            System.out.println("XML enriquecidos: " + result.getEnrichedXmlFiles());
            System.out.println("Compuestos únicos guardados en lista: " + result.getUniqueCompounds().size());

            ExcelWriter.write(result.getUniqueCompounds(), outputExcelPath);

            System.out.println("Excel de compuestos únicos creado en:");
            System.out.println(outputExcelPath);

            System.out.println("\nLista de compuestos únicos:");
            for (HmdbCompoundRecord compound : result.getUniqueCompounds()) {
                System.out.println(compound);
            }

            if (!result.getMissingHmdbIds().isEmpty()) {
                System.out.println("\nHMDB IDs no encontrados en el XML general:");
                for (String hmdbId : result.getMissingHmdbIds()) {
                    System.out.println(hmdbId);
                }
            }

        } catch (Exception e) {
            System.out.println("Error durante el enriquecimiento de XML HMDB GC-MS:");
            e.printStackTrace();
        }
    }
}
