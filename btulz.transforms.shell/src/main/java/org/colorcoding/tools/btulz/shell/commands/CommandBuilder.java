package org.colorcoding.tools.btulz.shell.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 命令构造器
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "CommandBuilder", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class CommandBuilder {
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

	@XmlElement(name = "Items")
	private CommandItem[] items;

	public final CommandItem[] getItems() {
		if (this.items == null) {
			this.items = new CommandItem[] {};
		}
		return items;
	}

	public final void setItems(CommandItem[] items) {
		this.items = items;
	}

	/**
	 * 形成命令
	 * 
	 * @return 命令字符串
	 */
	public String toCommand() {
		StringBuilder stringBuilder = new StringBuilder();
		for (CommandItem commandItem : items) {
			if (commandItem == null) {
				continue;
			}
			if (commandItem.isMust() && !commandItem.isSelected()) {
				throw new RuntimeException(String.format("%s must be selected.", commandItem.getName()));
			}
			if (stringBuilder.length() > 0) {
				stringBuilder.append(" ");
			}
			stringBuilder.append(commandItem.getName());
			if (commandItem.getValue() != null) {
				stringBuilder.append(commandItem.getOperator());
				stringBuilder.append(commandItem.getValue());
			}
		}
		return stringBuilder.toString();
	}
}
