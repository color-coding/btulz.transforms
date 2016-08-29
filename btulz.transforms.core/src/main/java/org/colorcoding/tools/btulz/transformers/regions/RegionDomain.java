package org.colorcoding.tools.btulz.transformers.regions;

import org.colorcoding.tools.btulz.templates.InvalidRegionException;
import org.colorcoding.tools.btulz.templates.Region;
import org.colorcoding.tools.btulz.templates.RegionTemplate;

/**
 * 区域-领域
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionDomain extends RegionTemplate {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "DOMAIN";

	@Override
	protected Region createRegion(String beginDelimiter) throws InvalidRegionException {
		Region region = RegionsFactory.create().createRegion(beginDelimiter);
		if (region != null) {
			return region;
		}
		return super.createRegion(beginDelimiter);
	}

}
