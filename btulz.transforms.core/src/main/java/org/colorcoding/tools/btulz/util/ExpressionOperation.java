package org.colorcoding.tools.btulz.util;

public enum ExpressionOperation {
	/** 与 */
	AND,
	/** 或 */
	OR,
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
	NOT_CONTAIN;

	public static ExpressionOperation valueOf(ConditionRelation value) {
		if (value == ConditionRelation.AND) {
			return AND;
		} else if (value == ConditionRelation.OR) {
			return OR;
		}
		return null;
	}

	public static ExpressionOperation valueOf(ConditionOperation value) {
		if (value == null) {
			return null;
		} else {
			return ExpressionOperation.valueOf(value.toString());
		}
	}

}
