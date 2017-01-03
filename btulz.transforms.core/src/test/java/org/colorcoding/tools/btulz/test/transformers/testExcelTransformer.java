package org.colorcoding.tools.btulz.test.transformers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.colorcoding.tools.btulz.test.Environment;

import junit.framework.TestCase;

public class testExcelTransformer extends TestCase {

	// jdbc.odbc的方法已在java7中取消

	public void testLoadXls() throws Exception {
		InputStream stream = null;
		Workbook wb = null;
		String fileName = Environment.getWorkingFolder() + Environment.getExcelModelsFile();
		try {
			stream = new FileInputStream(fileName);

			if (fileName.endsWith(".xls")) {
				wb = new HSSFWorkbook(stream);
			} else {
				throw new Exception("读取的不是excel文件");
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				System.out.println("sheet: " + wb.getSheetName(i));
				Sheet sheet = wb.getSheetAt(i);
				for (Row row : sheet) {
					for (Cell cell : row) {
						CellType cellType = CellType.forInt(cell.getCellType());
						if (cellType == CellType.STRING) {
							System.out.println(cell.getRichStringCellValue().getString());
						} else if (cellType == CellType.NUMERIC) {
							if (DateUtil.isCellDateFormatted(cell)) {
								System.out.println(cell.getDateCellValue());
							} else {
								System.out.println(cell.getNumericCellValue());
							}
						} else if (cellType == CellType.BOOLEAN) {
							System.out.println(cell.getBooleanCellValue());
						} else if (cellType == CellType.FORMULA) {
							System.out.println(cell.getCellFormula());
						} else if (cellType == CellType.BLANK) {
							System.out.println();
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (wb != null) {
				wb.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}
}
