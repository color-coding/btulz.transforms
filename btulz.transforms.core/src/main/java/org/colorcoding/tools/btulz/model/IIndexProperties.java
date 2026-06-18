package org.colorcoding.tools.btulz.model;

import org.colorcoding.tools.btulz.util.List;

/**
 * 索引属性集合
 * 
 * @author Niuren.Zhu
 *
 */
public interface IIndexProperties extends List<IIndexProperty> {
	/**
	 * 创建并添加索引属性
	 * 
	 * @return
	 */
	IIndexProperty create();
}