package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * 注释区域
 * 
 * @author Niuren.Zhu
 *
 */
public class CommentRegion extends TemplateRegion {
	public CommentRegion() {
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
	public void export(BufferedWriter writer, Parameters pars) throws Exception {
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(Parameters pars) {
		return null;
	}
}
