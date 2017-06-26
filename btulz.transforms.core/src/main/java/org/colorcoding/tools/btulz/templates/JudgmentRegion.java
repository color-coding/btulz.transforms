package org.colorcoding.tools.btulz.templates;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * 区域-判断（根据区域名称，判断条件是否成立）
 * 
 * @author Niuren.Zhu
 *
 */
public class JudgmentRegion extends TemplateRegion {

	public JudgmentRegion(String delimiter) {
		super(delimiter);
	}

	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "_IS_";

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		String[] regionValues = this.getBeginDelimiter().replace(REGION_SIGN_BEGIN, "").replace(REGION_SIGN, "")
				.split("_");
		if (regionValues != null && regionValues.length > 1) {
			String parName = regionValues[0];
			Parameter parameter = parameters.get(parName);
			if (parameter != null && parameter.getValue() != null) {
				Object value = parameter.getValue();
				int index = 0;
				// 循环取值
				for (int i = 1; i < regionValues.length; i++) {
					if (regionValues[i].equals(REGION_DELIMITER.replace("_", ""))) {
						// 到is终止
						index = i;
						break;
					}
					if (value != null) {
						Class<?> type = value.getClass();
						for (Method method : type.getMethods()) {
							if (method.getName() != null && method.getParameterCount() == 0
									&& method.getName().toUpperCase().endsWith(regionValues[i])) {
								// 找到一次就退出
								try {
									value = method.invoke(value);
								} catch (Exception e) {
									value = null;
								}
								break;
							}
						}
					}
				}
				// 判断取到的值与区域定义是否一致
				boolean done = true;
				String valueString = String.valueOf(value).toUpperCase();
				for (int i = index + 1; i < regionValues.length; i++) {
					// 跳过is比较直
					if (!valueString.equals(regionValues[i])) {
						done = false;
						break;
					}
				}
				if (done) {
					// 与区域值一致
					return new Iterator<Parameter>() {
						int curIndex = 1;

						@Override
						public boolean hasNext() {
							return curIndex > 0;
						}

						@Override
						public Parameter next() {
							curIndex--;
							return null;
						}
					};
				} else {
					// 与区域值不一致
					return new Iterator<Parameter>() {

						@Override
						public boolean hasNext() {
							return false;
						}

						@Override
						public Parameter next() {
							return null;
						}
					};
				}
			}
		}
		return null;
	}

}
