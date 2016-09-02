package org.colorcoding.tools.btulz.transformers.regions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emYesNo;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.transformers.regions.models.Property;

/**
 * 区域-模型主键
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionModelPrimaryProperty extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_PRIMARY_PROPERTY";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Property";

	public RegionModelPrimaryProperty() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		Parameter parameter = this.getParameter(pars, RegionModel.REGION_PARAMETER_NAME);
		if (parameter != null) {
			if (parameter.getValue() instanceof IModel) {
				IModel model = (IModel) parameter.getValue();
				ArrayList<IProperty> primaryProperty = new ArrayList<>();
				for (IProperty iProperty : model.getProperties()) {
					if (iProperty.isPrimaryKey() == emYesNo.Yes) {
						primaryProperty.add(iProperty);
					}
				}
				return new Iterable<Parameter>() {
					@Override
					public Iterator<Parameter> iterator() {

						return new Iterator<Parameter>() {
							int curIndex = 0;

							@Override
							public boolean hasNext() {
								return curIndex < primaryProperty.size() ? true : false;
							}

							@Override
							public Parameter next() {
								Property property = new Property(primaryProperty.get(curIndex));
								property.addDataTypeMappings(getParameter(pars, Property.PARAMETER_NAME_MAPPED_TYPE));
								property.addDataTypeMappings(getParameter(pars, Property.PARAMETER_NAME_DECLARED_TYPE));
								if (curIndex >= primaryProperty.size() - 1) {
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

				};
			}
		}
		return null;
	}

}
