package GCMS.Pipeline;

import PCDLExcelReader.PCDLCompoundRecord;
import PCDLExcelReader.ReadExcelPCDL;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String excelPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\PCDL Methyl chloroformates MBF\\PCDL Methyl chloroformates MBF\\PCDL.xlsx";

        try {
            List<PCDLCompoundRecord> compounds = ReadExcelPCDL.read(excelPath);

            System.out.println("Compuestos leídos: " + compounds.size());

            for (int i = 0; i < Math.min(10, compounds.size()); i++) {
                System.out.println(compounds.get(i));
            }

        } catch (IOException e) {
            System.err.println("Error leyendo el Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
