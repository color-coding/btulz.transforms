package org.colorcoding.tools.btulz.transformer.region;

import org.colorcoding.tools.btulz.template.InvalidRegionException;
import org.colorcoding.tools.btulz.template.JudgmentRegion;
import org.colorcoding.tools.btulz.template.TemplateRegion;

/**
 * 区域-判断（根据区域名称，判断条件是否成立）
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionJudgment extends JudgmentRegion {

	public RegionJudgment(String delimiter) {
		super(delimiter);
	}

	@Override
	protected TemplateRegion createRegion(String delimiter) throws InvalidRegionException {
		TemplateRegion region = RegionsFactory.create().createRegion(delimiter);
		if (region != null) {
			return region;
		}
		return super.createRegion(delimiter);
	}

}
