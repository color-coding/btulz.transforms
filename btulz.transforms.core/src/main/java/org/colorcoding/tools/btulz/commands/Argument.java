package org.colorcoding.tools.btulz.commands;

/**
 * 参数
 * 
 * @author Niuren.Zhu
 *
 */
public class Argument {

	public Argument() {

	}

	public Argument(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && !name.startsWith("-")) {
			name = "-" + name;
		}
		this.name = name;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String original;

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 判断字符串是否为当前变量
	 * 
	 * @param string
	 *            分析的字符串
	 * @return
	 */
	public boolean check(String string) {
		if (string == null || string.isEmpty()) {
			return false;
		}

		return false;
	}
}
