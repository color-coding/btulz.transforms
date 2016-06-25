package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emModelType;

public class Model implements IModel {

	private String name;

	
	public String getName() {
		return this.name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	private String shortName;

	
	public String getShortName() {
		return this.shortName;
	}

	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	private String description;

	
	public String getDescription() {
		return this.description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	private emModelType modelType;

	
	public emModelType getModelType() {
		if (this.modelType == null) {
			this.modelType = emModelType.mt_Unspecified;
		}
		return this.modelType;
	}

	
	public void setModelType(emModelType modelType) {
		this.modelType = modelType;
	}

	private IProperties properties;

	
	public IProperties getProperties() {
		if (this.properties == null) {
			this.properties = new Properties();
		}
		return this.properties;
	}

	private String mapped;

	
	public String getMapped() {
		return this.mapped;
	}

	
	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	
	public String toString() {
		return String.format("model:%s type:%s", this.getName(), this.getModelType());
	}
}
