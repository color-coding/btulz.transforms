package org.colorcoding.tools.btulz.models;

import java.util.ArrayList;

import org.colorcoding.tools.btulz.models.data.emPropertyType;

public class Properties extends ArrayList<IProperty> implements IProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215636759503857382L;

	
	public IProperty create(emPropertyType type) {
		if (type == emPropertyType.pt_Data) {
			IPropertyData item = new PropertyData();
			if (this.add(item)) {
				return item;
			}
		} else if (type == emPropertyType.pt_Model) {
			IPropertyModel item = new PropertyModel();
			if (this.add(item)) {
				return item;
			}
		} else if (type == emPropertyType.pt_Models) {
			IPropertyModels item = new PropertyModels();
			if (this.add(item)) {
				return item;
			}
		}
		throw new RuntimeException();
	}

}
