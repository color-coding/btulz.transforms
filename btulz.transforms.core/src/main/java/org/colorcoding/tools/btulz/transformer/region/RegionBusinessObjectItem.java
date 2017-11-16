package org.colorcoding.tools.btulz.transformer.region;

import java.util.Iterator;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;

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
	public static final String REGION_DELIMITER = "BOITEM";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "BOItem";

	public RegionBusinessObjectItem() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IBusinessObject businessObject = parameters.getValue(RegionBusinessObject.REGION_PARAMETER_NAME,
				IBusinessObject.class);
		if (businessObject != null) {
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < businessObject.getRelatedBOs().size() ? true : false;
				}

				@Override
				public Parameter next() {
					curIndex++;
					return ParametersFactory.create().createParameter(businessObject.getRelatedBOs().get(curIndex - 1),
							businessObject, curIndex);
				}
			};
		}
		return null;
	}

}
