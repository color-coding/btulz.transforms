package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Domain", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlRootElement(name = "Domain", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class Domain implements IDomain {

	@XmlAttribute(name = "Name")
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "ShortName")
	private String shortName;

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@XmlAttribute(name = "Description")
	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "Model", type = Model.class, required = false)
	private IModels models;

	public IModels getModels() {
		if (this.models == null) {
			this.models = new Models();
		}
		return this.models;
	}

	@XmlElement(name = "BusinessObject", type = BusinessObject.class, required = false)
	private IBusinessObjects businessObjects;

	public IBusinessObjects getBusinessObjects() {
		if (this.businessObjects == null) {
			this.businessObjects = new BusinessObjects();
		}
		return this.businessObjects;
	}

	public String toString() {
		return String.format("domain:%s", this.getName());
	}
}
