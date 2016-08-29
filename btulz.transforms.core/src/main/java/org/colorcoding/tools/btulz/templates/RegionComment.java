package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;

/**
 * 注释区域
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionComment extends TemplateRegion {
	public RegionComment() {
		super("$", "$");
	}

	@Override
	public String toString() {
		return String.format("RegionComment", this.getBeginDelimiter(), this.getEndDelimiter());
	}

	@Override
	void parse(BufferedReader template) throws Exception {
	}

	@Override
	public void export(BufferedWriter writer, List<Parameter> pars) throws Exception {
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		return null;
	}
}
