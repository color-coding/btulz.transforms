package org.colorcoding.tools.btulz.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlType(name = "BusinessObjects", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlSeeAlso({ BusinessObject.class })
public class BusinessObjects extends ArrayList<IBusinessObject> implements IBusinessObjects {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4473833073233202816L;

	@Override
	public IBusinessObject create() {
		IBusinessObject item = new BusinessObject();
		if (this.add(item))
			return item;
		return null;
	}

	@Override
	public IBusinessObject firstOrDefault() {
		if (this.size() > 0) {
			return this.get(0);
		}
		return null;
	}

}
