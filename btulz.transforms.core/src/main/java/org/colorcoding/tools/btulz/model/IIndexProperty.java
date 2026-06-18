package org.colorcoding.tools.btulz.model;

/**
 * 索引属性
 * 
 * @author Niuren.Zhu
 *
 */
public interface IIndexProperty {
	/**
	 * 获取-属性名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 设置-属性名称
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 获取-排序方向
	 * 
	 * @return ASC / DESC
	 */
	String getDirection();

	/**
	 * 设置-排序方向
	 * 
	 * @param direction
	 */
	void setDirection(String direction);
}