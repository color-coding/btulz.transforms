package org.colorcoding.tools.btulz.models;

import java.util.List;

public interface IBusinessObjects extends List<IBusinessObject> {
	/**
	 * 创建并添加新的实例
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	IBusinessObject create() throws ClassNotFoundException;
}
