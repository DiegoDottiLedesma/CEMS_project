package GCMS.PCDLExcelReader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

public class ReadExcelPCDLCorrected {

    private static final String DEFAULT_SHEET_NAME = "Librería tras corregirla";
    private static final DataFormatter FORMATTER = new DataFormatter();

    public static List<PCDLCorrectedCompoundRecord> read(String excelPath) throws IOException {
        List<PCDLCorrectedCompoundRecord> compounds = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new File(excelPath))) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = findSheet(workbook, DEFAULT_SHEET_NAME);

            int headerRowIndex = findHeaderRowIndex(sheet, evaluator);
            Row headerRow = sheet.getRow(headerRowIndex);
            Map<String, Integer> columnMap = getColumnMap(headerRow, evaluator);

            for (int rowIndex = headerRowIndex + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null || isRowEmpty(row, evaluator)) {
                    continue;
                }

                String name = getString(row, columnMap, evaluator, "name");

                if (name == null || name.isBlank()) {
                    System.out.println("Fila " + (rowIndex + 1) + " ignorada: no tiene nombre.");
                    continue;
                }

                String formula = getString(row, columnMap, evaluator, "formula");
                Double mass = getDouble(row, columnMap, evaluator, "mass");
                Double retentionTime = getDouble(row, columnMap, evaluator, "retention time");
                Double retentionIndex = getDouble(row, columnMap, evaluator, "retention index");
                String cation = getString(row, columnMap, evaluator, "cation");
                String anion = getString(row, columnMap, evaluator, "anion");
                String cas = getString(row, columnMap, evaluator, "cas");
                Integer chemSpiderId = getInteger(row, columnMap, evaluator, "chemspider");

                /*
                 * En esta hoja corregida, algunas filas tienen el PubChem ID desplazado
                 * una columna hacia la derecha. Por eso primero se lee por cabecera y,
                 * si está vacío, se prueba la columna K.
                 */
                Integer pubChemId = getInteger(row, columnMap, evaluator, "pubchem");
                if (pubChemId == null) {
                    pubChemId = getIntegerAt(row, col("K"), evaluator);
                }

                String synonyms = getString(row, columnMap, evaluator, "synonyms");
                if (isSameInteger(synonyms, pubChemId)) {
                    synonyms = null;
                }

                String iupac = getString(row, columnMap, evaluator, "iupac");

                /*
                 * En las filas desplazadas, NumSpectra aparece en la columna N.
                 * Se acepta solo si es un entero pequeño para no confundirlo con fechas.
                 */
                Integer numSpectra = getInteger(row, columnMap, evaluator, "numspectra");
                if (numSpectra == null) {
                    numSpectra = getSmallIntegerAt(row, col("N"), evaluator, 1000);
                }

                Integer ccsCount = getInteger(row, columnMap, evaluator, "ccs count");
                if (ccsCount == null) {
                    ccsCount = getSmallIntegerAt(row, col("Q"), evaluator, 1000);
                }

                String inchiKey = firstNonBlank(
                        getString(row, columnMap, evaluator, "inchikey"),
                        getStringAt(row, col("R"), evaluator)
                );

                String inchi = normalizeInchi(firstNonBlank(
                        getString(row, columnMap, evaluator, "inchi"),
                        getStringAt(row, col("S"), evaluator)
                ));

                PCDLCorrectedCompoundRecord compound = new PCDLCorrectedCompoundRecord(
                        name,
                        formula,
                        mass,
                        retentionTime,
                        retentionIndex,
                        cation,
                        anion,
                        cas,
                        chemSpiderId,
                        pubChemId,
                        synonyms,
                        iupac,
                        numSpectra,
                        ccsCount,
                        inchiKey,
                        inchi
                );

                compounds.add(compound);
            }
        }

        return compounds;
    }

    private static Sheet findSheet(Workbook workbook, String expectedName) {
        Sheet exactSheet = workbook.getSheet(expectedName);

        if (exactSheet != null) {
            return exactSheet;
        }

        String expectedNormalized = normalizeHeader(expectedName);

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (normalizeHeader(sheet.getSheetName()).equals(expectedNormalized)) {
                return sheet;
            }
        }

        Sheet fallback = workbook.getSheetAt(0);
        System.out.println("No se encontró la hoja '" + expectedName + "'. Se usará la primera hoja: " + fallback.getSheetName());
        return fallback;
    }

    private static int findHeaderRowIndex(Sheet sheet, FormulaEvaluator evaluator) {
        int maxRowsToCheck = Math.min(sheet.getLastRowNum(), 10);

        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= maxRowsToCheck; rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if (row == null) {
                continue;
            }

            Map<String, Integer> columnMap = getColumnMap(row, evaluator);

            if (columnMap.containsKey("name")
                    && columnMap.containsKey("formula")
                    && columnMap.containsKey("mass")) {
                return rowIndex;
            }
        }

        throw new IllegalArgumentException("No se ha encontrado una fila de cabecera válida en la hoja: " + sheet.getSheetName());
    }

    private static Map<String, Integer> getColumnMap(Row headerRow, FormulaEvaluator evaluator) {
        Map<String, Integer> columnMap = new HashMap<>();

        for (Cell cell : headerRow) {
            String header = getStringFromCell(cell, evaluator);

            if (header != null && !header.isBlank()) {
                columnMap.putIfAbsent(normalizeHeader(header), cell.getColumnIndex());
            }
        }

        return columnMap;
    }

    private static String normalizeHeader(String header) {
        if (header == null) {
            return "";
        }

        String withoutAccents = Normalizer.normalize(header, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return withoutAccents
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace("_", " ")
                .replace("-", " ")
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String getString(Row row, Map<String, Integer> columnMap, FormulaEvaluator evaluator, String columnName) {
        Integer columnIndex = columnMap.get(normalizeHeader(columnName));

        if (columnIndex == null) {
            return null;
        }

        return getStringAt(row, columnIndex, evaluator);
    }

    private static String getStringAt(Row row, int columnIndex, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

        if (cell == null) {
            return null;
        }

        return getStringFromCell(cell, evaluator);
    }

    private static String getStringFromCell(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return null;
        }

        String value = FORMATTER.formatCellValue(cell, evaluator);
        value = clean(value);

        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }

    private static Double getDouble(Row row, Map<String, Integer> columnMap, FormulaEvaluator evaluator, String columnName) {
        Integer columnIndex = columnMap.get(normalizeHeader(columnName));

        if (columnIndex == null) {
            return null;
        }

        return getDoubleAt(row, columnIndex, evaluator);
    }

    private static Double getDoubleAt(Row row, int columnIndex, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

        if (cell == null) {
            return null;
        }

        Double numericValue = getRawNumericValue(cell, evaluator);
        if (numericValue != null) {
            return numericValue;
        }

        String value = getStringFromCell(cell, evaluator);

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

    private static Double getRawNumericValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }

        if (cell.getCellType() == CellType.FORMULA) {
            CellValue evaluatedCell = evaluator.evaluate(cell);

            if (evaluatedCell != null && evaluatedCell.getCellType() == CellType.NUMERIC) {
                return evaluatedCell.getNumberValue();
            }
        }

        return null;
    }

    private static Integer getInteger(Row row, Map<String, Integer> columnMap, FormulaEvaluator evaluator, String columnName) {
        Integer columnIndex = columnMap.get(normalizeHeader(columnName));

        if (columnIndex == null) {
            return null;
        }

        return getIntegerAt(row, columnIndex, evaluator);
    }

    private static Integer getIntegerAt(Row row, int columnIndex, FormulaEvaluator evaluator) {
        Double value = getDoubleAt(row, columnIndex, evaluator);

        if (value == null) {
            return null;
        }

        return (int) Math.round(value);
    }

    private static Integer getSmallIntegerAt(Row row, int columnIndex, FormulaEvaluator evaluator, int maxValue) {
        Integer value = getIntegerAt(row, columnIndex, evaluator);

        if (value == null) {
            return null;
        }

        if (value < 0 || value > maxValue) {
            return null;
        }

        return value;
    }

    private static boolean isRowEmpty(Row row, FormulaEvaluator evaluator) {
        for (Cell cell : row) {
            String value = getStringFromCell(cell, evaluator);

            if (value != null && !value.isBlank()) {
                return false;
            }
        }

        return true;
    }

    private static int col(String excelColumnName) {
        return CellReference.convertColStringToIndex(excelColumnName);
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value
                .replace('\u00A0', ' ')
                .replace("\uFEFF", "")
                .trim();

        if (cleaned.isBlank()) {
            return null;
        }

        return cleaned;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            String cleaned = clean(value);

            if (cleaned != null && !cleaned.isBlank()) {
                return cleaned;
            }
        }

        return null;
    }

    private static String normalizeInchi(String inchi) {
        inchi = clean(inchi);

        if (inchi == null) {
            return null;
        }

        if (inchi.startsWith("InChI=")) {
            return inchi;
        }

        if (inchi.startsWith("1S/") || inchi.startsWith("1/")) {
            return "InChI=" + inchi;
        }

        return inchi;
    }

    private static boolean isSameInteger(String text, Integer number) {
        if (text == null || number == null) {
            return false;
        }

        try {
            double parsed = Double.parseDouble(text.replace(",", "."));
            return Math.round(parsed) == number;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
