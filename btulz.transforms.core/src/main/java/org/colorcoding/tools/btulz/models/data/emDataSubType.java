package org.colorcoding.tools.btulz.models.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 枚举，数据子类型
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emDataSubType", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emDataSubType {
	/**
	 * 默认
	 */
	Default,
	/**
	 * 地址
	 */
	Address,
	/**
	 * 电话
	 */
	Phone,
	/**
	 * 日期
	 */
	Date,
	/**
	 * 时间
	 */
	Time,
	/**
	 * 率
	 */
	Rate,
	/**
	 * 总计
	 */
	Sum,
	/**
	 * 价格
	 */
	Price,
	/**
	 * 数量
	 */
	Quantity,
	/**
	 * 百分比
	 */
	Percentage,
	/**
	 * 单位数量
	 */
	Measurement,
	/**
	 * 连接
	 */
	Link,
	/**
	 * 图片
	 */
	Image,
	/**
	 * 邮箱
	 */
	Email,

}
