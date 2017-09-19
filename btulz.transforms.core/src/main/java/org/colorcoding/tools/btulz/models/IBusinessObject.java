package org.colorcoding.tools.btulz.models;

/**
 * 业务对象
 * 
 * @author Niuren.Zhu
 *
 */
public interface IBusinessObject {

	/**
	 * 获取-名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 设置-名称
	 * 
	 * 名称中带“conceptual”标记的自动标记为非实体
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 获取-描述
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 设置-描述
	 * 
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * 获取-短名称
	 * 
	 * @return
	 */
	String getShortName();

	/**
	 * 设置-短名称
	 * 
	 * @param name
	 */
	void setShortName(String name);

	/**
	 * 获取-映射的对象名称
	 * 
	 * @return
	 */
	String getMappedModel();

	/**
	 * 设置-映射的对象名称
	 * 
	 * @param name
	 */
	void setMappedModel(String name);

	/**
	 * 设置-映射的对象
	 * 
	 * @param model
	 */
	void setMappedModel(IModel model);

	/**
	 * 获取-关联的业务对象集合
	 * 
	 * @return
	 */
	IBusinessObjectItems getRelatedBOs();

	/**
	 * 深度克隆
	 * 
	 * @return
	 */
	IBusinessObject clone();
}
