package org.colorcoding.tools.btulz.transformer.region;

import org.colorcoding.tools.btulz.template.InvalidRegionException;
import org.colorcoding.tools.btulz.template.TemplateRegion;

/**
 * 领域模型的区域基类
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class RegionBase extends TemplateRegion {
	public RegionBase() {
		super();
	}

	public RegionBase(String beginDelimiter, String endDelimiter) {
		super(beginDelimiter, endDelimiter);
	}

	public RegionBase(String delimiter) {
		super(delimiter);
	}

	@Override
	protected TemplateRegion createRegion(String beginDelimiter) throws InvalidRegionException {
		TemplateRegion region = RegionsFactory.create().createRegion(beginDelimiter);
		if (region != null) {
			return region;
		}
		return super.createRegion(beginDelimiter);
	}

}
