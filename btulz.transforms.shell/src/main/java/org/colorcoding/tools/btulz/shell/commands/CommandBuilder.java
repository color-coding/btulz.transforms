package org.colorcoding.tools.btulz.shell.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 命令构造器
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CommandBuilder", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
@XmlType(name = "CommandBuilder", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class CommandBuilder implements Comparable<CommandBuilder> {
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

	@XmlElement(name = "Item")
	private CommandItems items;

	public final CommandItems getItems() {
		if (this.items == null) {
			this.items = new CommandItems();
		}
		return items;
	}

	private String workFolder;

	public final String getWorkFolder() {
		if (this.workFolder == null) {
			this.workFolder = Environment.getWorkingFolder();
		}
		if (!this.workFolder.endsWith(File.separator)) {
			// 自动补路径符
			this.workFolder = this.workFolder + File.separator;
		}
		return workFolder;
	}

	public final void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	public String toString() {
		return String.format("{command builder %s}", this.getName());
	}

	private List<Variable> variables;

	protected List<Variable> getVariables() {
		if (this.variables == null) {
			this.variables = new ArrayList<>();
			this.variables.add(new Variable(Variable.VARIABLE_NAME_WORK_FOLDER, this.getWorkFolder()));
			this.variables.add(new Variable(Variable.VARIABLE_NAME_FILE_SEPARATOR, File.separator));
		}
		return this.variables;
	}

	/**
	 * 形成命令
	 * 
	 * @return 命令字符串
	 */
	public String toCommand() {
		StringBuilder stringBuilder = new StringBuilder();
		for (CommandItem commandItem : this.getItems()) {
			if (commandItem == null) {
				continue;
			}
			if (!commandItem.isOptional() && !commandItem.isSelected()) {
				throw new RuntimeException(String.format("%s must be selected.", commandItem.getName()));
			}
			if (!commandItem.isSelected()) {
				continue;
			}
			List<Variable> tmpVariables = new ArrayList<>(this.getVariables());// 新建一个数组，避免变量间影响
			if (stringBuilder.length() > 0) {
				stringBuilder.append(" ");
			}
			if (commandItem.getContent() != null && !commandItem.getContent().isEmpty()) {
				// 添加命令内容
				String itemValue = commandItem.getValue();
				if (commandItem.getItems().size() > 0) {
					itemValue = commandItem.getItems().getValue(tmpVariables);
				}
				if (itemValue == null) {
					itemValue = "";
				}
				// 替换value中变量
				for (Variable variable : tmpVariables) {
					itemValue = itemValue.replace(variable.getName(), variable.getValue());
				}
				// 替换content中变量
				tmpVariables.add(new Variable(Variable.VARIABLE_NAME_VALUE, itemValue));

				String content = commandItem.getContent();
				for (Variable variable : tmpVariables) {
					content = content.replace(variable.getName(), variable.getValue());
				}
				stringBuilder.append(content);
			}
		}
		return stringBuilder.toString();
	}

	@Override
	public int compareTo(CommandBuilder o) {
		return this.getName().compareTo(o.getName());
	}

}
