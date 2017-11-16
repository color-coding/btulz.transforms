package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IBusinessObjectItems;
import org.colorcoding.tools.btulz.model.IModel;

public class BusinessObject extends Entity implements IBusinessObject {
	public BusinessObject(IBusinessObject bo) {
		this.entity = bo;
	}

	private IBusinessObject entity;

	@Override
	public String getName() {
		return this.entity.getName();
	}

	@Override
	public void setName(String name) {
		this.entity.setName(name);
	}

	@Override
	public String getDescription() {
		return this.entity.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.entity.setDescription(description);
	}

	@Override
	public String getShortName() {
		String name = this.entity.getShortName();
		if (name == null || name.isEmpty()) {
			name = this.entity.getName().toUpperCase();
		}
		return name;
	}

	@Override
	public void setShortName(String name) {
		this.entity.setShortName(name);
	}

	@Override
	public String getMappedModel() {
		return this.entity.getMappedModel();
	}

	@Override
	public void setMappedModel(String name) {
		this.entity.setMappedModel(name);
	}

	@Override
	public void setMappedModel(IModel model) {
		this.entity.setMappedModel(model);
	}

	@Override
	public IBusinessObjectItem clone() {
		return (IBusinessObjectItem) super.clone();
	}

	@Override
	public IBusinessObjectItems getRelatedBOs() {
		return this.entity.getRelatedBOs();
	}

}
