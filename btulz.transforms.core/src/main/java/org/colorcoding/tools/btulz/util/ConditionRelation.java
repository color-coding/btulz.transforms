package org.colorcoding.tools.btulz.util;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlType(name = "ConditionRelation", namespace = Environment.NAMESPACE_BTULZ_UTIL)
public enum ConditionRelation {
	/** 无 */
	NONE,
	/** 且 */
	AND,
	/** 或 */
	OR;
}
