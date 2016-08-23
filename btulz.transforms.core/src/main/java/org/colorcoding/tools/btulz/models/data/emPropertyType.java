package org.colorcoding.tools.btulz.models.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 属性类型
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emPropertyType", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emPropertyType {
	/**
	 * 未指定
	 */
	Unspecified,
	/**
	 * 数据类型
	 */
	Data,
	/**
	 * 模型
	 */
	Model,
	/**
	 * 模型集合
	 */
	Models,
}
