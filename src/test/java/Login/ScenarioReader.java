package Login;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ScenarioReader {

    public static class EnrollmentScenario {
        public String card1, card2, card3;
        public String status;
        public int rowIndex;
        public String generatedEmail;

        public EnrollmentScenario(String c1, String c2, String c3, String status, int rowIndex) {
            this.card1 = c1;
            this.card2 = c2;
            this.card3 = c3;
            this.status = status;
            this.rowIndex = rowIndex;
        }
    }

    public static List<EnrollmentScenario> readScenarios(String excelPath) {
        List<EnrollmentScenario> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String c1 = getCellValue(row.getCell(0));
                String c2 = getCellValue(row.getCell(1));
                String c3 = getCellValue(row.getCell(2));
                String status = getCellValue(row.getCell(3));

                EnrollmentScenario scenario = new EnrollmentScenario(c1, c2, c3, status, i);
                list.add(scenario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue()).trim();
        }
        return "";
    }

    public static void updateScenarioStatus(String filePath, int rowIndex, String newStatus, String email) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowIndex);

            int statusCol = 3; // "Status" column index (D)
            int emailCol = 4;  // New column right after "Status" (E)

            // Update status
            Cell statusCell = row.getCell(statusCol);
            if (statusCell == null) statusCell = row.createCell(statusCol);
            statusCell.setCellValue(newStatus);

            // Update email
            Cell emailCell = row.getCell(emailCol);
            if (emailCell == null) emailCell = row.createCell(emailCol);
            emailCell.setCellValue(email);

            // Save changes
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
