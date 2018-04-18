package org.colorcoding.tools.btulz.shell.command;

/**
 * 可选值获取者
 * 
 * @author Niuren.Zhu
 *
 */
public interface ValidValuesGetter {

	/**
	 * 获取可选值
	 * 
	 * @param definitions
	 *            参数
	 * @return
	 */
	ValidValue[] get(String definitions);
}
