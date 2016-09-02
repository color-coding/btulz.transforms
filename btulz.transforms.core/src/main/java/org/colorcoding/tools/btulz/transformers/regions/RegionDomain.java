package org.colorcoding.tools.btulz.transformers.regions;

import org.colorcoding.tools.btulz.templates.InvalidRegionException;
import org.colorcoding.tools.btulz.templates.Template;
import org.colorcoding.tools.btulz.templates.TemplateRegion;

/**
 * 区域-领域
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionDomain extends Template {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "DOMAIN";
	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Domain";

	@Override
	protected TemplateRegion createRegion(String beginDelimiter) throws InvalidRegionException {
		TemplateRegion region = RegionsFactory.create().createRegion(beginDelimiter);
		if (region != null) {
			return region;
		}
		return super.createRegion(beginDelimiter);
	}

}
