package org.colorcoding.tools.btulz.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;

/**
 * excel解释器
 * 
 * @author Niuren.Zhu
 *
 */
public class ExcelParser implements IExcelParser {

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static Object getCellValue(Cell cell) {
		Object cellValue = null;
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
		return cellValue;
	}

	private List<ConvertDataListener> listeners;

	@Override
	public final void addListener(ConvertDataListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<>(1);
		}
		this.listeners.add(listener);
	}

	@Override
	public boolean match(Sheet sheet) {
		return true;
	}

	@Override
	public final void parse(IDomain domain, Sheet sheet) throws Exception {
		try {
			if (domain == null || sheet == null) {
				return;
			}
		} finally {
			if (this.listeners != null) {
				this.listeners = null;
			}
		}
	}

	/**
	 * excel数据区域
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected abstract class ExcelDataArea {

		/**
		 * 判断是否匹配
		 * 
		 * @param row
		 *            行
		 * @return
		 */
		public abstract boolean match(Row row);

		/**
		 * 获取单元格的值
		 * 
		 * @param cell
		 * @return
		 */
		public Object getCellValue(Cell cell) {
			return ExcelParser.getCellValue(cell);
		}

		/**
		 * 解释数据
		 * 
		 * @param target
		 * @param row
		 * @return 已使用行数
		 */
		public abstract int parse(Object target, Row row);
	}

	/**
	 * excel数据区域-模型
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected class ExcelDataAreaModel extends ExcelDataArea {

		public static final String TABLE_NAME = "表名";

		/**
		 * 第一列是值为“表名”
		 */
		@Override
		public boolean match(Row row) {
			int index = 0;
			for (Cell cell : row) {
				if (index == 0) {
					if (!TABLE_NAME.equals(this.getCellValue(cell))) {
						return false;
					}
				}
				index++;
			}
			return true;
		}

		@Override
		public int parse(Object target, Row row) {
			if (target instanceof IDomain) {
				return this.parse((IDomain) target, row);
			} else {
				throw new RuntimeException(String.format("[%s] requires.", IDomain.class.getName()));
			}
		}

		public int parse(IDomain domain, Row row) {
			int useCount = 0;

			return useCount;
		}
	}

	/**
	 * excel数据区域-模型
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected class ExcelDataAreaProperty extends ExcelDataArea {

		/**
		 * 前7列不为空
		 */
		@Override
		public boolean match(Row row) {
			int index = 0;
			for (Cell cell : row) {
				if (index < 7) {
					if (this.getCellValue(cell) == null) {
						return false;
					}
				}
				index++;
			}
			return true;
		}

		@Override
		public int parse(Object target, Row row) {
			if (target instanceof IModel) {
				return this.parse((IModel) target, row);
			} else {
				throw new RuntimeException(String.format("[%s] requires.", IModel.class.getName()));
			}
		}

		public int parse(IModel model, Row row) {
			int useCount = 0;

			return useCount;
		}
	}

	/**
	 * excel数据区域-对象
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected class ExcelDataAreaBO extends ExcelDataArea {

		public static final String BO_NAME = "对象名";

		/**
		 * 第一列是值为“对象名”
		 */
		@Override
		public boolean match(Row row) {
			int index = 0;
			for (Cell cell : row) {
				if (index == 0) {
					if (!BO_NAME.equals(this.getCellValue(cell))) {
						return false;
					}
				}
				index++;
			}
			return true;
		}

		@Override
		public int parse(Object target, Row row) {
			if (target instanceof IDomain) {
				return this.parse((IDomain) target, row);
			} else {
				throw new RuntimeException(String.format("[%s] requires.", IDomain.class.getName()));
			}
		}

		public int parse(IDomain domain, Row row) {
			int useCount = 0;

			return useCount;
		}
	}
}
