package org.colorcoding.tools.btulz.templates;

import java.io.BufferedWriter;

/**
 * 模板数据
 * 
 * @author Niuren.Zhu
 *
 */
public interface ITemplateData {
	/**
	 * 输出数据
	 * 
	 * @param writer
	 *            输出
	 * @param pars
	 *            参数
	 * @throws Exception
	 */
	void export(BufferedWriter writer, Parameters pars) throws Exception;
}
