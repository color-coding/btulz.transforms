package org.colorcoding.tools.btulz.shell.commands;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 子项集合
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "CommandItems", namespace = Environment.NAMESPACE_BTULZ_SHELL_COMMANDS)
public class CommandItems extends ArrayList<CommandItem> {

	private static final long serialVersionUID = -4475012404050446583L;

	public String getValue(List<Variable> variables) {
		if (variables == null)
			variables = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		for (CommandItem item : this) {
			List<Variable> tmpVariables = new ArrayList<>(variables);
			String content = item.getContent();
			String itemValue = item.getValue();
			if (item.getItems().size() > 0) {
				itemValue = item.getItems().getValue(tmpVariables);
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
			for (Variable variable : tmpVariables) {
				content = content.replace(variable.getName(), variable.getValue());
			}
			stringBuilder.append(content);
		}
		return stringBuilder.toString();
	}

	public CommandItem create() {
		CommandItem item = new CommandItem();
		this.add(item);
		return item;
	}

	public int getCount() {
		return this.getCount(this);
	}

	private int getCount(CommandItems items) {
		int i = this.size();
		for (CommandItem commandItem : items) {
			i += this.getCount(commandItem.getItems());
		}
		return i;
	}
}
