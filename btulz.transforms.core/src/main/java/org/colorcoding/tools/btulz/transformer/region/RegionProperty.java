package org.colorcoding.tools.btulz.transformer.region;

import java.util.Iterator;

import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.transformer.region.model.Property;

/**
 * 区域-模型属性
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionProperty extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_PROPERTY";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Property";

	public RegionProperty() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IModel model = parameters.getValue(RegionModel.REGION_PARAMETER_NAME, IModel.class);
		if (model != null) {
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < model.getProperties().size() ? true : false;
				}

				@Override
				public Parameter next() {
					Property property = new Property(model.getProperties().get(curIndex));
					property.addMappedTypeMappings(parameters.get(ParametersFactory.PARAMETER_NAME_MAPPED_TYPE));
					property.addDeclaredTypeMappings(parameters.get(ParametersFactory.PARAMETER_NAME_DECLARED_TYPE));
					property.addTypeOutputMappings(parameters.get(ParametersFactory.PARAMETER_NAME_TYPE_OUTPUT));
					if (curIndex >= model.getProperties().size() - 1) {
						property.setLast(true);
					}
					curIndex++;
					return ParametersFactory.create().createParameter(REGION_PARAMETER_NAME, property);
				}
			};
		}
		return null;
	}
}
