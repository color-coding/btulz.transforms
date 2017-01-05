package org.colorcoding.tools.btulz.test.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.colorcoding.tools.btulz.test.Environment;
import org.colorcoding.tools.btulz.transformers.ExcelTransformer;
import org.colorcoding.tools.btulz.transformers.MultiTransformException;
import org.colorcoding.tools.btulz.transformers.TransformException;

import junit.framework.TestCase;

public class testExcelTransformer extends TestCase {

	// jdbc.odbc的方法已在java7中取消

	public void testLoadXls() throws Exception {
		InputStream stream = null;
		Workbook wb = null;
		String fileName = Environment.getWorkingFolder() + Environment.getExcelModelsFileOld();
		try {
			stream = new FileInputStream(fileName);

			if (fileName.endsWith(".xls")) {
				wb = new HSSFWorkbook(stream);
			} else {
				throw new Exception("读取的不是excel文件");
			}
			this.loadWorkbook(wb);
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

	public void testLoadXlsx() throws Exception {
		InputStream stream = null;
		Workbook wb = null;
		String fileName = (new File(Environment.getWorkingFolder())).getParent() + Environment.getExcelModelsFile();
		try {
			stream = new FileInputStream(fileName);
			if (fileName.endsWith(".xlsx")) {
				wb = new XSSFWorkbook(stream);
			} else {
				throw new Exception("读取的不是excel文件");
			}
			this.loadWorkbook(wb);
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

	public void loadWorkbook(Workbook workbook) {
		String cellValue = null;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			System.out.println("sheet: " + workbook.getSheetName(i));
			// 有效值
			List<? extends DataValidation> dataValidations = sheet.getDataValidations();
			if (dataValidations != null && dataValidations.size() > 0) {
				System.out.println("data validations: ");
				for (DataValidation validation : dataValidations) {
					System.out.println(validation.toString());
				}
			}
			// 表格行
			for (Row row : sheet) {
				// 行单元格
				for (Cell cell : row) {
					cellValue = "";
					CellType cellType = CellType.forInt(cell.getCellType());
					if (cellType == CellType.STRING) {
						cellValue = cell.getRichStringCellValue().getString();
					} else if (cellType == CellType.NUMERIC) {
						if (DateUtil.isCellDateFormatted(cell)) {
							cellValue = String.valueOf(cell.getDateCellValue());
						} else {
							cellValue = String.valueOf(cell.getNumericCellValue());
						}
					} else if (cellType == CellType.BOOLEAN) {
						cellValue = String.valueOf(cell.getBooleanCellValue());
					} else if (cellType == CellType.FORMULA) {
						cellValue = String.valueOf(cell.getCellFormula());
					} else if (cellType == CellType.BLANK) {
						cellValue = String.valueOf(" ");
					}
					System.out.print(cellValue);
					System.out.print("  ");
				}
				System.out.println("");
			}
		}
	}

	public void testTransformer() throws TransformException, MultiTransformException {
		ExcelTransformer excelTransformer = new ExcelTransformer();
		excelTransformer.setIgnoreSheet(false);// 不忽悠注释表格
		excelTransformer.load((new File(Environment.getWorkingFolder())).getParent() + Environment.getExcelModelsFile(),
				true);
	}
}
