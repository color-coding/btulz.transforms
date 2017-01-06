package org.colorcoding.tools.btulz.transformers;

import org.apache.poi.ss.usermodel.Sheet;
import org.colorcoding.tools.btulz.models.IDomain;

/**
 * excel解释器
 * 
 * @author Niuren.Zhu
 *
 */
public interface IExcelParser {

	/**
	 * 判断此解释器是否可用
	 * 
	 * @param sheet
	 *            待分析的表格
	 * @return true，可用；false，不可用
	 */
	boolean match(Sheet sheet);

	/**
	 * 分析
	 * 
	 * @param domain
	 *            目标模型
	 * @param sheet
	 *            表格
	 */
	void parse(IDomain domain, Sheet sheet) throws Exception;

	@Override
	String toString();
}
