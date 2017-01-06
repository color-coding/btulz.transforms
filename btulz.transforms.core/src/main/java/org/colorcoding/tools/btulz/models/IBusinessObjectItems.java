package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.util.List;

public interface IBusinessObjectItems extends List<IBusinessObjectItem> {
	/**
	 * 获取第一个或默认对象
	 * 
	 * @return
	 */
	IBusinessObjectItem firstOrDefault();

	/**
	 * 创建并添加新的实例
	 * 
	 * @return
	 */
	IBusinessObjectItem create();
}
