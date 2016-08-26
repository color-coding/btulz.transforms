package org.colorcoding.tools.btulz.templates;

import java.util.ArrayList;

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
}
