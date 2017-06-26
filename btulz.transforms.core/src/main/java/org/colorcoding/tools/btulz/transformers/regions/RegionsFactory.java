package org.colorcoding.tools.btulz.transformers.regions;

import org.colorcoding.tools.btulz.templates.TemplateRegion;

/**
 * 区域工厂
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionsFactory {

	private static RegionsFactory instance;

	public static RegionsFactory create() {
		if (instance == null) {
			instance = new RegionsFactory();
		}
		return instance;
	}

	private RegionsFactory() {

	}

	public TemplateRegion createRegion(String delimiter) {
		if (delimiter != null) {
			// 修正下标识符，原始的实例$BEGIN_MODEL$
			if (delimiter.startsWith(TemplateRegion.REGION_SIGN_BEGIN)) {
				delimiter = delimiter.substring(TemplateRegion.REGION_SIGN_BEGIN.length(), delimiter.length() - 1);
			}
			if (RegionModel.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModel();
			} else if (RegionModelProperty.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModelProperty();
			} else if (RegionModelPrimaryProperty.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModelPrimaryProperty();
			} else if (RegionModelUniqueProperty.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModelUniqueProperty();
			} else if (RegionModelHasPrimary.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModelHasPrimary();
			} else if (RegionModelHasUnique.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModelHasUnique();
			} else if (RegionBusinessObject.REGION_DELIMITER.equals(delimiter)) {
				return new RegionBusinessObject();
			} else if (RegionBusinessObjectModel.REGION_DELIMITER.equals(delimiter)) {
				return new RegionBusinessObjectModel();
			} else if (RegionBusinessObjectHasItem.REGION_DELIMITER.equals(delimiter)) {
				return new RegionBusinessObjectHasItem();
			} else if (RegionBusinessObjectItem.REGION_DELIMITER.equals(delimiter)) {
				return new RegionBusinessObjectItem();
			} else if (RegionBusinessObjectItemModel.REGION_DELIMITER.equals(delimiter)) {
				return new RegionBusinessObjectItemModel();
			} else if (RegionBusinessObjectItems.REGION_DELIMITER.equals(delimiter)) {
				return new RegionBusinessObjectItems();
			}
		}
		return null;
	}
}
