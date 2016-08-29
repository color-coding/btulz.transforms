package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板区域
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class Region {
	public Region() {
	}

	public Region(String beginDelimiter, String endDelimiter) {
		this.setBeginDelimiter(beginDelimiter);
		this.setEndDelimiter(endDelimiter);
	}

	public Region(String delimiter) {
		this(String.format("%sBEGIN_%s%s", REGION_SIGN, delimiter, REGION_SIGN),
				String.format("%sEND_%s%s", REGION_SIGN, delimiter, REGION_SIGN));
	}

	public static final String REGION_SIGN = "$";

	private ArrayList<Parameter> parameters;

	public List<Parameter> getParameters() {
		if (this.parameters == null) {
			this.parameters = new ArrayList<>();
		}
		return parameters;
	}

	protected Parameter getParameter(String name) {
		for (Parameter parameter : this.getParameters()) {
			if (parameter.getName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}

	public void addParameter(String name, Object value) {
		for (Parameter parameter : this.getParameters()) {
			if (parameter.getName().equals(name)) {
				parameter.setValue(value);
				return;
			}
		}
		this.getParameters().add(new Parameter(name, value));
	}

	private String beginDelimiter;

	/**
	 * 获取-开始标识符
	 * 
	 * @return
	 */
	public String getBeginDelimiter() {
		return beginDelimiter;
	}

	public void setBeginDelimiter(String beginDelimiter) {
		this.beginDelimiter = beginDelimiter;
	}

	private String endDelimiter;

	/**
	 * 获取-结束标识符
	 * 
	 * @return
	 */
	public String getEndDelimiter() {
		if (this.endDelimiter == null && this.beginDelimiter != null) {

		}
		return endDelimiter;
	}

	public void setEndDelimiter(String endDelimiter) {
		this.endDelimiter = endDelimiter;
	}

	@Override
	public String toString() {
		return String.format("region between %s and %s", this.getBeginDelimiter(), this.getEndDelimiter());
	}

	private ArrayList<String> templateLines;

	protected List<String> getTemplateLines() {
		if (this.templateLines == null) {
			this.templateLines = new ArrayList<>();
		}
		return this.templateLines;
	}

	protected Region createRegion(String beginDelimiter) throws InvalidRegionException {
		if (beginDelimiter == "$$") {
			// 备注区域
			return new RegionComment();
		}
		throw new InvalidRegionException(String.format("undefined region delimiter %s.", beginDelimiter));
	}

	public void parse(BufferedReader template, BufferedWriter outPut) throws Exception {
		String readString = null;
		while ((readString = template.readLine()) != null) {
			if (readString.startsWith(REGION_SIGN) && readString.endsWith(REGION_SIGN)) {
				// like $……$，发现区域，创建新的区域
				if ((this.getEndDelimiter() != null && this.getEndDelimiter().length() > 0)
						&& readString.indexOf(this.getEndDelimiter()) >= 0) {
					// 自身的结束区域
					break;
				}
				Region region = this.createRegion(readString.trim());
				region.getParameters().addAll(this.getParameters());// 传递参数到子区域
				region.parse(template, outPut);
			} else {
				// 处理变量
				this.getTemplateLines().add(new String(readString));
			}
		}
		// 输出模板数据
		Iterable<Parameter> regionParameters = this.getRegionParameters();
		if (regionParameters == null) {
			throw new InvalidParameterException(String.format("invalid parameter between %s and %s.",
					this.getBeginDelimiter(), this.getEndDelimiter()));
		}
		for (Parameter regionParameter : regionParameters) {
			// 替换区域变量
			if (regionParameter != null) {
				for (int i = 0; i < this.getParameters().size(); i++) {
					Parameter parameter = this.getParameters().get(i);
					if (parameter.getName() == regionParameter.getName()) {
						this.getParameters().set(i, parameter);
					}
				}
			}
			// 重复区域变量内容，并输出
			for (String tpltLine : this.getTemplateLines()) {
				Variable[] variables = Variable.discerning(tpltLine);
				for (Variable variable : variables) {
					if (variable.getName() != null) {
						Parameter parameter = this.getParameter(variable.getName());
						if (parameter != null) {
							try {
								Object value = parameter.getValue(variable.getValuePath());
								if (value != null) {
									variable.setValue(value);
									tpltLine.replace(variable.getOriginal(), variable.getValue());
								}
							} catch (Exception e) {
								System.err.println(
										String.format("replace [%s]'value, error %s", variable.getOriginal(), e));
							}
						}
					}
				}
				outPut.write(tpltLine);
				outPut.newLine();
			}
		}
		outPut.flush();
	}

	protected abstract Iterable<Parameter> getRegionParameters() throws InvalidParameterException;
}
