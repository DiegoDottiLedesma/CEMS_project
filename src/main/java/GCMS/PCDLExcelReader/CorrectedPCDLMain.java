package GCMS.PCDLExcelReader;

import java.io.IOException;
import java.util.List;

public class CorrectedPCDLMain {

    public static void main(String[] args) throws IOException {
        String excelPath;

        if (args.length > 0) {
            excelPath = args[0];
        } else {
            excelPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\PCDL Methyl chloroformates MBF\\PCDL Methyl chloroformates MBF\\PCDL.xlsx";
        }

        List<PCDLCorrectedCompoundRecord> compounds = ReadExcelPCDLCorrected.read(excelPath);

        System.out.println("Compuestos leídos: " + compounds.size());
        compounds.stream()
                .limit(compounds.size())
                .forEach(System.out::println);

    }
}
