package org.colorcoding.tools.btulz.transformers.regions;

import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.templates.Parameter;

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
	public static final String REGION_DELIMITER = "BUSINESS_OBJECT";

	public RegionBusinessObject() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		Parameter parameter = this.getParameter(pars, RegionDomain.REGION_DELIMITER);
		if (parameter != null) {
			if (parameter.getValue() instanceof IDomain) {
				IDomain domain = (IDomain) parameter.getValue();
				return new Iterable<Parameter>() {
					@Override
					public Iterator<Parameter> iterator() {

						return new Iterator<Parameter>() {
							int curIndex = 0;

							@Override
							public boolean hasNext() {
								return curIndex < domain.getBusinessObjects().size() ? true : false;
							}

							@Override
							public Parameter next() {
								Parameter parameter = new Parameter();
								parameter.setName(REGION_DELIMITER);
								parameter.setValue(domain.getBusinessObjects().get(curIndex));
								curIndex++;
								return parameter;
							}
						};
					}

				};
			}
		}
		return null;
	}

}
