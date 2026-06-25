package GCMS.JSONMassBankHandler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MassBankExcelExporter {

    private static final int MAX_EXCEL_CELL_LENGTH = 30000;

    private MassBankExcelExporter() {
        // Utility class.
    }

    /**
     * Exports one row per unique compound.
     */
    public static void exportUniqueCompounds(List<MassBankCompoundGroup> groups, Path outputPath) throws IOException {
        createParentFolder(outputPath);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Unique compounds");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {
                    "Grouping Key Type",
                    "Grouping Key",
                    "Compound Name",
                    "Formula",
                    "Exact Mass",
                    "CAS",
                    "SMILES",
                    "InChI",
                    "InChIKey",
                    "Number of Spectra"
            };

            createHeaderRow(sheet, headers, headerStyle);

            int rowIndex = 1;
            for (MassBankCompoundGroup group : groups) {
                MassBankSimpleRecord representative = group.getRepresentativeRecord();
                Row row = sheet.createRow(rowIndex++);

                int column = 0;
                setCell(row, column++, group.getGroupingKeyType());
                setCell(row, column++, group.getGroupingKey());
                setCell(row, column++, representative.getCompoundName());
                setCell(row, column++, representative.getFormula());
                setCell(row, column++, representative.getExactMass());
                setCell(row, column++, representative.getCas());
                setCell(row, column++, representative.getSmiles());
                setCell(row, column++, representative.getInchi());
                setCell(row, column++, representative.getInchiKey());
                setCell(row, column, group.getNumberOfSpectra());
            }

            autoSizeColumns(sheet, headers.length);
            writeWorkbook(workbook, outputPath);
        }
    }

    /**
     * Exports one row per MassBank spectrum/record, preserving its association with the unique compound group.
     */
    public static void exportSpectraByCompound(List<MassBankCompoundGroup> groups, Path outputPath) throws IOException {
        createParentFolder(outputPath);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Spectra by compound");
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {
                    "Grouping Key Type",
                    "Grouping Key",
                    "Compound Name",
                    "Accession",
                    "Formula",
                    "Exact Mass",
                    "CAS",
                    "SMILES",
                    "InChI",
                    "InChIKey",
                    "Retention Index",
                    "Retention Time Seconds",
                    "Retention Time Minutes",
                    "Derivatization Type",
                    "Instrument Type",
                    "Number of Peaks",
                    "Spectrum Peaks"
            };

            createHeaderRow(sheet, headers, headerStyle);

            int rowIndex = 1;
            for (MassBankCompoundGroup group : groups) {
                for (MassBankSimpleRecord record : group.getSpectrumRecords()) {
                    Row row = sheet.createRow(rowIndex++);

                    int column = 0;
                    setCell(row, column++, group.getGroupingKeyType());
                    setCell(row, column++, group.getGroupingKey());
                    setCell(row, column++, record.getCompoundName());
                    setCell(row, column++, record.getAccession());
                    setCell(row, column++, record.getFormula());
                    setCell(row, column++, record.getExactMass());
                    setCell(row, column++, record.getCas());
                    setCell(row, column++, record.getSmiles());
                    setCell(row, column++, record.getInchi());
                    setCell(row, column++, record.getInchiKey());
                    setCell(row, column++, record.getRetentionIndex());
                    setCell(row, column++, record.getRetentionTimeSeconds());
                    setCell(row, column++, record.getRetentionTimeMinutes());
                    setCell(row, column++, record.getDerivatizationType());
                    setCell(row, column++, record.getInstrumentType());
                    setCell(row, column++, record.getNumberOfPeaks());
                    setCell(row, column, record.getTruncatedSpectrumAsText(MAX_EXCEL_CELL_LENGTH));
                }
            }

            autoSizeColumns(sheet, headers.length);
            writeWorkbook(workbook, outputPath);
        }
    }

    private static void createHeaderRow(Sheet sheet, String[] headers, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private static void setCell(Row row, int columnIndex, String value) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "" : value);
    }

    private static void setCell(Row row, int columnIndex, Double value) {
        Cell cell = row.createCell(columnIndex);
        if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value);
        }
    }

    private static void setCell(Row row, int columnIndex, int value) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
    }

    private static void autoSizeColumns(Sheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void writeWorkbook(Workbook workbook, Path outputPath) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(outputPath)) {
            workbook.write(outputStream);
        }
    }

    private static void createParentFolder(Path outputPath) throws IOException {
        Path parent = outputPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
