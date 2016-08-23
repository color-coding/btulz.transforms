package org.colorcoding.tools.btulz.models;

import java.util.List;

public interface IBusinessObjectItems extends List<IBusinessObjectItem> {
	/**
	 * 创建并添加新的实例
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	IBusinessObjectItem create() throws ClassNotFoundException;
}
