package org.colorcoding.tools.btulz.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ConditionJudger {

	public ConditionJudger(Condition[] conditions) {
		this.setConditions(conditions);
	}

	public ConditionJudger(List<Condition> conditions) {
		this.setConditions(conditions);
	}

	private List<Condition> conditions;

	protected final List<Condition> getConditions() {
		if (this.conditions == null) {
			this.conditions = new ArrayList<Condition>();
		}
		return conditions;
	}

	private final void setConditions(Condition[] conditions) {
		this.conditions = null;
		for (Condition condition : conditions) {
			this.getConditions().add(condition);
		}
	}

	private final void setConditions(List<Condition> conditions) {
		this.conditions = null;
		for (Condition condition : conditions) {
			this.getConditions().add(condition);
		}
	}

	public final boolean judge(Object data) {
		if (this.getConditions().isEmpty()) {
			return true;
		}
		this.getConditions().firstOrDefault().setRelation(ConditionRelation.NONE);
		return this.judge(0, this.getConditions(), data);
	}

	private List<Condition> bracketedConditions(int startIndex, List<Condition> conditions) {
		boolean done = false;// 完成
		int closeCount = 0;
		int bracket = -1;
		ArrayList<Condition> nConditions = new ArrayList<Condition>();
		for (int i = startIndex; i < conditions.size(); i++) {
			Condition jItem = conditions.get(i);
			if (bracket == -1) {
				bracket = jItem.getBracketOpen();
			}
			nConditions.add(jItem);
			if (jItem.getBracketClose() > 0) {
				closeCount += jItem.getBracketClose();
			}
			if (jItem.getBracketOpen() > 0 && i != startIndex) {
				closeCount -= jItem.getBracketOpen();
			}
			if (closeCount >= bracket) {
				// 闭环
				done = true;
				break;
			}
		}
		if (!done) {
			// 未标记完成，存在不匹配的括号
			throw new RuntimeException(String.format("Bracket [%s] is not closed.", bracket));
		}
		return nConditions;
	}

	protected boolean judge(int bracket, List<Condition> conditions, Object data) {
		boolean cValue = false;// 当前的结果
		Expression<Boolean> rExpression = null;
		for (int i = 0; i < conditions.size(); i++) {
			Condition iCondition = conditions.get(i);
			if (rExpression != null) {
				rExpression.setOperation(ExpressionOperation.valueOf(iCondition.getRelation()));
			}
			// 计算表达式结果
			if ((iCondition.getBracketOpen() != bracket || (iCondition.getBracketOpen() == bracket && i > 0))
					&& iCondition.getBracketOpen() > 0) {
				// 新的括号，先执行新括号判断
				List<Condition> nConditions = this.bracketedConditions(i, conditions);
				cValue = this.judge(iCondition.getBracketOpen(), nConditions, data);
				// 跳过已执行的
				if (nConditions.size() > 0) {
					i = i + nConditions.size() - 1;
				}
			} else {
				// 计算当前表达式
				Expression<?> cExpression = new ExpressionString();
				cExpression.setLeftValue(VALUES.propertyValue(data, iCondition.getProperty()));
				cExpression.setOperation(ExpressionOperation.valueOf(iCondition.getOperation()));
				cExpression.setRightValue(iCondition.getValue());
				cValue = cExpression.result();
			}
			if (rExpression == null) {
				// 第一个表达式
				rExpression = new ExpressionBoolean();
				rExpression.setLeftValue(cValue);
				rExpression.setOperation(ExpressionOperation.valueOf(ConditionRelation.AND));
				rExpression.setRightValue(true);
			} else {
				// 后续表达式
				rExpression.setRightValue(cValue);
			}
			cValue = rExpression.result();
			rExpression.setLeftValue(cValue);// 结果左移
			if (!rExpression.result()) {
				// 表达式不成立
				if (i == conditions.size() - 1) {
					// 最后一个表达式，返回结果
					return false;
				}
			}
		}
		return true;
	}

}

final class VALUES {

	private static Map<Class<?>, Map<String, Method>> ENTITY_PROPERTIES;

	@SuppressWarnings("unchecked")
	public static <T> T propertyValue(Object entity, String property) {
		if (ENTITY_PROPERTIES == null) {
			ENTITY_PROPERTIES = new HashMap<Class<?>, Map<String, Method>>();
		}
		Map<String, Method> propertyMap = ENTITY_PROPERTIES.get(entity.getClass());
		if (propertyMap == null) {
			ENTITY_PROPERTIES.put(entity.getClass(), propertyMap = new HashMap<String, Method>());
		}
		Method method = propertyMap.get(property);
		if (method == null) {
			for (Method item : entity.getClass().getMethods()) {
				if (item.getName().equalsIgnoreCase("get" + property)
						|| item.getName().equalsIgnoreCase("is" + property)) {
					propertyMap.put(property, method = item);
					break;
				}
			}
		}
		if (method == null) {
			throw new RuntimeException(String.format("%s not have property %s.", entity.getClass(), property));
		}
		try {
			return (T) method.invoke(entity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}