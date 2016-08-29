package org.colorcoding.tools.btulz.templates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 参数
 * 
 * @author Niuren.Zhu
 *
 */
public class Parameter {
	public Parameter() {

	}

	public Parameter(String name, Object value) {
		this.setName(name);
		this.setValue(value);
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	protected Object getValue(Object value, String path)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (path != null && !path.equals("") && value != null) {
			String curPath = path;
			String nextPath = null;
			int indexPath = path.indexOf(".");
			if (indexPath > 0) {
				curPath = path.substring(0, indexPath);
				nextPath = path.substring(indexPath + 1, path.length());
			}
			// 获取当前路径参数
			Object tmpValue = null;
			Class<?> type = value.getClass();
			Object[] pathArgs = curPath.replace("(", "").replace(")", "").split(",");
			for (Method method : type.getDeclaredMethods()) {
				if (method.getName().equals(curPath) && (method.getParameterCount() == pathArgs.length)
						|| pathArgs == null) {
					return method.invoke(value, pathArgs);
				}
			}
			// 处理下级路径
			if (nextPath != null && !nextPath.equals("")) {
				return this.getValue(tmpValue, nextPath);
			}
			// 路径值未找到
			return null;
		}
		return value;
	}

	public Object getValue(String path) throws Exception {
		// like,getValue().toString(xml)
		return this.getValue(this.getValue(), path);
	}
}
