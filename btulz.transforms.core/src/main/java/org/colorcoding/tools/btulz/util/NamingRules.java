package org.colorcoding.tools.btulz.util;

/**
 * 命令规则
 * 
 * @author Niuren.Zhu
 *
 */
public class NamingRules {

	/**
	 * 规则名称-驼峰-小，如：itemCode
	 */
	public final static String RULES_NAME_CAMEL_CASE_LOWER = "LowerCamelCase";
	/**
	 * 规则名称-驼峰-大，如：ItemCode
	 */
	public final static String RULES_NAME_CAMEL_CASE_UPPER = "UpperCamelCase";

	/**
	 * 格式化
	 * 
	 * @param type
	 *            规则
	 * @param value
	 *            值
	 * @return 格式化的值
	 */
	public static String format(String type, String value) {
		if (RULES_NAME_CAMEL_CASE_LOWER.equalsIgnoreCase(type)) {
			String name = value.substring(0, 1);
			name = name.toLowerCase() + value.substring(1);
			return name;
		} else if (RULES_NAME_CAMEL_CASE_UPPER.equalsIgnoreCase(type)) {
			String name = value.substring(0, 1);
			name = name.toUpperCase() + value.substring(1);
			return name;
		}
		// 原样返回
		return value;
	}
}
