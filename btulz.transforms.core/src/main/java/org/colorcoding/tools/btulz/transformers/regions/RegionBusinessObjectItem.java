package org.colorcoding.tools.btulz.transformers.regions;

import java.util.List;

import org.colorcoding.tools.btulz.templates.Parameter;

/**
 * 区域-业务对象模型
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionBusinessObjectItem extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "BUSINESS_OBJECT_ITEM";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "BusinessObjectItem";

	public RegionBusinessObjectItem() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		// 先获取领域，然后获取业务对象，再找业务对象的模型
		return null;
	}

}
