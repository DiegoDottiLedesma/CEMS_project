package GCMS.miniNDBHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simple executable class to test that the Excel is correctly transformed into a Java list.
 */
public class MiniNExcelToListMain {

    public static void main(String[] args) {
        Path excelPath;

        if (args.length > 0) {
            excelPath = Paths.get(args[0]);
        } else {
            excelPath = Paths.get("C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\A GC-MS Database of Nitrogen-Rich Volatile Compounds\\minini.xlsx");
        }

        try {
            List<MiniNCompoundRecord> compounds = ReadExcelMiniN.readExcelMiniN(excelPath);

            System.out.println("Compuestos leidos: " + compounds.size());
            compounds.stream()
                    .limit(compounds.size())
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error leyendo el Excel Mini-N:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
