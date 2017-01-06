package org.colorcoding.tools.btulz.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emBORelation;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.models.data.emModelType;
import org.colorcoding.tools.btulz.models.data.emYesNo;

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
			cellValue = cell.getStringCellValue();
		} else if (cellType == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = cell.getDateCellValue();
			} else {
				cellValue = cell.getNumericCellValue();
			}
		} else if (cellType == CellType.BOOLEAN) {
			cellValue = cell.getBooleanCellValue();
		} else if (cellType == CellType.FORMULA) {
			cellValue = cell.getCellFormula();
		} else if (cellType == CellType.BLANK) {
			cellValue = "";
		}
		return cellValue;
	}

	@Override
	public boolean match(Sheet sheet) {
		return true;
	}

	/**
	 * 已知的数据区域定义
	 * 
	 * @return
	 */
	protected List<ExcelDataArea> getDataAreas() {
		List<ExcelDataArea> areas = new ArrayList<>();
		areas.add(new ExcelDataAreaEmpty(this));
		areas.add(new ExcelDataAreaModel(this));
		areas.add(new ExcelDataAreaBO(this));
		return areas;
	}

	@Override
	public final void parse(IDomain domain, Sheet sheet) throws Exception {
		try {
			if (domain == null || sheet == null) {
				return;
			}
			for (int iRow = 0; iRow < sheet.getLastRowNum(); iRow++) {
				Row row = sheet.getRow(iRow);
				if (row == null) {
					continue;
				}
				for (ExcelDataArea area : this.getDataAreas()) {
					if (area.match(row)) {
						// 此行为当前区域
						iRow += area.parse(row, domain);// 跳过使用的行
						// 匹配成功，则不再匹配
						break;
					}
				}
			}
		} finally {

		}
	}

	/**
	 * 转换数据
	 * 
	 * @param value
	 *            原始类型
	 * @param targetType
	 *            目标类型
	 * @return 转换的数据
	 */
	@SuppressWarnings("unchecked")
	public <T> T convertData(Object value, Class<T> targetType) {
		String tmpValue = String.valueOf(value).trim();
		if (targetType.isEnum()) {
			// 枚举类型
			if (targetType == emYesNo.class) {
				if ("是".equals(tmpValue)) {
					return (T) emYesNo.Yes;
				} else if ("Yes".equals(tmpValue)) {
					return (T) emYesNo.Yes;
				}
				return (T) emYesNo.No;
			} else if (targetType == emBORelation.class) {
				if ("一对一".equals(tmpValue)) {
					return (T) emBORelation.OneToOne;
				}
				return (T) emBORelation.OneToMany;
			} else if (targetType == emModelType.class) {
				if ("主数据".equals(tmpValue)) {
					return (T) emModelType.MasterData;
				} else if ("主数据行".equals(tmpValue)) {
					return (T) emModelType.MasterDataLine;
				} else if ("单据".equals(tmpValue)) {
					return (T) emModelType.Document;
				} else if ("单据行".equals(tmpValue)) {
					return (T) emModelType.DocumentLine;
				} else if ("普通".equals(tmpValue)) {
					return (T) emModelType.Simple;
				} else if ("普通行".equals(tmpValue)) {
					return (T) emModelType.SimpleLine;
				}
				return (T) emModelType.Unspecified;
			} else if (targetType == emDataType.class) {
				if ("字母数字".equals(tmpValue)) {
					return (T) emDataType.Alphanumeric;
				} else if ("长字符串".equals(tmpValue)) {
					return (T) emDataType.Memo;
				} else if ("数字".equals(tmpValue)) {
					return (T) emDataType.Numeric;
				} else if ("日期".equals(tmpValue)) {
					return (T) emDataType.Date;
				} else if ("小数".equals(tmpValue)) {
					return (T) emDataType.Decimal;
				} else if ("字节".equals(tmpValue)) {
					return (T) emDataType.Bytes;
				}
				return (T) emDataType.Unknown;
			} else if (targetType == emDataSubType.class) {
				if ("地址".equals(tmpValue)) {
					return (T) emDataSubType.Address;
				} else if ("电话".equals(tmpValue)) {
					return (T) emDataSubType.Phone;
				} else if ("时间".equals(tmpValue)) {
					return (T) emDataSubType.Time;
				} else if ("率".equals(tmpValue)) {
					return (T) emDataSubType.Rate;
				} else if ("总计".equals(tmpValue)) {
					return (T) emDataSubType.Sum;
				} else if ("价格".equals(tmpValue)) {
					return (T) emDataSubType.Price;
				} else if ("数量".equals(tmpValue)) {
					return (T) emDataSubType.Quantity;
				} else if ("百分比".equals(tmpValue)) {
					return (T) emDataSubType.Percentage;
				} else if ("单位数量".equals(tmpValue)) {
					return (T) emDataSubType.Measurement;
				} else if ("连接".equals(tmpValue)) {
					return (T) emDataSubType.Link;
				} else if ("图片".equals(tmpValue)) {
					return (T) emDataSubType.Image;
				} else if ("邮箱".equals(tmpValue)) {
					return (T) emDataSubType.Email;
				}
				return (T) emDataSubType.Default;
			}
		} else if (targetType == Boolean.class) {
			// 布尔
			if ("是".equals(tmpValue)) {
				return (T) Boolean.TRUE;
			} else if ("Yes".equals(tmpValue)) {
				return (T) Boolean.TRUE;
			}
			return (T) Boolean.FALSE;
		} else if (targetType == Integer.class) {
			// 数值
			if (tmpValue.indexOf(".") > 0) {
				// 带.
				tmpValue = tmpValue.substring(0, tmpValue.indexOf("."));
			}
			return (T) (Object) Integer.parseInt(tmpValue);
		} else if (targetType == String.class) {
			// 字符
			return (T) String.valueOf(tmpValue);
		}
		return (T) value;
	}

	@Override
	public String toString() {
		return String.format("{excel parser %s}", "default");
	}

	/**
	 * excel数据区域
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected abstract class ExcelDataArea {

		public ExcelDataArea(ExcelParser excelParser) {
			this.setExcelParser(excelParser);
		}

		private ExcelParser excelParser;

		protected final ExcelParser getExcelParser() {
			return excelParser;
		}

		private final void setExcelParser(ExcelParser excelParser) {
			this.excelParser = excelParser;
		}

		/**
		 * 转换单元格数据
		 * 
		 * @param cell
		 *            单元格
		 * @param targetType
		 *            目标类型
		 * @return 转换的数据
		 */
		protected final <T> T convertData(Cell cell, Class<T> targetType) {
			return this.getExcelParser().convertData(this.getCellValue(cell), targetType);
		}

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
		 * @param row
		 *            行数据
		 * @param target
		 *            目标对象
		 * @return 额外使用行数，默认0
		 */
		public abstract int parse(Row row, Object... targets);
	}

	/**
	 * 空行，无任何数据
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected class ExcelDataAreaEmpty extends ExcelDataArea {

		public ExcelDataAreaEmpty(ExcelParser excelParser) {
			super(excelParser);
		}

		@Override
		public boolean match(Row row) {
			for (Cell cell : row) {
				if (this.getCellValue(cell) != null) {
					return false;
				}
			}
			Environment.getLogger().debug(String.format("sheet [%s] row [%s] is a empty.",
					row.getSheet().getSheetName(), row.getRowNum() + 1));
			return true;
		}

		@Override
		public int parse(Row row, Object... targets) {
			return 0;
		}

	}

	/**
	 * excel数据区域-模型
	 * 
	 * @author Niuren.Zhu
	 *
	 */
	protected class ExcelDataAreaModel extends ExcelDataArea {

		public static final String TABLE_NAME = "表名";

		public ExcelDataAreaModel(ExcelParser excelParser) {
			super(excelParser);
		}

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
			Environment.getLogger().debug(String.format("sheet [%s] row [%s] is a model.",
					row.getSheet().getSheetName(), row.getRowNum() + 1));
			return true;
		}

		@Override
		public int parse(Row row, Object... targets) {
			if (targets.length > 0 && targets[0] instanceof IDomain) {
				return this.parse(row, (IDomain) targets[0]);
			} else {
				throw new RuntimeException(String.format("[%s] requires.", IDomain.class.getName()));
			}
		}

		/**
		 * 已知的数据区域定义
		 * 
		 * @return
		 */
		protected List<ExcelDataArea> getDataAreas() {
			List<ExcelDataArea> areas = new ArrayList<>();
			areas.add(new ExcelDataAreaEmpty(this.getExcelParser()));
			areas.add(new ExcelDataAreaProperty(this.getExcelParser()));
			return areas;
		}

		public final static int COLUMN_INDEX_TABLE_NAME = 1;
		public final static int COLUMN_INDEX_DESCRIPTION = 4;
		public final static int COLUMN_INDEX_NAME = 6;
		public final static int COLUMN_INDEX_MODEL_TYPE = 9;
		public final static int COLUMN_INDEX_IS_ENTITY = 11;

		public int parse(Row row, IDomain domain) {
			Sheet sheet = row.getSheet();
			ExcelDataArea area = null;
			int useCount = 0;
			IModel model = domain.getModels().create();
			model.setMapped(this.convertData(row.getCell(COLUMN_INDEX_TABLE_NAME), String.class));
			model.setDescription(this.convertData(row.getCell(COLUMN_INDEX_DESCRIPTION), String.class));
			model.setName(this.convertData(row.getCell(COLUMN_INDEX_NAME), String.class));
			model.setModelType(this.convertData(row.getCell(COLUMN_INDEX_MODEL_TYPE), emModelType.class));
			model.setEntity(this.convertData(row.getCell(COLUMN_INDEX_IS_ENTITY), Boolean.class));
			useCount++;// 自身行
			useCount++;// propety标题1
			useCount++;// propety标题2
			// 开始属性行处理
			for (int iRow = row.getRowNum() + useCount; iRow < sheet.getLastRowNum(); iRow++) {
				useCount++;
				row = sheet.getRow(iRow);
				if (row == null) {
					// 遇到空行，认为是中断行
					break;
				}
				area = null;
				for (ExcelDataArea tmpArea : this.getDataAreas()) {
					if (tmpArea.match(row)) {
						area = tmpArea;
						break;
					}
				}
				if (area instanceof ExcelDataAreaEmpty) {
					// 遇到无数据行，认为是中断行
					break;
				} else if (area instanceof ExcelDataAreaProperty) {
					// 属性行
					useCount += area.parse(row, model);
				} else {
					Environment.getLogger().warn(String.format("sheet [%s] row [%s] is unkown.",
							row.getSheet().getSheetName(), row.getRowNum() + 1));
					break;
				}
			}
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

		public ExcelDataAreaProperty(ExcelParser excelParser) {
			super(excelParser);
		}

		/**
		 * 前7列不为空
		 */
		@Override
		public boolean match(Row row) {
			int index = 0;
			for (Cell cell : row) {
				if (index < 7) {
					Object value = this.getCellValue(cell);
					if (value == null || "".equals(value)) {
						return false;
					}
				}
				index++;
			}
			Environment.getLogger().debug(String.format("sheet [%s] row [%s] is a property.",
					row.getSheet().getSheetName(), row.getRowNum() + 1));
			return true;
		}

		@Override
		public int parse(Row row, Object... targets) {
			if (targets.length > 0 && targets[0] instanceof IModel) {
				return this.parse(row, (IModel) targets[0]);
			} else {
				throw new RuntimeException(String.format("[%s] requires.", IModel.class.getName()));
			}
		}

		/**
		 * 已知的数据区域定义
		 * 
		 * @return
		 */
		protected List<ExcelDataArea> getDataAreas() {
			List<ExcelDataArea> areas = new ArrayList<>();
			areas.add(new ExcelDataAreaEmpty(this.getExcelParser()));
			areas.add(new ExcelDataAreaProperty(this.getExcelParser()));
			return areas;
		}

		public final static int COLUMN_INDEX_FIELD_NAME = 0;
		public final static int COLUMN_INDEX_DESCRIPTION = 1;
		public final static int COLUMN_INDEX_NAME = 2;
		public final static int COLUMN_INDEX_DATA_TYPE = 4;
		public final static int COLUMN_INDEX_DATA_SUB_TYPE = 5;
		public final static int COLUMN_INDEX_EDIT_SIZE = 6;

		public int parse(Row row, IModel model) {
			int useCount = 0;
			IProperty property = model.getProperties().create();
			property.setMapped(this.convertData(row.getCell(COLUMN_INDEX_FIELD_NAME), String.class));
			property.setDescription(this.convertData(row.getCell(COLUMN_INDEX_DESCRIPTION), String.class));
			property.setName(this.convertData(row.getCell(COLUMN_INDEX_NAME), String.class));
			property.setDataType(this.convertData(row.getCell(COLUMN_INDEX_DATA_TYPE), emDataType.class));
			property.setDataSubType(this.convertData(row.getCell(COLUMN_INDEX_DATA_SUB_TYPE), emDataSubType.class));
			property.setEditSize(this.convertData(row.getCell(COLUMN_INDEX_EDIT_SIZE), Integer.class));

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

		public ExcelDataAreaBO(ExcelParser excelParser) {
			super(excelParser);
		}

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
			Environment.getLogger().debug(
					String.format("sheet [%s] row [%s] is a bo.", row.getSheet().getSheetName(), row.getRowNum() + 1));
			return true;
		}

		@Override
		public int parse(Row row, Object... targets) {
			if (targets.length > 0 && targets[0] instanceof IDomain) {
				return this.parse(row, (IDomain) targets[0]);
			} else {
				throw new RuntimeException(String.format("[%s] requires.", IDomain.class.getName()));
			}
		}

		public final static int COLUMN_INDEX_SHORT_NAME = 1;
		public final static int COLUMN_INDEX_TABLE = 1;

		public int parse(Row row, IDomain domain) {
			int useCount = 0;
			IBusinessObject bo = domain.getBusinessObjects().create();
			bo.setShortName(this.convertData(row.getCell(COLUMN_INDEX_SHORT_NAME), String.class));
			Sheet sheet = row.getSheet();
			// 处理主表
			row = sheet.getRow(row.getRowNum() + 1);
			String master = this.convertData(row.getCell(COLUMN_INDEX_TABLE), String.class);
			IModel model = domain.getModels().firstOrDefault(c -> c.getMapped().equals(master));
			if (model == null) {
				throw new RuntimeException(String.format("not found model [%s].", master));
			}
			bo.setMappedModel(model);
			useCount++;
			// 处理子表
			for (int i = row.getRowNum() + 1; i < sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				String child = this.convertData(row.getCell(COLUMN_INDEX_TABLE), String.class);
				if (child == null || child.isEmpty()) {
					break;
				}
				model = domain.getModels().firstOrDefault(c -> c.getMapped().equals(child));
				if (model == null) {
					throw new RuntimeException(String.format("not found model [%s].", child));
				}
				IBusinessObjectItem boItem = bo.getRelatedBOs().create();
				boItem.setMappedModel(model);
				useCount++;
			}
			return useCount;
		}
	}
}
