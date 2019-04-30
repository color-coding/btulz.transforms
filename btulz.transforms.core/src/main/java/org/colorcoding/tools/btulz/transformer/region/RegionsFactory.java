package org.colorcoding.tools.btulz.transformer.region;

import org.colorcoding.tools.btulz.template.JudgmentRegion;
import org.colorcoding.tools.btulz.template.TemplateRegion;

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
			if (delimiter.indexOf(JudgmentRegion.REGION_DELIMITER) > 0) {
				return new RegionJudgment(delimiter);
			} else if (RegionModel.REGION_DELIMITER.equals(delimiter)) {
				return new RegionModel();
			} else if (RegionProperty.REGION_DELIMITER.equals(delimiter)) {
				return new RegionProperty();
			} else if (RegionPrimaryProperty.REGION_DELIMITER.equals(delimiter)) {
				return new RegionPrimaryProperty();
			} else if (RegionUniqueProperty.REGION_DELIMITER.equals(delimiter)) {
				return new RegionUniqueProperty();
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
