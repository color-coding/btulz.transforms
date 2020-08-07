package org.colorcoding.tools.btulz.transformer.region;

import java.util.ArrayList;
import java.util.Iterator;

import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.transformer.region.model.Property;

/**
 * 区域-模型唯一索引属性
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionUniqueProperty extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_UNIQUE_PROPERTY";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = RegionProperty.REGION_PARAMETER_NAME;

	public RegionUniqueProperty() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IModel model = parameters.getValue(RegionModel.REGION_PARAMETER_NAME, IModel.class);
		if (model != null) {
			ArrayList<IProperty> uniqueProperty = new ArrayList<>();
			for (IProperty iProperty : model.getProperties()) {
				if (iProperty.isUniqueKey()) {
					uniqueProperty.add(iProperty);
				}
			}
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < uniqueProperty.size() ? true : false;
				}

				@Override
				public Parameter next() {
					Property property = new Property(uniqueProperty.get(curIndex));
					property.addOutputMappings(parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING));
					if (curIndex >= uniqueProperty.size() - 1) {
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
