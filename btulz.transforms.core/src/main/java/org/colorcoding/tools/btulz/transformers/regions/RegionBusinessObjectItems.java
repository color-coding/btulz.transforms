package org.colorcoding.tools.btulz.transformers.regions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;
import org.colorcoding.tools.btulz.transformers.regions.models.BusinessObjectItem;

/**
 * 区域-业务对象模型
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionBusinessObjectItems extends RegionBase {
	/**
	 * 此区域标记
	 */
	public static final String REGION_DELIMITER = "BOITEMS";

	/**
	 * 此区域变量名称
	 */
	public static final String REGION_PARAMETER_NAME = "BO";

	public RegionBusinessObjectItems() {
		super(REGION_DELIMITER);
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters parameters) {
		IBusinessObject businessObject = parameters.getValue(RegionBusinessObject.REGION_PARAMETER_NAME,
				IBusinessObject.class);
		if (businessObject != null) {
			return new Iterator<Parameter>() {
				private List<BusinessObjectItem> getBOItems(IBusinessObject bo) {
					List<BusinessObjectItem> boItems = new ArrayList<>();
					// 当前层
					for (IBusinessObjectItem item : bo.getRelatedBOs()) {
						BusinessObjectItem boItem = new BusinessObjectItem(item);
						boItem.setParent(bo);
						boItems.add(boItem);
					}
					// 下一层
					for (IBusinessObjectItem item : bo.getRelatedBOs()) {
						boItems.addAll(this.getBOItems(item));
					}
					return boItems;
				}

				private List<BusinessObjectItem> allItems;

				protected List<BusinessObjectItem> getAllItems() {
					if (this.allItems == null) {
						this.allItems = this.getBOItems(businessObject);
						for (int i = 0; i < this.allItems.size(); i++) {
							BusinessObjectItem boItem = this.allItems.get(i);
							boItem.setIndex(i + 1);
						}
					}
					return this.allItems;
				}

				int curIndex = 0;

				@Override
				public boolean hasNext() {
					return curIndex < this.getAllItems().size() ? true : false;
				}

				@Override
				public Parameter next() {
					Parameter parameter = new Parameter();
					parameter.setName(REGION_PARAMETER_NAME);
					parameter.setValue(this.getAllItems().get(curIndex));
					curIndex++;
					return parameter;
				}
			};
		}
		return null;
	}

}
