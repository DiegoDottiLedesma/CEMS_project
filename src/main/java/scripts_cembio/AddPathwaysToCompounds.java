package scripts_cembio;

import cems_project.Compound;
import cems_project.Identifier;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import dbmanager.ChebiDatabase;
import dbmanager.DBManager;
import dbmanager.KeggRESTAPI;
import dbmanager.PubchemRest;
import exceptions.ChebiException;
import exceptions.CompoundNotFoundException;
import exceptions.KeggCompoundNotFoundException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pathways.PathwayKegg;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AddPathwaysToCompounds {

    public static void main(String[] args) throws Exception {
        String inputCsv = "/home/ceu/Escritorio/cmm_temporal/compound_chebis.csv";
        String outputXlsx = "/home/ceu/Escritorio/cmm_temporal/compound_chebis_with_pathways.xlsx";

        try (BufferedReader br = new BufferedReader(new FileReader(inputCsv));
             XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet("Compounds");

            String headerLine = br.readLine();
            String[] headers = headerLine.split(";");
            // Add "KEGG Pathways" as the starting column for pathways
            Row headerRow = sheet.createRow(0);
            int col = 0;
            for (String h : headers) {
                Cell cell = headerRow.createCell(col++);
                cell.setCellValue(h);
            }
            Cell pathwayHeaderCell = headerRow.createCell(col++);
            pathwayHeaderCell.setCellValue("KEGG Pathways");

            String line;
            int rowIdx = 1;
            CreationHelper creationHelper = workbook.getCreationHelper();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                Row row = sheet.createRow(rowIdx++);


                for (int i = 0; i < parts.length; i++) {
                    String value = parts[i].trim();
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    Cell cell = row.createCell(i);
                    cell.setCellValue(value.trim().equals("-") ? "-" : value.trim());
                }

                String value = parts[3].trim();
                if (value.isEmpty()) {
                    break;
                } else if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                String keggId = value.trim();
                List<PathwayKegg> pathways;
                try {
                    pathways = KeggRESTAPI.getPathwaysFromCompound(keggId);
                } catch (KeggCompoundNotFoundException e) {
                    pathways = List.of(); // empty list if not found
                }

                int pathwayCol = parts.length; // start after last CSV column
                for (PathwayKegg pw : pathways) {
                    Cell cell = row.createCell(pathwayCol++);
                    Hyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
                    link.setAddress("https://www.kegg.jp/dbget-bin/www_bget?" + pw.getPathwayId());
                    cell.setHyperlink(link);
                    cell.setCellValue(pw.getPathwayName());
                }

            }

            // Auto-size columns
            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to XLSX
            try (FileOutputStream fos = new FileOutputStream(outputXlsx)) {
                workbook.write(fos);
            }
        }

        System.out.println("✅ Excel file written to " + outputXlsx);
    }
}