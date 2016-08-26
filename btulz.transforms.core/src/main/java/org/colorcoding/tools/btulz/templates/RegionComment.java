package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * 注释区域
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionComment extends Region {
	public RegionComment() {
		super("$", "$");
	}

	public void parse(BufferedReader template, BufferedWriter outPut) throws Exception {
		return;
	}

	@Override
	protected Iterable<Parameter> getRegionParameters() {
		return null;
	}
}
