package org.colorcoding.tools.btulz.shell.commands;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 项目的有效值集合
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ValidValues", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class ValidValues extends ArrayList<ValidValue> {

	private static final long serialVersionUID = 6366175491071067769L;

	/**
	 * 取值类型
	 */
	@XmlAttribute(name = "ClassName")
	private String className;

	public final String getClassName() {
		return className;
	}

	public final void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 初始化下
	 */
	public void get() {
		if (this.size() > 0) {
			// 存在值，则表示已经初始化过
			return;
		}
		if (this.getClassName() == null || this.getClassName().isEmpty()) {
			return;
		}
		try {
			Class<?> type = Class.forName(this.getClassName());
			if (type == null) {
				return;
			}
			if (type.isEnum()) {
				// 枚举类型
				for (Object item : type.getEnumConstants()) {
					this.add(new ValidValue(item.toString()));
				}
			} else if (type.equals(Boolean.class)) {
				// 布尔值
				this.add(new ValidValue(Boolean.TRUE.toString()));
				this.add(new ValidValue(Boolean.FALSE.toString()));
			}
		} catch (Exception e) {

		}
	}
}
