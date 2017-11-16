package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperties;
import org.colorcoding.tools.btulz.model.data.emModelType;

public class Model extends Entity implements IModel {

	public Model(IModel model) {
		this.entity = model;
	}

	private IModel entity;

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
	public emModelType getModelType() {
		return this.entity.getModelType();
	}

	@Override
	public void setModelType(emModelType modelType) {
		this.entity.setModelType(modelType);
	}

	@Override
	public String getMapped() {
		return this.entity.getMapped();
	}

	@Override
	public void setMapped(String mapped) {
		this.entity.setMapped(mapped);
	}

	@Override
	public boolean isEntity() {
		return this.entity.isEntity();
	}

	@Override
	public void setEntity(boolean value) {
		this.entity.setEntity(value);
	}

	@Override
	public IModel clone() {
		return (IModel) super.clone();
	}

	@Override
	public IProperties getProperties() {
		return this.entity.getProperties();
	}

}
