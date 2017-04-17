package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;

/**
 * 基本属性
 * 
 * @author Niuren.Zhu
 *
 */
public interface IProperty {
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
	 * 获取-声明类型
	 * 
	 * @return
	 */
	String getDeclaredType();

	/**
	 * 设置-声明类型
	 * 
	 * @param declaredType
	 */
	void setDeclaredType(String declaredType);

	/**
	 * 是否主键
	 * 
	 * @return
	 */
	boolean isPrimaryKey();

	/**
	 * 设置-主键
	 * 
	 * @param dataType
	 */
	void setPrimaryKey(boolean value);

	/**
	 * 是否唯一
	 * 
	 * @return
	 */
	boolean isUniqueKey();

	/**
	 * 设置-唯一键
	 * 
	 * @param dataType
	 */
	void setUniqueKey(boolean value);

	/**
	 * 获取-数据类型
	 * 
	 * @return
	 */
	emDataType getDataType();

	/**
	 * 设置-数据类型
	 * 
	 * @param dataType
	 */
	void setDataType(emDataType dataType);

	/**
	 * 获取-数据子类型
	 * 
	 * @return
	 */
	emDataSubType getDataSubType();

	/**
	 * 设置-数据子类型
	 * 
	 * @param dataSubType
	 */
	void setDataSubType(emDataSubType dataSubType);

	/**
	 * 获取-数据长度
	 * 
	 * @return
	 */
	int getEditSize();

	/**
	 * 设置-数据长度
	 * 
	 * @param dataType
	 */
	void setEditSize(int editSize);

	/**
	 * 获取-绑定到
	 * 
	 * @return
	 */
	String getMapped();

	/**
	 * 设置-绑定到
	 * 
	 * @param mapped
	 */
	void setMapped(String mapped);

	/**
	 * 获取-连接的
	 * 
	 * @return
	 */
	String getLinked();

	/**
	 * 设置-连接的
	 * 
	 * @param linked
	 */
	void setLinked(String linked);

	/**
	 * 深度克隆
	 * 
	 * @return
	 */
	IProperty clone();
}
