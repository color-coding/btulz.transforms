package org.colorcoding.tools.btulz.template;

import java.io.BufferedWriter;
import java.lang.reflect.InvocationTargetException;

import org.colorcoding.tools.btulz.Environment;

/**
 * 模板行
 * 
 * @author Niuren.Zhu
 *
 */
public class TemplateLine implements ITemplateData {
	/**
	 * 延迟解析标记
	 */
	public static final String DELAYED_MARK = "!";

	public TemplateLine(String data) {
		this.line = data;
	}

	private String line;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return String.format("TemplateLine %s", this.getLine());
	}

	@Override
	public void export(Parameters parameters, BufferedWriter writer) throws Exception {
		String outLine = this.getLine();
		if (outLine == null) {
			return;
		}
		boolean loop, hasErr;
		do {
			loop = false;
			hasErr = false;
			Variable[] variables = Variable.discerning(outLine);
			if (variables.length <= 0) {
				hasErr = true;
			}
			for (Variable variable : variables) {
				if (variable == null || variable.getName() == null) {
					continue;
				}
				if (variable.getName().startsWith(DELAYED_MARK)) {
					outLine = outLine.replace(variable.getOriginal(), variable.getOriginal().replace("${!", "${"));
				} else {
					Parameter parameter = parameters.get(variable.getName());
					if (parameter != null) {
						try {
							Object value = parameter.getValue(variable.getValuePath());
							if (value != null) {
								variable.setValue(value);
								outLine = outLine.replace(variable.getOriginal(), variable.getValue());
							} else {
								hasErr = true;
								Environment.getLogger().warn(String
										.format("template: not found parameter [%s]'s value.", variable.getName()));
							}
						} catch (InvocationTargetException e) {
							hasErr = true;
							Environment.getLogger().error(String.format("template: replace [%s]'value, error %s",
									variable.getOriginal(), e.getTargetException()));
						} catch (Exception e) {
							hasErr = true;
							Environment.getLogger().error(
									String.format("template: replace [%s]'value, error %s", variable.getOriginal(), e));
						}
					} else {
						hasErr = true;
						Environment.getLogger().warn(
								String.format("template: not found variable [%s]'s parameter.", variable.getName()));
					}
				}
			}
			// 输出字符存在变量时，重复运算
			if (!hasErr && outLine.indexOf("${") > 0) {
				loop = true;
			}
		} while (loop);
		writer.write(outLine);
		writer.newLine();
	}

}
