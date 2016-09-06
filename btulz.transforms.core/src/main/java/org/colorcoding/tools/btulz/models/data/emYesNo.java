package org.colorcoding.tools.btulz.models.data;

import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 枚举，是否
 * 
 * @author Niuren.Zhu
 *
 */
@XmlType(name = "emYesNo", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public enum emYesNo {
	/**
	 * 否
	 */
	No,
	/**
	 * 是
	 */
	Yes;
	public static emYesNo valueOf(boolean value) {
		if (value) {
			return emYesNo.Yes;
		}
		return emYesNo.No;
	}
}
