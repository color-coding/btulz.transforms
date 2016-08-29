package org.colorcoding.tools.btulz.transformers.regions;

import org.colorcoding.tools.btulz.templates.InvalidRegionException;
import org.colorcoding.tools.btulz.templates.Region;

/**
 * 领域模型的区域基类
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class RegionBase extends Region {
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
	protected Region createRegion(String beginDelimiter) throws InvalidRegionException {
		Region region = RegionsFactory.create().createRegion(beginDelimiter);
		if (region != null) {
			return region;
		}
		return super.createRegion(beginDelimiter);
	}

}
