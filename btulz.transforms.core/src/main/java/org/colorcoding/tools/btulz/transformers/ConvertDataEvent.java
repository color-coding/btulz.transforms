package org.colorcoding.tools.btulz.transformers;

import java.util.EventObject;

/**
 * 数据转换事件
 * 
 * @author Niuren.Zhu
 *
 */
public class ConvertDataEvent extends EventObject {

	private static final long serialVersionUID = -1583938831494593622L;

	public ConvertDataEvent(Object source) {
		super(source);
	}

	private Object data;

	/**
	 * 待转换的数据
	 * 
	 * @return
	 */
	public final Object getData() {
		return data;
	}

	public final void setData(Object data) {
		this.data = data;
	}

	private Class<?> targetType;

	/**
	 * 转换的目标类型
	 * 
	 * @return
	 */
	public final Class<?> getTargetType() {
		return targetType;
	}

	public final void setTargetType(Class<?> targetType) {
		this.targetType = targetType;
	}
}
