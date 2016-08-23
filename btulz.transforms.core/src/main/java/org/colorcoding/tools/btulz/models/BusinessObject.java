package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "BusinessObject", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class BusinessObject implements IBusinessObject {

	public boolean equals(IProperty property) {
		if (this.getName() != null && property != null && this.getName().equals(property.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object property) {
		return this.equals((IProperty) property);
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

	@XmlAttribute(name = "ShortName")
	private String shortName;

	@Override
	public String getShortName() {
		return this.shortName;
	}

	@Override
	public void setShortName(String name) {
		this.shortName = name;
	}

	@XmlAttribute(name = "MappedModel")
	private String mappedModel;

	@Override
	public String getMappedModel() {
		return this.mappedModel;
	}

	@Override
	public void setMappedModel(String name) {
		this.mappedModel = name;
	}

	@Override
	public void setMappedModel(IModel model) {
		if (model == null) {
			return;
		}
		this.mappedModel = model.getName();
		this.name = model.getName();
		this.description = model.getDescription();
	}

	@XmlElement(name = "RelatedBO", type = BusinessObjectItem.class, required = false)
	private IBusinessObjectItems relatedBOs;

	@Override
	public IBusinessObjectItems getRelatedBOs() {
		if (this.relatedBOs == null) {
			this.relatedBOs = new BusinessObjectItems();
		}
		return this.relatedBOs;
	}

}
