package org.colorcoding.tools.btulz.transformers.regions;

import java.util.ArrayList;
import java.util.Iterator;

import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;
import org.colorcoding.tools.btulz.transformers.regions.models.Property;

/**
 * 区域-模型唯一索引属性
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionModelUniqueProperty extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_UNIQUE_PROPERTY";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Property";

	public RegionModelUniqueProperty() {
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
					property.addMappedTypeMappings(parameters.get(Property.PARAMETER_NAME_MAPPED_TYPE));
					property.addDeclaredTypeMappings(parameters.get(Property.PARAMETER_NAME_DECLARED_TYPE));
					property.addDefaultValueMappings(parameters.get(Property.PARAMETER_NAME_DEFAULT_VALUE));
					if (curIndex >= uniqueProperty.size() - 1) {
						property.setLast(true);
					}
					Parameter parameter = new Parameter();
					parameter.setName(REGION_PARAMETER_NAME);
					parameter.setValue(property);
					curIndex++;
					return parameter;
				}
			};
		}
		return null;
	}

}
