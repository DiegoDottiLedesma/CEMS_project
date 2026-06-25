package GCMS.Pipeline;

import GCMS.JSONMassBankHandler.MassBankSimpleJsonParser;
import GCMS.JSONMassBankHandler.MassBankSimpleRecord;
import GCMS.JSONMassBankHandler.SpectrumPeak;

import java.io.IOException;
import java.util.List;

public class MassBankMain {

    public static void main(String[] args) {

        String jsonPath = "C:\\Users\\diego\\OneDrive\\Escritorio\\CEU2026\\Proyecto\\bases de datos\\MoNA-export-GC-MS_Spectra.json";

        try {
            List<MassBankSimpleRecord> records =
                    MassBankSimpleJsonParser.readJsonLines(jsonPath);

            System.out.println("Registros leídos: " + records.size());

            int numberOfRecordsToPrint =  records.size();

            for (int i = 0; i < numberOfRecordsToPrint; i++) {
                MassBankSimpleRecord record = records.get(i);

                System.out.println("====================================");
                System.out.println("Registro " + (i + 1));
                System.out.println(record);

                System.out.println("Primeros 10 picos del espectro:");

                for (int j = 0; j < Math.min(10, record.getPeaks().size()); j++) {
                    SpectrumPeak peak = record.getPeaks().get(j);
                    System.out.println(peak);
                }

                System.out.println();
            }

        } catch (IOException e) {
            System.err.println("Error leyendo JSON:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}