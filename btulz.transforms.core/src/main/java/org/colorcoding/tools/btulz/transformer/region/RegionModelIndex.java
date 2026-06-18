package org.colorcoding.tools.btulz.transformer.region;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.model.IIndex;
import org.colorcoding.tools.btulz.model.IIndexProperty;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.IndexProperty;
import org.colorcoding.tools.btulz.model.data.emIndexType;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.transformer.region.model.Index;

/**
 * 区域-模型索引
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionModelIndex extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_INDEX";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Index";

	/**
	 * 唯一索引名称前缀
	 */
	public static final String UNIQUE_INDEX_NAME_PREFIX = "UK_";

	/**
	 * 索引名称前缀
	 */
	public static final String INDEX_NAME_PREFIX = "IX_";

	public RegionModelIndex() {
		super(REGION_DELIMITER);
	}

	/**
	 * 收集模型的索引列表（含模型自身定义的索引，以及由 UniqueKey 属性合成的虚拟唯一索引）
	 * 
	 * @param model 模型
	 * @return 索引列表
	 */
	public static List<IIndex> collectIndexes(IModel model) {
		List<IIndex> indexes = new ArrayList<>();
		if (model == null) {
			return indexes;
		}
		// 模型自身定义的索引
		for (IIndex index : model.getIndexes()) {
			if (index.getName() == null || index.getName().isEmpty()) {
				index.setName(INDEX_NAME_PREFIX + String.format("%s_%s", model.getMapped(), index.getShortName()));
			}
			indexes.add(index);
		}
		// 由 UniqueKey="Yes" 属性合成的虚拟唯一索引
		List<IProperty> uniqueProperties = new ArrayList<>();
		for (IProperty property : model.getProperties()) {
			if (property.isUniqueKey()) {
				uniqueProperties.add(property);
			}
		}
		if (!uniqueProperties.isEmpty()) {
			org.colorcoding.tools.btulz.model.Index virtual = new org.colorcoding.tools.btulz.model.Index();
			String tableName = model.getMapped();
			if (tableName == null || tableName.isEmpty()) {
				tableName = model.getName();
			}
			virtual.setName(UNIQUE_INDEX_NAME_PREFIX + (tableName != null ? tableName.toUpperCase() : ""));
			virtual.setIndexType(emIndexType.Unique);
			for (IProperty property : uniqueProperties) {
				IIndexProperty indexProperty = new IndexProperty();
				indexProperty.setName(property.getName());
				virtual.getIndexProperties().add(indexProperty);
			}
			indexes.add(virtual);
		}
		return indexes;
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IModel model = parameters.getValue(RegionModel.REGION_PARAMETER_NAME, IModel.class);
		if (model != null) {
			List<IIndex> indexes = collectIndexes(model);
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < indexes.size() ? true : false;
				}

				@Override
				public Parameter next() {
					Index index = new Index(indexes.get(curIndex));
					index.addOutputMappings(parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING));
					if (curIndex >= indexes.size() - 1) {
						index.setLast(true);
					}
					curIndex++;
					return ParametersFactory.create().createParameter(REGION_PARAMETER_NAME, index);
				}
			};
		}
		return null;
	}

}
