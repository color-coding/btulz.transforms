package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;

@XmlType(name = "Properties", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlSeeAlso({ Property.class })
public class Properties extends ArrayList<IProperty> implements IProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215636759503857382L;

	@Override
	public IProperty create() {
		IProperty item = new Property();
		if (this.add(item)) {
			return item;
		}
		return null;
	}

}
