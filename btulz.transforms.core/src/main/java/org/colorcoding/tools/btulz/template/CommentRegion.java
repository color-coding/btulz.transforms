package org.colorcoding.tools.btulz.template;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Iterator;

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
		return String.format("RegionComment between %s and %s", this.getBeginDelimiter(), this.getEndDelimiter());
	}

	@Override
	void parse(BufferedReader template) throws Exception {
	}

	@Override
	public void export(Parameters pars, BufferedWriter writer) throws Exception {
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters pars) {
		return null;
	}
}
