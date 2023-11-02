package org.colorcoding.tools.btulz.transformer.region;

import java.util.ArrayList;
import java.util.Iterator;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;

/**
 * 区域-业务对象模型
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionBusinessObjectItemModel extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "BOITEM_MODEL";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = RegionModel.REGION_PARAMETER_NAME;

	public RegionBusinessObjectItemModel() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		// 根据当前业务对象在领域中查询子对象模型
		IDomain domain = parameters.getValue(RegionDomain.REGION_PARAMETER_NAME, IDomain.class);
		if (domain != null) {
			IBusinessObject bo = parameters.getValue(RegionBusinessObjectItem.REGION_PARAMETER_NAME,
					IBusinessObject.class);
			if (bo != null) {

				ArrayList<IModel> boModels = new ArrayList<>();
				for (IModel iModel : domain.getModels()) {
					if (iModel.getMapped() == null || bo.getMappedModel() == null) {
						continue;
					}
					if (iModel.getName().equals(bo.getMappedModel())) {
						boModels.add(iModel);
					}
				}
				return new Iterator<Parameter>() {
					int curIndex = 0;

					@Override
					public boolean hasNext() {
						return curIndex < 1 && boModels.size() > 0;
					}

					@Override
					public Parameter next() {
						curIndex++;
						return ParametersFactory.create().createParameter(boModels.get(curIndex - 1),
								parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING));
					}
				};
			}
		}
		return null;
	}

}
