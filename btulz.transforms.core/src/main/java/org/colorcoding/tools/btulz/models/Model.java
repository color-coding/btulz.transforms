package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.data.emModelType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Model", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class Model implements IModel {

	public boolean equals(IModel model) {
		if (this.getName() != null && model != null && this.getName().equals(model.getName())) {
			return true;
		}
		return false;
	}

	public Model() {
		this.setEntity(true);

	}

	@Override
	public boolean equals(Object model) {
		return this.equals((IModel) model);
	}

	@XmlAttribute(name = "Name")
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "Description")
	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlAttribute(name = "ModelType")
	private emModelType modelType;

	public emModelType getModelType() {
		if (this.modelType == null) {
			this.modelType = emModelType.Unspecified;
		}
		return this.modelType;
	}

	public void setModelType(emModelType modelType) {
		this.modelType = modelType;
	}

	@XmlAttribute(name = "Mapped")
	private String mapped;

	public String getMapped() {
		return this.mapped;
	}

	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	@XmlAttribute(name = "Entity")
	private boolean entity;

	@Override
	public boolean isEntity() {
		return this.entity;
	}

	@Override
	public void setEntity(boolean value) {
		this.entity = value;
	}

	@XmlElement(name = "Property", type = Property.class, required = false)
	private IProperties properties;

	public IProperties getProperties() {
		if (this.properties == null) {
			this.properties = new Properties();
		}
		return this.properties;
	}

	public String toString() {
		return String.format("model:%s type:%s", this.getName(), this.getModelType());
	}

}
