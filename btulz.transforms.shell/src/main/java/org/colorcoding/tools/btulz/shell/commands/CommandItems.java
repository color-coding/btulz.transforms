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
		List<Variable> tmpVariables = new ArrayList<>(variables);
		StringBuilder stringBuilder = new StringBuilder();
		for (CommandItem item : this) {
			String content = item.getContent();
			String itemValue = item.getValue();
			if (item.getItems().size() > 0) {
				itemValue = item.getItems().getValue(variables);
			}
			tmpVariables.add(new Variable(Variable.VARIABLE_NAME_VALUE, itemValue));
			for (Variable variable : tmpVariables) {
				content = content.replace(variable.getName(), variable.getValue());
			}
			stringBuilder.append(content);
		}
		return stringBuilder.toString();
	}

}
