package org.colorcoding.tools.btulz.model.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 枚举，索引类型
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emIndexType", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emIndexType {
	/**
	 * 非聚集索引
	 */
	NonClustered,
	/**
	 * 聚集索引
	 */
	Clustered,
	/**
	 * 唯一索引
	 */
	Unique,
	/**
	 * 唯一聚集索引
	 */
	UniqueClustered,
}