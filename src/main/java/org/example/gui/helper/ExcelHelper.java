package org.example.gui.helper;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    public static void xuatExcel(JTable table, Component parent, String tenSheet) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(tenSheet);


                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);


                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderTop(BorderStyle.THIN);
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);

                int rowIndex = 0;

                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = workbook.createFont();
                titleFont.setBold(true);
                titleFont.setFontHeightInPoints((short) 14);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);

                Row titleRow = sheet.createRow(rowIndex++);
                Cell titleCell = titleRow.createCell(0);

                titleCell.setCellValue(tenSheet);
                titleCell.setCellStyle(titleStyle);

                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, Math.max(1, table.getColumnCount() - 1)));

                Row dateRow = sheet.createRow(rowIndex++);

                int colDateLabel = Math.max(0, table.getColumnCount() - 2);
                int colDateValue = Math.max(1, table.getColumnCount() - 1);

                Cell dateLabelCell = dateRow.createCell(colDateLabel);
                dateLabelCell.setCellValue("Ngày xuất");

                Cell dateValueCell = dateRow.createCell(colDateValue);
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dateValueCell.setCellValue(currentDate);

                rowIndex++;

                Row headerRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                for (int i = 0; i < table.getRowCount(); i++) {
                    Row row = sheet.createRow(rowIndex++);
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = table.getValueAt(i, j);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellStyle(dataStyle);
                    }
                }

                for (int i = 0; i < table.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(parent, "Xuất file Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Lỗi khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void xuatExcel1Dong(JTable table, int row, Component parent, String tenSheet) {
        if (row < 0 || row >= table.getRowCount()) {
            JOptionPane.showMessageDialog(parent, "Dòng được chọn không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel cho dòng này");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet(tenSheet);

                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);

                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderTop(BorderStyle.THIN);
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);

                int rowIndex = 0;

                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = workbook.createFont();
                titleFont.setBold(true);
                titleFont.setFontHeightInPoints((short) 14);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);

                Row titleRow = sheet.createRow(rowIndex++);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue(tenSheet);
                titleCell.setCellStyle(titleStyle);

                int colCountToExport = table.getColumnCount() - 1;

                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, Math.max(1, colCountToExport - 1)));

                Row dateRow = sheet.createRow(rowIndex++);
                int colDateLabel = Math.max(0, colCountToExport - 2);
                int colDateValue = Math.max(1, colCountToExport - 1);

                Cell dateLabelCell = dateRow.createCell(colDateLabel);
                dateLabelCell.setCellValue("Ngày xuất");

                Cell dateValueCell = dateRow.createCell(colDateValue);
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dateValueCell.setCellValue(currentDate);

                rowIndex++;

                Row headerExcelRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < colCountToExport; i++) {
                    Cell cell = headerExcelRow.createCell(i);
                    cell.setCellValue(table.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                Row dataExcelRow = sheet.createRow(rowIndex++);
                for (int j = 0; j < colCountToExport; j++) {
                    Cell cell = dataExcelRow.createCell(j);
                    Object value = table.getValueAt(row, j);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                    cell.setCellStyle(dataStyle);
                }

                for (int i = 0; i < colCountToExport; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }

                JOptionPane.showMessageDialog(parent, "Xuất dòng " + (row + 1) + " thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Lỗi khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}