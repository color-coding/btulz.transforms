package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;

/**
 * 模板区域
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class TemplateRegion implements ITemplateData {
	public TemplateRegion() {
	}

	public TemplateRegion(String beginDelimiter, String endDelimiter) {
		this.setBeginDelimiter(beginDelimiter);
		this.setEndDelimiter(endDelimiter);
	}

	public TemplateRegion(String delimiter) {
		this((REGION_SIGN_BEGIN + delimiter + REGION_SIGN), (REGION_SIGN_END + delimiter + REGION_SIGN));
	}

	public static final String REGION_SIGN = "$";
	public static final String REGION_SIGN_BEGIN = REGION_SIGN + "BEGIN_";
	public static final String REGION_SIGN_END = REGION_SIGN + "END_";
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
		return String.format("TemplateRegion between %s and %s", this.getBeginDelimiter(), this.getEndDelimiter());
	}

	private ArrayList<ITemplateData> templateLines;

	protected List<ITemplateData> getTemplateLines() {
		if (this.templateLines == null) {
			this.templateLines = new ArrayList<>();
		}
		return this.templateLines;
	}

	protected TemplateRegion createRegion(String delimiter) throws InvalidRegionException {
		if (delimiter.startsWith(REGION_SIGN_BEGIN)) {
			delimiter = delimiter.substring(REGION_SIGN_BEGIN.length(), delimiter.length() - 1);
		}
		if (delimiter.startsWith(REGION_SIGN + REGION_SIGN)) {
			// 备注区域
			return new CommentRegion();
		} else if (delimiter.indexOf(JudgmentRegion.REGION_DELIMITER) > 0) {
			// 判断区域
			return new JudgmentRegion(delimiter);
		}
		throw new InvalidRegionException(String.format("undefined region delimiter %s.", delimiter));
	}

	void parse(BufferedReader template) throws Exception {
		String readString = null;
		while ((readString = template.readLine()) != null) {
			if ((readString.startsWith(REGION_SIGN) && readString.endsWith(REGION_SIGN))
					|| readString.startsWith(REGION_SIGN + REGION_SIGN)) {
				// like $……$，发现区域，创建新的区域
				if ((this.getEndDelimiter() != null && this.getEndDelimiter().length() > 0)
						&& readString.indexOf(this.getEndDelimiter()) >= 0) {
					// 自身的结束区域
					break;
				}
				TemplateRegion region = this.createRegion(readString.trim());
				region.parse(template);
				this.getTemplateLines().add(region);
				Environment.getLogger().debug(String.format("template: create region [%s].", region));
			} else {
				// 处理变量
				this.getTemplateLines().add(new TemplateLine(readString));
			}
		}
	}

	public void export(BufferedWriter writer, List<Parameter> pars) throws Exception {
		List<Parameter> newPars = pars;
		for (ITemplateData tpltLine : this.getTemplateLines()) {
			if (tpltLine instanceof TemplateRegion && !(tpltLine instanceof CommentRegion)) {
				// 重复区域，需要循环输出
				// 输出模板数据
				TemplateRegion region = (TemplateRegion) tpltLine;
				Iterable<Parameter> regionPars = region.getRegionParameters(pars);
				if (regionPars == null) {
					throw new InvalidParameterException(String.format("invalid parameter between %s and %s.",
							region.getBeginDelimiter(), region.getEndDelimiter()));
				}
				Environment.getLogger().debug(String.format("template: begin export region [%s].", region));
				for (Parameter regionPar : regionPars) {
					// 替换区域变量
					if (regionPar != null) {
						Environment.getLogger().debug(String.format("template: using region parameter [%s:%s].",
								regionPar.getName(), regionPar.getValue()));
						newPars = new ArrayList<Parameter>(pars);
						boolean done = false;
						for (int i = 0; i < newPars.size(); i++) {
							if (regionPar.getName().equals(newPars.get(i).getName())) {
								newPars.set(i, regionPar);
								done = true;
							}
						}
						if (!done)
							newPars.add(regionPar);
					}
					tpltLine.export(writer, newPars);
				}
				Environment.getLogger().debug(String.format("template: end export region [%s].", tpltLine));
			} else {
				tpltLine.export(writer, newPars);
			}
		}
		writer.flush();
	}

	protected Parameter getParameter(List<Parameter> parameters, String name) {
		for (Parameter parameter : parameters) {
			if (parameter.getName().equalsIgnoreCase(name)) {
				return parameter;
			}
		}
		return null;
	}

	protected abstract Iterable<Parameter> getRegionParameters(List<Parameter> pars) throws InvalidParameterException;
}
