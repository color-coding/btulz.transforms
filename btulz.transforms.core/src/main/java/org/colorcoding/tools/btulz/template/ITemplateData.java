package org.colorcoding.tools.btulz.template;

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
	 * @param pars   参数
	 * @param writer 输出
	 * @throws Exception
	 */
	void export(Parameters pars, BufferedWriter writer) throws Exception;
}
