package org.colorcoding.tools.btulz.models.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 模型类型
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emModelType", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emModelType {
	/**
	 * 未指定
	 */
	Unspecified,
	/**
	 * 主数据
	 */
	MasterData,
	/**
	 * 主数据行
	 */
	MasterDataLine,
	/**
	 * 单据
	 */
	Document,
	/**
	 * 单据行
	 */
	DocumentLine,
	/**
	 * 普通
	 */
	Simple,
	/**
	 * 普通行
	 */
	SimpleLine,
}
