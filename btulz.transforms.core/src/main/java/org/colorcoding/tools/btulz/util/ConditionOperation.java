package org.colorcoding.tools.btulz.util;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlType(name = "ConditionOperation", namespace = Environment.NAMESPACE_BTULZ_UTIL)
public enum ConditionOperation {
	/** 等于(=) */
	EQUAL,
	/** 不等于(<>) */
	NOT_EQUAL,
	/** 开始为 */
	BEGIN_WITH,
	/** 非开始为 */
	NOT_BEGIN_WITH,
	/** 结束为 */
	END_WITH,
	/** 非结束为 */
	NOT_END_WITH,
	/** 包含 */
	CONTAIN,
	/** 不包含 */
	NOT_CONTAIN,
}
