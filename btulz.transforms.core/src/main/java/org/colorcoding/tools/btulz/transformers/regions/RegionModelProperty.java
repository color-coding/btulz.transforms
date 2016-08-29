package org.colorcoding.tools.btulz.transformers.regions;

import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.templates.Parameter;

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

	public RegionModelProperty() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		Parameter parameter = this.getParameter(pars, RegionModel.REGION_DELIMITER);
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
								Parameter parameter = new Parameter();
								parameter.setName(REGION_DELIMITER);
								parameter.setValue(model.getProperties().get(curIndex));
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
