package GCMS.HMDBXmlReader.enrichedHandler;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HmdbGcmsSpectraExcelWriter {

    private static final String SHEET_NAME = "HMDB GC-MS spectra";

    public static void write(List<EnrichedSpectrumRecord> records, String outputExcelPath) throws IOException {
        File outputFile = new File(outputExcelPath);
        File parentFolder = outputFile.getParentFile();

        if (parentFolder != null && !parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle textStyle = createTextStyle(workbook);
            CellStyle integerStyle = createIntegerStyle(workbook);
            CellStyle decimalStyle = createDecimalStyle(workbook);

            createHeader(sheet, headerStyle);
            fillRows(sheet, records, textStyle, integerStyle, decimalStyle);
            configureSheet(sheet);

            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                workbook.write(outputStream);
            }
        }
    }

    private static void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        String[] headers = {
                "Source File",
                "Instrument Type",
                "Peak Counter",
                "Created At",
                "Updated At",
                "Chromatography Type",
                "Retention Index",
                "Retention Time",
                "Ionization Mode",
                "Column Type",
                "Derivative Type",
                "HMDB ID",
                "InChI",
                "InChIKey",
                "HMDB Name",
                "HMDB Chemical Formula",
                "HMDB SMILES"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static void fillRows(
            Sheet sheet,
            List<EnrichedSpectrumRecord> records,
            CellStyle textStyle,
            CellStyle integerStyle,
            CellStyle decimalStyle
    ) {
        for (int i = 0; i < records.size(); i++) {
            EnrichedSpectrumRecord record = records.get(i);
            Row row = sheet.createRow(i + 1);

            createTextCell(row, 0, record.getSourceFileName(), textStyle);
            createTextCell(row, 1, record.getInstrumentType(), textStyle);
            createIntegerCell(row, 2, record.getPeakCounter(), integerStyle);
            createTextCell(row, 3, record.getCreatedAt(), textStyle);
            createTextCell(row, 4, record.getUpdatedAt(), textStyle);
            createTextCell(row, 5, record.getChromatographyType(), textStyle);
            createDoubleCell(row, 6, record.getRetentionIndex(), decimalStyle);
            createTextCell(row, 7, record.getRetentionTime(), textStyle);
            createTextCell(row, 8, record.getIonizationMode(), textStyle);
            createTextCell(row, 9, record.getColumnType(), textStyle);
            createTextCell(row, 10, record.getDerivativeType(), textStyle);
            createTextCell(row, 11, record.getDatabaseId(), textStyle);
            createTextCell(row, 12, record.getInchi(), textStyle);
            createTextCell(row, 13, record.getInchiKey(), textStyle);
            createTextCell(row, 14, record.getHmdbName(), textStyle);
            createTextCell(row, 15, record.getHmdbChemicalFormula(), textStyle);
            createTextCell(row, 16, record.getHmdbSmiles(), textStyle);
        }
    }

    private static void createTextCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private static void createIntegerCell(Row row, int columnIndex, Integer value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);

        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setBlank();
        }

        cell.setCellStyle(style);
    }

    private static void createDoubleCell(Row row, int columnIndex, Double value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);

        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setBlank();
        }

        cell.setCellStyle(style);
    }

    private static void configureSheet(Sheet sheet) {
        sheet.createFreezePane(0, 1);
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 16));

        for (int i = 0; i <= 16; i++) {
            sheet.autoSizeColumn(i);
        }

        sheet.setColumnWidth(0, 45 * 256);
        sheet.setColumnWidth(9, 55 * 256);
        sheet.setColumnWidth(12, 80 * 256);
        sheet.setColumnWidth(13, 32 * 256);
        sheet.setColumnWidth(16, 45 * 256);
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle style = workbook.createCellStyle();
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private static CellStyle createTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(false);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private static CellStyle createIntegerStyle(Workbook workbook) {
        CellStyle style = createTextStyle(workbook);
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("0"));
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createDecimalStyle(Workbook workbook) {
        CellStyle style = createTextStyle(workbook);
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("0.00"));
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}