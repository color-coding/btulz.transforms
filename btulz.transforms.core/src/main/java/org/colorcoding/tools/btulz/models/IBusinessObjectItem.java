package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emBORelation;

/**
 * 业务对象子项
 * 
 * @author Niuren.Zhu
 *
 */
public interface IBusinessObjectItem extends IBusinessObject {
	/**
	 * 获取-对象关系
	 * 
	 * @return
	 */
	emBORelation getRelation();

	/**
	 * 设置-对象关系
	 * 
	 * @param relation
	 */
	void setRelation(emBORelation relation);
}
