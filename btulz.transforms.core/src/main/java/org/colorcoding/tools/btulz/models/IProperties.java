package org.colorcoding.tools.btulz.models;

import java.util.List;

import org.colorcoding.tools.btulz.models.data.emPropertyType;

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
	 * @throws ClassNotFoundException
	 */
	IProperty create(emPropertyType type) throws ClassNotFoundException;

	/**
	 * 创建并添加属性
	 * 
	 * @param type
	 *            属性的类型，必须为IProperty的实现类型
	 * @return 属性的实例
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	<P extends IProperty> P create(Class<P> type) throws InstantiationException, IllegalAccessException;
}
