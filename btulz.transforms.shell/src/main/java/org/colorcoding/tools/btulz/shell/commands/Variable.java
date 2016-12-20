package org.colorcoding.tools.btulz.shell.commands;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 变量
 * 
 * @author Niuren.Zhu
 *
 */
public class Variable {
	/**
	 * 变量名称-命令项目-值
	 */
	public static final String VARIABLE_NAME_VALUE = "${VALUE}";
	/**
	 * 变量名称-工作目录
	 */
	public static final String VARIABLE_NAME_WORK_FOLDER = "${WORK_FOLDER}";
	/**
	 * 变量名称-文件分隔符
	 */
	public static final String VARIABLE_NAME_FILE_SEPARATOR = "${FILE_SEPARATOR}";

	public Variable() {

	}

	public Variable(String name, String value) {
		this.setName(name);
		this.setValue(value);
	}

	/**
	 * 名称
	 */
	@XmlAttribute(name = "Name")
	private String name;

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * 值
	 */
	@XmlAttribute(name = "Value")
	private String value;

	public final String getValue() {
		if (value == null) {
			value = "";
		}
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("{variable %s %s}", this.getName(), this.getValue());
	}
}
