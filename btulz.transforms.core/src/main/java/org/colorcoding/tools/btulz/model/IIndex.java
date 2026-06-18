package org.colorcoding.tools.btulz.model;

import org.colorcoding.tools.btulz.model.data.emIndexType;

/**
 * 索引
 * 
 * @author Niuren.Zhu
 *
 */
public interface IIndex {
	/**
	 * 获取-名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 设置-名称
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 获取-短名称
	 * 
	 * @return
	 */
	String getShortName();

	/**
	 * 设置-短名称
	 * 
	 * @param name
	 */
	void setShortName(String name);

	/**
	 * 获取-描述
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 设置-描述
	 * 
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * 获取-索引类型
	 * 
	 * @return
	 */
	emIndexType getIndexType();

	/**
	 * 设置-索引类型
	 * 
	 * @param indexType
	 */
	void setIndexType(emIndexType indexType);

	/**
	 * 获取-索引属性集合
	 * 
	 * @return
	 */
	IIndexProperties getIndexProperties();

	/**
	 * 深度克隆
	 * 
	 * @return
	 */
	IIndex clone();
}