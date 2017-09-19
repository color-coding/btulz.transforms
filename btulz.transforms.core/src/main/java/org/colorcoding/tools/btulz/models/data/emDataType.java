package org.colorcoding.tools.btulz.models.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 枚举，数据类型
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emDataType", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emDataType {
	/**
	 * 未知
	 */
	Unknown,
	/**
	 * 字母数字
	 */
	Alphanumeric,
	/**
	 * 长字符串
	 */
	Memo,
	/**
	 * 数字
	 */
	Numeric,
	/**
	 * 日期
	 */
	Date,
	/**
	 * 小数
	 */
	Decimal,
	/**
	 * 字节
	 */
	Bytes,
}
