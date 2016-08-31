package org.colorcoding.tools.btulz.templates;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.colorcoding.tools.btulz.Environment;

/**
 * 变量
 * 
 * @author Niuren.Zhu
 *
 */
public class Variable {
	/**
	 * 识别字符中的变量
	 * 
	 * @param string
	 *            分析的字符串
	 * @return
	 */
	public static Variable[] discerning(String string) {
		ArrayList<Variable> variables = new ArrayList<>();
		if (string != null && !string.equals("")) {
			// ${Model.getMapped()}
			String pattern = "\\$\\{([\\!a-zA-Z].*?)\\}";
			Matcher matcher = Pattern.compile(pattern).matcher(string);
			while (matcher.find()) {
				Variable variable = new Variable();
				variable.setOriginal(matcher.group(0));
				String tmp = variable.getOriginal().replace("${", "").replace("}", "");
				String[] tmps = tmp.split("\\.");
				if (tmps.length > 1) {
					variable.setName(tmps[0]);
					variable.setValuePath(tmp.substring(variable.getName().length() + 1));
				} else {
					variable.setName(tmp);
				}
				variables.add(variable);
			}
			String line = System.getProperty("line.seperator", "\n");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder
					.append(String.format("template: get [%s] parameters in [%s].", variables.size(), string.trim()));
			for (Variable variable : variables) {
				stringBuilder.append(line);
				stringBuilder.append(String.format("    %s | %s", variable.toString(), variable.getOriginal()));
			}
			Environment.getLogger().debug(stringBuilder.toString());
		}
		return variables.toArray(new Variable[] {});
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(Object value) {
		if (value != null) {
			this.value = String.valueOf(value);
		}
	}

	private String original;

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	private String valuePath;

	public String getValuePath() {
		return valuePath;
	}

	public void setValuePath(String valuePath) {
		this.valuePath = valuePath;
	}

	public String toString() {
		if (this.getValuePath() != null) {
			return String.format("Variable %s %s", this.getName(), this.getValuePath());
		}
		return String.format("Variable %s", this.getName());
	}
}
