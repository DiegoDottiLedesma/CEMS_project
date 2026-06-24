package GCMS.HMDBXmlReader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {

    private static final String SHEET_NAME = "Unique HMDB compounds";

    public static void write(List<HmdbCompoundRecord> compounds, String outputExcelPath) throws IOException {
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

            createHeader(sheet, headerStyle);
            fillRows(sheet, compounds, textStyle, integerStyle);
            configureSheet(sheet);

            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                workbook.write(outputStream);
            }
        }
    }

    private static void createHeader(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        String[] headers = {
                "HMDB ID",
                "Name",
                "Formula",
                "Monoisotopic Mass",
                "SMILES",
                "InChI",
                "InChIKey",
                "Number of Spectra"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static void fillRows(
            Sheet sheet,
            List<HmdbCompoundRecord> compounds,
            CellStyle textStyle,
            CellStyle integerStyle
    ) {
        for (int i = 0; i < compounds.size(); i++) {
            HmdbCompoundRecord compound = compounds.get(i);
            Row row = sheet.createRow(i + 1);

            createTextCell(row, 0, compound.getHmdbId(), textStyle);
            createTextCell(row, 1, compound.getName(), textStyle);
            createTextCell(row, 2, compound.getFormula(), textStyle);
            createTextCell(row, 3, compound.getMonoisotopicMass(), textStyle);
            createTextCell(row, 4, compound.getSmiles(), textStyle);
            createTextCell(row, 5, compound.getInchi(), textStyle);
            createTextCell(row, 6, compound.getInchiKey(), textStyle);

            Cell spectraCountCell = row.createCell(7);
            spectraCountCell.setCellValue(compound.getSpectraCount());
            spectraCountCell.setCellStyle(integerStyle);
        }
    }

    private static void createTextCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);

        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }

        cell.setCellStyle(style);
    }

    private static void configureSheet(Sheet sheet) {
        sheet.createFreezePane(0, 1);
        sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 7));

        for (int i = 0; i <= 7; i++) {
            sheet.autoSizeColumn(i);
        }

        sheet.setColumnWidth(0, 16 * 256);
        sheet.setColumnWidth(1, 35 * 256);
        sheet.setColumnWidth(4, 45 * 256);
        sheet.setColumnWidth(5, 65 * 256);
        sheet.setColumnWidth(6, 32 * 256);
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
}
