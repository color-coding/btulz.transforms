package org.colorcoding.tools.btulz.models.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 业务对象关系
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emBORelation", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emBORelation {

	/**
	 * 一对多
	 */
	OneToMany,
	/**
	 * 一对一
	 */
	OneToOne,
}
