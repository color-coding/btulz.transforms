package org.colorcoding.tools.btulz.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlType(name = "BusinessObjectItems", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlSeeAlso({ BusinessObjectItem.class })
public class BusinessObjectItems extends ArrayList<IBusinessObjectItem> implements IBusinessObjectItems {

	/**
	 * 
	 */
	private static final long serialVersionUID = 547175645190234604L;

	@Override
	public IBusinessObjectItem create() throws ClassNotFoundException {
		IBusinessObjectItem item = new BusinessObjectItem();
		if (this.add(item))
			return item;
		throw new ClassNotFoundException();
	}

	@Override
	public IBusinessObjectItem firstOrDefault() {
		if (this.size() > 0) {
			return this.get(0);
		}
		return null;
	}

}
