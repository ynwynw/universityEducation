package com.edu.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;

/**
 * Office文件转PDF工具类
 */
public class OfficeToPdfConverter {

    private static BaseFont baseFont;

    static {
        try {
            // 使用系统中文字体
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            try {
                baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 将Office件转换为PDF
     */
    public static File convert(File sourceFile) throws Exception {
        String name = sourceFile.getName().toLowerCase();
        String tempDir = System.getProperty("java.io.tmpdir") + "/pdf_preview";
        new File(tempDir).mkdirs();
        String pdfPath = tempDir + "/" + System.currentTimeMillis() + ".pdf";
        File pdfFile = new File(pdfPath);

        if (name.endsWith(".docx")) {
            convertDocxToPdf(sourceFile, pdfFile);
        } else if (name.endsWith(".doc")) {
            convertDocToPdf(sourceFile, pdfFile);
        } else if (name.endsWith(".xlsx")) {
            convertXlsxToPdf(sourceFile, pdfFile);
        } else if (name.endsWith(".xls")) {
            convertXlsToPdf(sourceFile, pdfFile);
        } else {
            throw new Exception("不支持的文件格式");
        }

        return pdfFile;
    }

    /**
     * DOCX转PDF
     */
    private static void convertDocxToPdf(File source, File target) throws Exception {
        try (FileInputStream fis = new FileInputStream(source);
             XWPFDocument doc = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(target)) {
            Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(pdfDoc, fos);
            pdfDoc.open();

            Font font = new Font(baseFont, 12, Font.NORMAL);

            for (XWPFParagraph para : doc.getParagraphs()) {
                String text = para.getText();
                if (text != null && !text.trim().isEmpty()) {
                    Paragraph p = new Paragraph(text, font);
                    pdfDoc.add(p);
                }
            }

            // 处理表格
            for (XWPFTable table : doc.getTables()) {
                int cols = table.getRow(0).getTableCells().size();
                PdfPTable pdfTable = new PdfPTable(cols);
                pdfTable.setWidthPercentage(100);
                pdfTable.setSpacingBefore(10);
                pdfTable.setSpacingAfter(10);

                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        PdfPCell pdfCell = new PdfPCell(new Phrase(cell.getText(), font));
                        pdfCell.setPadding(5);
                        pdfTable.addCell(pdfCell);
                    }
                }
                pdfDoc.add(pdfTable);
            }

            pdfDoc.close();
        }
    }

    /**
     * DOC 转 PDF
     */
    private static void convertDocToPdf(File source, File target) throws Exception {
        try (FileInputStream fis = new FileInputStream(source);
             HWPFDocument doc = new HWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(target)) {

            Document pdfDoc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(pdfDoc, fos);
            pdfDoc.open();

            Font font = new Font(baseFont, 12, Font.NORMAL);
            Range range = doc.getRange();
            String text = range.text();

            // 按段落分割
            String[] paragraphs = text.split("\r");
            for (String para : paragraphs) {
                if (para != null && !para.trim().isEmpty()) {
                    Paragraph p = new Paragraph(para.trim(), font);
                    pdfDoc.add(p);
                }
            }

            pdfDoc.close();
        }
    }

    /**
     * XLSX转PDF
     */
    private static void convertXlsxToPdf(File source, File target) throws Exception {
        try (FileInputStream fis = new FileInputStream(source);
             XSSFWorkbook workbook = new XSSFWorkbook(fis);
             FileOutputStream fos = new FileOutputStream(target)) {
            convertWorkbookToPdf(workbook, fos);
        }
    }

    /**
     * XLS转PDF
     */
    private static void convertXlsToPdf(File source, File target) throws Exception {
        try (FileInputStream fis = new FileInputStream(source);
             HSSFWorkbook workbook = new HSSFWorkbook(fis);
             FileOutputStream fos = new FileOutputStream(target)) {
            convertWorkbookToPdf(workbook, fos);
        }
    }

    /**
     * Excel Workbook 转 PDF
     */
    private static void convertWorkbookToPdf(Workbook workbook, FileOutputStream fos) throws Exception {
        Document pdfDoc = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(pdfDoc, fos);
        pdfDoc.open();

        Font font = new Font(baseFont, 10, Font.NORMAL);
        Font headerFont = new Font(baseFont, 10, Font.BOLD);

        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet.getPhysicalNumberOfRows() == 0) continue;

            // Sheet 名称
            pdfDoc.add(new Paragraph(sheet.getSheetName(), headerFont));
            pdfDoc.add(Chunk.NEWLINE);

            // 获取最大列数
            int maxCols = 0;
            for (Row row : sheet) {
                if (row.getLastCellNum() > maxCols) {
                    maxCols = row.getLastCellNum();
                }
            }
            if (maxCols == 0) continue;

            PdfPTable table = new PdfPTable(maxCols);
            table.setWidthPercentage(100);

            for (Row row : sheet) {
                for (int c = 0; c < maxCols; c++) {
                    Cell cell = row.getCell(c);
                    String value = getCellValue(cell);
                    PdfPCell pdfCell = new PdfPCell(new Phrase(value, font));
                    pdfCell.setPadding(3);
                    table.addCell(pdfCell);
                }
            }

            pdfDoc.add(table);
            if (sheetIndex < workbook.getNumberOfSheets() - 1) {
                pdfDoc.newPage();
            }
        }

        pdfDoc.close();
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }
}
