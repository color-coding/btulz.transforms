package org.colorcoding.tools.btulz.transformer.region;

import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.model.IIndex;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;

/**
 * 区域-模型是否有索引（含 UniqueKey 属性合成的虚拟唯一索引）
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionModelHasIndex extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "MODEL_HAS_INDEX";

	public RegionModelHasIndex() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IModel model = parameters.getValue(RegionModel.REGION_PARAMETER_NAME, IModel.class);
		if (model != null) {
			List<IIndex> indexes = RegionModelIndex.collectIndexes(model);
			return new Iterator<Parameter>() {
				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < indexes.size() ? true : false;
				}

				@Override
				public Parameter next() {
					// 返回一次，没有额外变量
					curIndex = indexes.size();
					return null;
				}
			};
		}
		return null;
	}

}
