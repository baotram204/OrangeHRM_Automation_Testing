package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    private static Workbook workbook;
    private static Sheet sheet;

    /**
     * Load Excel file
     * @param filePath
     * @param sheetName
     * @throws IOException
     */
    public static void loadExcel(String filePath, String sheetName) throws IOException {

        FileInputStream file = new FileInputStream(filePath);

        workbook = new XSSFWorkbook(file);
        sheet = workbook.getSheet(sheetName);

    }


    /**
     * Get cell data based on row and column number
     * @param rowNum
     * @param colNum
     * @return cell data
     */
    public static String getCellData(int rowNum, int colNum) {
        if (sheet.getRow(rowNum) == null) {
            return "";
        }
        
        Cell cell = sheet.getRow(rowNum).getCell(colNum);
        
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }

    /**
     * Get total rows
     * @return total row count
     */
    public static int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    public static int getColCount() {
        return sheet.getRow(0).getPhysicalNumberOfCells();
    }

    /**
     * Close file excel
     */
    public static void closeExcel() throws IOException {
        if (workbook != null) {
            workbook.close();
        }

    }

}































