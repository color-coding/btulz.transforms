package org.colorcoding.tools.btulz.transformers.regions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.templates.Parameter;

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
	public static final String REGION_DELIMITER = "BUSINESS_OBJECT_ITEM_MODEL";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "Model";

	public RegionBusinessObjectItemModel() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) {
		// 根据当前业务对象在领域中查询子对象模型
		Parameter parameter = this.getParameter(pars, RegionDomain.REGION_PARAMETER_NAME);
		if (parameter != null) {
			if (parameter.getValue() instanceof IDomain) {
				IDomain domain = (IDomain) parameter.getValue();
				parameter = this.getParameter(pars, RegionBusinessObjectItem.REGION_PARAMETER_NAME);
				if (parameter != null) {
					if (parameter.getValue() instanceof IBusinessObject) {
						IBusinessObject bo = (IBusinessObject) parameter.getValue();
						ArrayList<IModel> boModels = new ArrayList<>();
						for (IModel iModel : domain.getModels()) {
							if (iModel.getMapped() == null || bo.getMappedModel() == null) {
								continue;
							}
							if (iModel.getMapped().equals(bo.getMappedModel())) {
								boModels.add(iModel);
							}
						}
						return new Iterable<Parameter>() {
							@Override
							public Iterator<Parameter> iterator() {

								return new Iterator<Parameter>() {
									int curIndex = 0;

									@Override
									public boolean hasNext() {
										return curIndex < 1 && boModels.size() > 0;
									}

									@Override
									public Parameter next() {
										Parameter parameter = new Parameter();
										parameter.setName(REGION_PARAMETER_NAME);
										parameter.setValue(boModels.get(curIndex));
										curIndex++;
										return parameter;
									}
								};
							}

						};
					}
				}
			}
		}
		return null;
	}

}
