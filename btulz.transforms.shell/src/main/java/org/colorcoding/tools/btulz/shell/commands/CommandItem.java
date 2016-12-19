package org.colorcoding.tools.btulz.shell.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 命令项
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "CommandItem", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class CommandItem {

	public CommandItem() {
		this.setMust(true);
		this.setSelected(false);
	}

	/**
	 * 必须选择项目
	 */
	@XmlAttribute(name = "Must")
	private boolean must;

	public final boolean isMust() {
		return must;
	}

	public final void setMust(boolean must) {
		this.must = must;
	}

	/**
	 * 已选择
	 */
	private boolean selected;

	public final boolean isSelected() {
		if (this.isMust()) {
			this.setSelected(true);
		}
		return selected;
	}

	public final void setSelected(boolean selected) {
		this.selected = selected;
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
	 * 描述
	 */
	@XmlAttribute(name = "Description")
	private String description;

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 操作符，名称与值的
	 */
	private String operator;

	@XmlAttribute(name = "Operator")
	public final String getOperator() {
		if (this.operator == null) {
			this.operator = "=";
		}
		return operator;
	}

	public final void setOperator(String operator) {
		this.operator = operator;
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

	/**
	 * 可选值
	 */
	@XmlElement(name = "Values")
	private CommandItemValues itemValues;

	public final CommandItemValues getItemValues() {
		if (this.itemValues == null) {
			this.itemValues = new CommandItemValues();
		}
		return itemValues;
	}

}
