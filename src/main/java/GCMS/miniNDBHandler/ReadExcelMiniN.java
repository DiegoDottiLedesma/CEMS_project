package GCMS.miniNDBHandler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Reader for the Mini-N GC-MS Excel database.
 *
 * The file contains several descriptive rows before the real table. This class searches
 * automatically for the header row containing: N, Name, SMILES, InChI, etc.
 */
public class ReadExcelMiniN {

    private static final DataFormatter DATA_FORMATTER = new DataFormatter(Locale.US);

    public static List<MiniNCompoundRecord> readExcelMiniN(String excelPath) throws IOException {
        return readExcelMiniN(Paths.get(excelPath));
    }

    public static List<MiniNCompoundRecord> readExcelMiniN(Path excelPath) throws IOException {
        List<MiniNCompoundRecord> compounds = new ArrayList<>();

        try (InputStream inputStream = Files.newInputStream(excelPath);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = workbook.getSheetAt(0);

            int headerRowIndex = findHeaderRow(sheet, evaluator);
            Row headerRow = sheet.getRow(headerRowIndex);
            Map<String, Integer> columns = mapColumns(headerRow, evaluator);

            int nameColumn = getRequiredColumn(columns, "name");
            int smilesColumn = getRequiredColumn(columns, "smiles");
            int inchiColumn = getRequiredColumn(columns, "inchi");
            int ssnpColumn = getRequiredColumn(columns, "ssnp");
            int spColumn = getRequiredColumn(columns, "sp");
            int inchiKeyColumn = getRequiredColumn(columns, "inchikey");
            int mfColumn = getRequiredColumn(columns, "mf");

            for (int rowIndex = headerRowIndex + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String name = clean(getCellText(row, nameColumn, evaluator));

                // Stop/skip empty rows. In this Excel the data table has no empty rows,
                // but this makes the parser safer if the file is modified later.
                if (name == null || name.isEmpty()) {
                    continue;
                }

                String smiles = clean(getCellText(row, smilesColumn, evaluator));
                String inchi = clean(getCellText(row, inchiColumn, evaluator));
                Double retentionIndexSSNP = parseDoubleOrNull(getCellText(row, ssnpColumn, evaluator));
                Double retentionIndexSP = parseDoubleOrNull(getCellText(row, spColumn, evaluator));
                String inchiKey = clean(getCellText(row, inchiKeyColumn, evaluator));
                String molecularFormula = clean(getCellText(row, mfColumn, evaluator));

                MiniNCompoundRecord compound = new MiniNCompoundRecord(
                        name,
                        smiles,
                        inchi,
                        retentionIndexSSNP,
                        retentionIndexSP,
                        inchiKey,
                        molecularFormula
                );

                compounds.add(compound);
            }
        } catch (Exception e) {
            throw new IOException("Error reading Mini-N Excel file: " + excelPath, e);
        }

        return compounds;
    }

    private static int findHeaderRow(Sheet sheet, FormulaEvaluator evaluator) throws IOException {
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            boolean hasName = false;
            boolean hasSmiles = false;
            boolean hasInchi = false;
            boolean hasInchiKey = false;
            boolean hasMf = false;

            for (Cell cell : row) {
                String value = normalizeHeader(getCellText(row, cell.getColumnIndex(), evaluator));
                if ("name".equals(value)) {
                    hasName = true;
                } else if ("smiles".equals(value)) {
                    hasSmiles = true;
                } else if ("inchi".equals(value)) {
                    hasInchi = true;
                } else if ("inchikey".equals(value)) {
                    hasInchiKey = true;
                } else if ("mf".equals(value)) {
                    hasMf = true;
                }
            }

            if (hasName && hasSmiles && hasInchi && hasInchiKey && hasMf) {
                return rowIndex;
            }
        }

        throw new IOException("Header row not found. Expected columns: Name, SMILES, InChI, InChIKey and MF.");
    }

    private static Map<String, Integer> mapColumns(Row headerRow, FormulaEvaluator evaluator) {
        Map<String, Integer> columns = new HashMap<>();

        for (Cell cell : headerRow) {
            String header = normalizeHeader(getCellText(headerRow, cell.getColumnIndex(), evaluator));
            if (!header.isEmpty()) {
                columns.put(header, cell.getColumnIndex());
            }
        }

        return columns;
    }

    private static int getRequiredColumn(Map<String, Integer> columns, String columnName) throws IOException {
        Integer columnIndex = columns.get(columnName);
        if (columnIndex == null) {
            throw new IOException("Required column not found in Mini-N Excel: " + columnName);
        }
        return columnIndex;
    }

    private static String getCellText(Row row, int columnIndex, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        }
        return DATA_FORMATTER.formatCellValue(cell, evaluator);
    }

    private static String normalizeHeader(String value) {
        if (value == null) {
            return "";
        }
        return value
                .trim()
                .replace(" ", "")
                .replace("-", "")
                .toLowerCase(Locale.ROOT);
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.trim();
        if (cleaned.isEmpty() || "-".equals(cleaned)) {
            return null;
        }
        return cleaned;
    }

    private static Double parseDoubleOrNull(String value) {
        String cleaned = clean(value);
        if (cleaned == null) {
            return null;
        }

        try {
            return Double.parseDouble(cleaned.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
