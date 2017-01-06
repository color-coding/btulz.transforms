package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.util.List;

/**
 * 模型集合
 * 
 * @author Niuren.Zhu
 *
 */
public interface IModels extends List<IModel> {
	/**
	 * 获取第一个或默认对象
	 * 
	 * @return
	 */
	IModel firstOrDefault();

	/**
	 * 创建并添加模型
	 * 
	 * @return
	 */
	IModel create();
}
