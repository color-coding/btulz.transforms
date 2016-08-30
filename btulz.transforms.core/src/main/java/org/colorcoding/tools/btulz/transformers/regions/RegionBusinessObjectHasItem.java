package org.colorcoding.tools.btulz.transformers.regions;

import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.templates.Parameter;

/**
 * 区域-业务对象模型
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionBusinessObjectHasItem extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "BUSINESS_OBJECT_HAS_ITEM";

	public RegionBusinessObjectHasItem() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		Parameter parameter = this.getParameter(pars, RegionBusinessObject.REGION_PARAMETER_NAME);
		if (parameter != null) {
			if (parameter.getValue() instanceof IBusinessObject) {
				IBusinessObject businessObject = (IBusinessObject) parameter.getValue();
				return new Iterable<Parameter>() {
					@Override
					public Iterator<Parameter> iterator() {

						return new Iterator<Parameter>() {
							int curIndex = 0;

							@Override
							public boolean hasNext() {
								return curIndex < 1 && businessObject.getRelatedBOs().size() > 0;
							}

							@Override
							public Parameter next() {
								curIndex++;
								return null;
							}
						};
					}

				};
			}
		}
		return null;
	}

}
