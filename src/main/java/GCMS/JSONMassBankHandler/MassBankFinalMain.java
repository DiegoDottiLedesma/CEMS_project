package GCMS.JSONMassBankHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MassBankFinalMain {

    /*
     * Edit these two paths before running the program.
     * INPUT_JSON_PATH: path to the MassBank JSON file.
     * OUTPUT_FOLDER_PATH: folder where the generated Excel files will be saved.
     */
    private static final String INPUT_JSON_PATH = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\MoNA-export-GC-MS_Spectra.json";
    private static final String OUTPUT_FOLDER_PATH = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\massbank_output";

    public static void main(String[] args) {
        Path inputJsonPath = Path.of(INPUT_JSON_PATH);
        Path outputFolder = Path.of(OUTPUT_FOLDER_PATH);

        try {
            validateInputFile(inputJsonPath);
            Files.createDirectories(outputFolder);

            System.out.println("Reading MassBank JSON file...");
            System.out.println("Input file: " + inputJsonPath.toAbsolutePath());
            List<MassBankSimpleRecord> allRecords = MassBankSimpleJsonParser.readJsonFile(inputJsonPath.toString());

            System.out.println("Grouping records by chemical identifier...");
            List<MassBankCompoundGroup> uniqueCompoundGroups = MassBankRecordGrouper.groupByChemicalIdentifier(allRecords);

            Path uniqueCompoundsExcel = outputFolder.resolve("massbank_unique_compounds.xlsx");
            Path spectraByCompoundExcel = outputFolder.resolve("massbank_spectra_by_compound.xlsx");

            System.out.println("Exporting unique compound list...");
            MassBankExcelExporter.exportUniqueCompounds(uniqueCompoundGroups, uniqueCompoundsExcel);

            System.out.println("Exporting spectra associated with each compound...");
            MassBankExcelExporter.exportSpectraByCompound(uniqueCompoundGroups, spectraByCompoundExcel);

            printSummary(allRecords, uniqueCompoundGroups, uniqueCompoundsExcel, spectraByCompoundExcel);

        } catch (IOException e) {
            System.err.println("I/O error while processing the MassBank file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while processing the MassBank file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void validateInputFile(Path inputJsonPath) throws IOException {
        if (!Files.exists(inputJsonPath)) {
            throw new IOException("Input JSON file does not exist: " + inputJsonPath.toAbsolutePath());
        }

        if (!Files.isRegularFile(inputJsonPath)) {
            throw new IOException("Input path is not a regular file: " + inputJsonPath.toAbsolutePath());
        }
    }

    private static void printSummary(List<MassBankSimpleRecord> allRecords,
                                     List<MassBankCompoundGroup> uniqueCompoundGroups,
                                     Path uniqueCompoundsExcel,
                                     Path spectraByCompoundExcel) {

        int recordsWithoutChemicalIdentifier = 0;
        int compoundsWithMultipleSpectra = 0;

        for (MassBankSimpleRecord record : allRecords) {
            if (!record.hasChemicalIdentifier()) {
                recordsWithoutChemicalIdentifier++;
            }
        }

        for (MassBankCompoundGroup group : uniqueCompoundGroups) {
            if (group.getNumberOfSpectra() > 1) {
                compoundsWithMultipleSpectra++;
            }
        }

        System.out.println();
        System.out.println("MassBank processing finished");
        System.out.println("----------------------------------------");
        System.out.println("Total MassBank records/spectra read: " + allRecords.size());
        System.out.println("Unique compounds detected: " + uniqueCompoundGroups.size());
        System.out.println("Compounds with more than one spectrum: " + compoundsWithMultipleSpectra);
        System.out.println("Records without InChI/InChIKey: " + recordsWithoutChemicalIdentifier);
        System.out.println();
        System.out.println("Generated files:");
        System.out.println("  " + uniqueCompoundsExcel.toAbsolutePath());
        System.out.println("  " + spectraByCompoundExcel.toAbsolutePath());
    }
}
