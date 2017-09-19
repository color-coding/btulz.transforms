package org.colorcoding.tools.btulz.models;

import java.util.List;

public interface IBusinessObjects extends List<IBusinessObject> {
	/**
	 * 获取第一个或默认对象
	 * 
	 * @return
	 */
	IBusinessObject firstOrDefault();

	/**
	 * 创建并添加新的实例
	 * 
	 * @return
	 */
	IBusinessObject create();
}
