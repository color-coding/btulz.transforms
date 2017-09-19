package org.colorcoding.tools.btulz.shell.commands;

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
	 * @return
	 */
	ValidValue[] get();
}
