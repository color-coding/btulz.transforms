package org.colorcoding.tools.btulz.shell.commands;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * 变量
 * 
* @author Niuren.Zhu
 *
 */
public class Variable {

	public static final String VARIABLE_NAME_VALUE = "${Value}";

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
}
