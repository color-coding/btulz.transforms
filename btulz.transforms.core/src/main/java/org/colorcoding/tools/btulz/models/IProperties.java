package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.util.List;

/**
 * 属性集合
 * 
 * @author Niuren.Zhu
 *
 */
public interface IProperties extends List<IProperty> {
	/**
	 * 创建并添加属性
	 * 
	 * @return
	 */
	IProperty create();

}
