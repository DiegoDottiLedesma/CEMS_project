package GCMS.GCBinBaseCuratedHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simple executable class to test that the GCBinbase TXT/MSP file is correctly
 * transformed into a Java list.
 */
public class GCBinbaseTxtToListMain {

    public static void main(String[] args) {
        Path txtPath;

        if (args.length > 0) {
            txtPath = Paths.get(args[0]);
        } else {
            txtPath = Paths.get("C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\GCBinbase_knowns_curated\\GCBinbase_knowns_curated.msp.txt");
        }

        try {
            List<GCBinbaseCompoundRecord> compounds = ReadTxtGCBinbase.readTxtGCBinbase(txtPath);

            System.out.println("Compuestos leidos: " + compounds.size());
            compounds.stream()
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error leyendo el TXT/MSP de GCBinbase:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
