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
		boolean loop = false;
		do {
			Variable[] variables = Variable.discerning(outLine);
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
							}
						} catch (InvocationTargetException e) {
							Environment.getLogger().error(String.format("template: replace [%s]'value, error %s",
									variable.getOriginal(), e.getTargetException()));
						} catch (Exception e) {
							Environment.getLogger().error(
									String.format("template: replace [%s]'value, error %s", variable.getOriginal(), e));
						}
					} else {
						Environment.getLogger().warn(
								String.format("template: not found variable [%s]'s parameter.", variable.getName()));
					}
				}
			}
			if (!loop) {
				if (outLine != null && outLine.indexOf("${") > 0) {
					// 输出字符存在变量时，重复一次运算
					loop = true;
				}
			} else {
				loop = false;
			}
		} while (loop);
		if (outLine != null) {
			writer.write(outLine);
			writer.newLine();
		}
	}

}
