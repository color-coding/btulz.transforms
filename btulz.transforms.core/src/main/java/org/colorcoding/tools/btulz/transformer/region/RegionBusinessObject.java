package org.colorcoding.tools.btulz.transformer.region;

import java.util.Iterator;

import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;

/**
 * 区域-业务对象
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionBusinessObject extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "BO";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "BO";

	public RegionBusinessObject() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IDomain domain = parameters.getValue(RegionDomain.REGION_PARAMETER_NAME, IDomain.class);
		if (domain != null) {
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < domain.getBusinessObjects().size() ? true : false;
				}

				@Override
				public Parameter next() {
					curIndex++;
					return ParametersFactory.create().createParameter(domain.getBusinessObjects().get(curIndex - 1),
							parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING));
				}
			};
		}
		return null;
	}

}
