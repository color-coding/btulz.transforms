package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.Domain;
import org.colorcoding.tools.btulz.models.IDomain;

/**
 * excel文件和领域模型的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class ExcelTransformer extends FileTransformer {
	/**
	 * 忽略表格的名称前缀
	 */
	public final static String IGNORE_SHEET_NAME_PREFIX = "!--";

	public ExcelTransformer() {
		this.setIgnoreSheet(true);
	}

	private boolean ignoreSheet;

	/**
	 * 是否忽略注释的表格（名称前缀为!--）
	 * 
	 * @return
	 */

	public final boolean isIgnoreSheet() {
		return ignoreSheet;
	}

	public final void setIgnoreSheet(boolean ignoreSheet) {
		this.ignoreSheet = ignoreSheet;
	}

	/**
	 * 当前领域模型输出为xml格式
	 */
	@Override
	public final void transform() throws Exception {
		this.transform(Environment.getWorkingFolder() + File.separator + "out");
	}

	/**
	 * 当前领域模型输出为xml格式
	 * 
	 * @param outFolder
	 *            输出目录
	 * @throws Exception
	 */
	public void transform(String outFolder) throws Exception {
		XmlTransformer xmlTransformer = null;
		for (IDomain domain : this.getWorkingDomains()) {
			xmlTransformer = new XmlTransformer();
			xmlTransformer.load(domain);
			xmlTransformer.setInterruptOnError(true);
			xmlTransformer.setKeepResults(false);
			xmlTransformer.save(outFolder);
		}
	}

	@Override
	protected final IDomain[] load(File file) throws Exception {
		InputStream stream = null;
		Workbook workbook = null;
		try {
			if (file == null || !file.isFile() || !file.exists()) {
				throw new Exception("invaild file.");
			}
			stream = new FileInputStream(file);
			if (file.getName().toLowerCase().endsWith(".xls")) {
				workbook = new HSSFWorkbook(stream);
			} else if (file.getName().toLowerCase().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(stream);
			} else {
				throw new Exception("file was not be supported.");
			}
			IDomain domain = new Domain();
			this.parse(domain, file.getName());
			this.parse(domain, workbook);
			return new IDomain[] { domain };
		} finally {
			if (workbook != null) {
				workbook.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * 解析文件名称
	 * 
	 * @param domain
	 *            目标模型
	 * @param fileName
	 *            文件名称
	 */
	protected void parse(IDomain domain, String fileName) {
		try {
			// ......_MarketingManagement_v...xls
			// ..............^..............^
			// .............名称..........版本号
			String tmp = fileName.substring(0, fileName.lastIndexOf("."));// 去扩展名
			tmp = tmp.substring(0, tmp.lastIndexOf("_"));// 去除版本
			tmp = tmp.substring(tmp.lastIndexOf("_") + 1);// 去除前缀
			String name = tmp;
			String shortName = "";
			// 获取缩写，即名称的大写字母
			for (char item : name.toCharArray()) {
				if (!Character.isLowerCase(item)) {
					shortName = shortName + item;
				}
			}
			if (name != null && !name.isEmpty()) {
				domain.setName(name);
			}
			if (shortName != null && !shortName.isEmpty()) {
				domain.setShortName(shortName);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 分析工作簿
	 * 
	 * @param domain
	 *            填充的领域模型
	 * 
	 * @param workbook
	 *            待分析的工作簿
	 * @throws TransformException
	 */
	private void parse(IDomain domain, Workbook workbook) throws TransformException {
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getSheetName().startsWith(IGNORE_SHEET_NAME_PREFIX) && this.isIgnoreSheet()) {
				Environment.getLogger().warn(String.format("[%s] is skipped.", sheet.getSheetName()));
				continue;
			}
			this.parse(domain, sheet);
		}
	}

	/**
	 * 分析表格
	 * 
	 * @param domain
	 *            填充的领域模型
	 * @param sheet
	 *            待分析的表格
	 * @throws TransformException
	 */
	private void parse(IDomain domain, Sheet sheet) throws TransformException {
		IExcelParser parser = this.createParser(sheet);
		if (parser == null) {
			Environment.getLogger().warn(String.format("[%s] no parser available.", sheet.getSheetName()));
			return;
		} else {
			Environment.getLogger().warn(String.format("[%s] using [%s].", sheet.getSheetName(), parser.toString()));
		}
		try {
			// 添加数据解释监听
			ExcelTransformer that = this;
			parser.addListener(new ConvertDataListener() {
				@Override
				public Object convertData(ConvertDataEvent event) {
					return that.convertData(event);
				}
			});
			parser.parse(domain, sheet);
		} catch (Exception e) {
			throw new TransformException(e);
		}
	}

	/**
	 * 转换数据
	 * 
	 * @param event
	 * @return
	 */
	protected Object convertData(ConvertDataEvent event) {

		// 原始返回
		return event.getData();
	}

	/**
	 * 创建解释器
	 * 
	 * @param sheet
	 *            待解释的表格
	 * @return null表示无可用解释器
	 */
	protected IExcelParser createParser(Sheet sheet) {
		IExcelParser parser = new ExcelParser();
		if (parser.match(sheet)) {
			return parser;
		}
		return null;
	}

	@Override
	protected void save(File outFolder, IDomain domain) throws Exception {
		// TODO Auto-generated method stub

	}

}
