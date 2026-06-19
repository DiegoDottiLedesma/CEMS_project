package GCMS.PCDLExcelReader;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReadExcelPCDL {

    private static final String DEFAULT_SHEET_NAME = "Librería tras corregirla";

    public static List<PCDLCompoundRecord> read(String excelPath) throws IOException {
        List<PCDLCompoundRecord> compounds = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new File(excelPath))) {

            Sheet sheet = workbook.getSheet(DEFAULT_SHEET_NAME);

            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
                System.out.println("No se encontró la hoja '" + DEFAULT_SHEET_NAME +
                        "'. Se usará la primera hoja: " + sheet.getSheetName());
            }

            Map<String, Integer> columnMap = getColumnMap(sheet);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                String name = getString(row, columnMap, "name");
                String formula = getString(row, columnMap, "formula");
                Double mass = getDouble(row, columnMap, "mass");
                Double rt = getDouble(row, columnMap, "retention time");
                Double ri = getDouble(row, columnMap, "retention index");
                String cas = getString(row, columnMap, "cas");
                Integer pubChemId = getInteger(row, columnMap, "pubchem");
                Integer numSpectra = getInteger(row, columnMap, "numspectra");

                if (name == null || name.isBlank()) {
                    System.out.println("Fila " + (rowIndex + 1) + " ignorada: no tiene nombre.");
                    continue;
                }

                PCDLCompoundRecord compound = new PCDLCompoundRecord(
                        name,
                        formula,
                        mass,
                        rt,
                        ri,
                        cas,
                        pubChemId,
                        numSpectra
                );

                compounds.add(compound);
            }
        }

        return compounds;
    }

    private static Map<String, Integer> getColumnMap(Sheet sheet) {
        Map<String, Integer> columnMap = new HashMap<>();

        Row headerRow = sheet.getRow(0);

        if (headerRow == null) {
            throw new IllegalArgumentException("El Excel no tiene fila de cabecera.");
        }

        DataFormatter formatter = new DataFormatter();

        for (Cell cell : headerRow) {
            String header = formatter.formatCellValue(cell);

            if (header != null && !header.isBlank()) {
                columnMap.put(normalizeHeader(header), cell.getColumnIndex());
            }
        }

        return columnMap;
    }

    private static String normalizeHeader(String header) {
        return header
                .trim()
                .toLowerCase()
                .replace("_", " ")
                .replace("-", " ")
                .replaceAll("\\s+", " ");
    }

    private static String getString(Row row, Map<String, Integer> columnMap, String columnName) {
        Integer columnIndex = columnMap.get(normalizeHeader(columnName));

        if (columnIndex == null) {
            return null;
        }

        Cell cell = row.getCell(columnIndex);

        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell);

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private static Double getDouble(Row row, Map<String, Integer> columnMap, String columnName) {
        String value = getString(row, columnMap, columnName);

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            value = value.replace(",", ".");
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer getInteger(Row row, Map<String, Integer> columnMap, String columnName) {
        Double value = getDouble(row, columnMap, columnName);

        if (value == null) {
            return null;
        }

        return value.intValue();
    }

    private static boolean isRowEmpty(Row row) {
        DataFormatter formatter = new DataFormatter();

        for (Cell cell : row) {
            String value = formatter.formatCellValue(cell);

            if (value != null && !value.isBlank()) {
                return false;
            }
        }

        return true;
    }
}
