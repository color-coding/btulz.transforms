package org.colorcoding.tools.btulz.shell.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
		this.setOptional(false);
		this.setSelected(true);
		this.setEditable(true);
	}

	/**
	 * 可选选择项目
	 */
	@XmlAttribute(name = "Optional")
	private boolean optional;

	public final boolean isOptional() {
		return optional;
	}

	public final void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * 已选择
	 */
	private boolean selected;

	public final boolean isSelected() {
		if (!this.isOptional()) {
			this.setSelected(true);
		}
		return selected;
	}

	public final void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * 可编辑
	 */
	@XmlAttribute(name = "Editable")
	private boolean editable;

	public final boolean isEditable() {
		return editable;
	}

	public final void setEditable(boolean editable) {
		this.editable = editable;
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
	 * 命令内容
	 */
	@XmlAttribute(name = "Content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
	@XmlElement(name = "ValidValues")
	private ValidValues validValues;

	public final ValidValues getValidValues() {
		if (this.validValues == null) {
			this.validValues = new ValidValues();
		}
		return validValues;
	}

	/**
	 * 子项
	 */
	@XmlElementWrapper(name = "Items")
	@XmlElement(name = "Item", type = CommandItem.class)
	private CommandItems items;

	public final CommandItems getItems() {
		if (this.items == null) {
			this.items = new CommandItems();
		}
		return items;
	}

	public String toString() {
		return String.format("{command item %s}", this.getName() != null ? this.getName() : this.getContent());
	}
}
