package org.colorcoding.tools.btulz.transformers.regions;

import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.transformers.regions.models.Property;

/**
 * 区域-模型属性
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionModelProperty extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_PROPERTY";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Property";

	public RegionModelProperty() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		Parameter parameter = this.getParameter(pars, RegionModel.REGION_PARAMETER_NAME);
		if (parameter != null) {
			if (parameter.getValue() instanceof IModel) {
				IModel model = (IModel) parameter.getValue();
				return new Iterable<Parameter>() {
					@Override
					public Iterator<Parameter> iterator() {

						return new Iterator<Parameter>() {
							int curIndex = 0;

							@Override
							public boolean hasNext() {
								return curIndex < model.getProperties().size() ? true : false;
							}

							@Override
							public Parameter next() {
								Property property = new Property(model.getProperties().get(curIndex));
								if (curIndex >= model.getProperties().size() - 1) {
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