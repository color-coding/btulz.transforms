package org.colorcoding.tools.btulz.shell.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 项目的有效值
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ValidValue", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class ValidValue {

	public ValidValue() {

	}

	public ValidValue(String value) {
		this();
		this.setValue(value);
	}

	public ValidValue(String value, String description) {
		this(value);
		this.setDescription(description);
	}

	/**
	 * 描述
	 */
	@XmlAttribute(name = "Description")
	private String description;

	public final String getDescription() {
		if (this.description == null) {
			return this.getValue();
		}
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 值
	 */
	@XmlAttribute(name = "Value")
	private String value;

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}
}
