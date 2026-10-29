
package com.airline.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public final class ExcelUtil {

    private ExcelUtil() {
        // Prevent object creation.
    }

    public static Object[][] getTestData(
            String filePath, String sheetName) {

        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException(
                    "Excel file path cannot be blank");
        }

        if (sheetName == null || sheetName.isBlank()) {
            throw new IllegalArgumentException(
                    "Excel sheet name cannot be blank");
        }

        Path path = Paths.get(filePath);

        try (InputStream input = Files.newInputStream(path);
             Workbook workbook = WorkbookFactory.create(input)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);

            if (headerRow == null || headerRow.getLastCellNum() < 0) {
                throw new IllegalArgumentException(
                        "Excel sheet has no header row: " + sheetName);
            }

            int columnCount = headerRow.getLastCellNum();
            int lastRowNumber = sheet.getLastRowNum();
            int dataRowCount = 0;

            for (int i = 1; i <= lastRowNumber; i++) {
                Row row = sheet.getRow(i);

                if (row != null && !isRowEmpty(row, columnCount)) {
                    dataRowCount++;
                }
            }

            Object[][] data = new Object[dataRowCount][columnCount];
            DataFormatter formatter = new DataFormatter();
            int dataIndex = 0;

            for (int i = 1; i <= lastRowNumber; i++) {
                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row, columnCount)) {
                    continue;
                }

                for (int j = 0; j < columnCount; j++) {
                    data[dataIndex][j] = formatter.formatCellValue(
                            row.getCell(j));
                }

                dataIndex++;
            }

            return data;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read Excel file: " + path, e);
        }
    }

    private static boolean isRowEmpty(Row row, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            if (row.getCell(i) != null
                    && !row.getCell(i).toString().isBlank()) {
                return false;
            }
        }

        return true;
    }
}
