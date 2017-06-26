package org.colorcoding.tools.btulz.transformers.regions;

import java.util.ArrayList;
import java.util.Iterator;

import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;

/**
 * 区域-模型是否有主键
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionModelHasPrimary extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_HAS_PRIMARY";

	public RegionModelHasPrimary() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IModel model = parameters.getValue(RegionModel.REGION_PARAMETER_NAME, IModel.class);
		if (model != null) {
			ArrayList<IProperty> primaryProperty = new ArrayList<>();
			for (IProperty iProperty : model.getProperties()) {
				if (iProperty.isPrimaryKey()) {
					primaryProperty.add(iProperty);
				}
			}
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < primaryProperty.size() ? true : false;
				}

				@Override
				public Parameter next() {
					// 返回一次，没有额外变量
					curIndex = primaryProperty.size();
					return null;
				}
			};
		}
		return null;
	}

}
