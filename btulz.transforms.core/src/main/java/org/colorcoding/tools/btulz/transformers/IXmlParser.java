package org.colorcoding.tools.btulz.transformers;

import org.colorcoding.tools.btulz.models.IDomain;
import org.w3c.dom.Node;

/**
 * xml解释器
 * 
 * @author Niuren.Zhu
 *
 */
public interface IXmlParser {
	/**
	 * 解析xml文档
	 * 
	 * @param document
	 * @return 包含的领域模型
	 */
	IDomain parse(Node root);

	/**
	 * 值类型转换
	 * 
	 * @param type
	 *            目标类型
	 * @param value
	 *            值（xml节点值）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default <P> P convert(Class<P> type, String value) {
		if (type.isEnum()) {
			// 枚举
			for (Object enumItem : type.getEnumConstants()) {
				if (value.equals(enumItem.toString())) {
					// 枚举的字符串
					return (P) enumItem;
				}
			}
		} else if (type == int.class) {
			return (P) Integer.valueOf(value);
		} else if (type == boolean.class) {
			if (value.equals("Yes")) {
				return (P) Boolean.TRUE;
			} else if (value.equals("No")) {
				return (P) Boolean.FALSE;
			}
			return (P) Boolean.valueOf(value);
		}
		return (P) value;

	}
}
