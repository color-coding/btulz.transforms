package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emPropertyType;

public class PropertyModels extends Property implements IPropertyModels {
	private IModel model;

	
	public IModel getModel() {
		return this.model;
	}

	
	public void setModel(IModel model) {
		this.model = model;
	}

	
	public emPropertyType getPropertyType() {
		return emPropertyType.pt_Models;
	}
}
