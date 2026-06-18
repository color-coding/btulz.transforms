package org.colorcoding.tools.btulz.transformer.region;

import java.util.Iterator;

import org.colorcoding.tools.btulz.model.IIndex;
import org.colorcoding.tools.btulz.model.IIndexProperty;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.transformer.region.model.IndexProperty;

/**
 * 区域-索引属性
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionIndexProperty extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "INDEX_PROPERTY";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "IndexProperty";

	public RegionIndexProperty() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IIndex index = parameters.getValue(RegionModelIndex.REGION_PARAMETER_NAME, IIndex.class);
		IModel model = parameters.getValue(RegionModel.REGION_PARAMETER_NAME, IModel.class);
		if (index != null) {
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < index.getIndexProperties().size() ? true : false;
				}

				@Override
				public Parameter next() {
					IIndexProperty indexProperty = index.getIndexProperties().get(curIndex);
					IndexProperty property = new IndexProperty(indexProperty);
					for (IProperty item : model.getProperties()) {
						if (item.getName().equalsIgnoreCase(property.getName())) {
							property.setMapped(item.getMapped());
							break;
						}
					}
					property.addOutputMappings(parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING));
					if (curIndex >= index.getIndexProperties().size() - 1) {
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