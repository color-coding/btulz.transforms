package org.colorcoding.tools.btulz.templates;

import java.io.BufferedWriter;
import java.util.List;

/**
 * 模板行
 * 
 * @author Niuren.Zhu
 *
 */
public class TemplateLine implements ITemplateData {

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
	public void export(BufferedWriter writer, List<Parameter> pars) throws Exception {
		String outLine = new String(this.getLine());
		Variable[] variables = Variable.discerning(this.getLine());
		for (Variable variable : variables) {
			if (variable.getName() != null) {
				Parameter parameter = null;
				for (Parameter par : pars) {
					if (par.getName().equals(variable.getName())) {
						parameter = par;
						break;
					}
				}
				if (parameter != null) {
					try {
						Object value = parameter.getValue(variable.getValuePath());
						if (value != null) {
							variable.setValue(value);
							outLine.replace(variable.getOriginal(), variable.getValue());
						}
					} catch (Exception e) {
						System.err.println(String.format("replace [%s]'value, error %s", variable.getOriginal(), e));
					}
				}
			}
		}
		writer.write(outLine);
		writer.newLine();
	}

}
