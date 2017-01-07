package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.Serializer;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Domain", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlRootElement(name = "Domain", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class Domain implements IDomain {

	public static final String FILE_NAME_SEPARATOR = "_";

	public boolean equals(IDomain domain) {
		if (this.getName() != null && domain != null && this.getName().equals(domain.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object model) {
		return this.equals((IDomain) model);
	}

	public Domain() {
		this.models = new Models();
		this.businessObjects = new BusinessObjects();
	}

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
		if (this.shortName == null) {
			IBusinessObject bo = this.getBusinessObjects().firstOrDefault();
			if (bo != null) {
				this.shortName = this.getShortName(bo.getShortName());
			} else {
				IModel model = this.getModels().firstOrDefault();
				if (model != null) {
					this.shortName = this.getShortName(model.getMapped());
				}
			}
		}
		return this.shortName;
	}

	private String getShortName(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		String[] tmps = name.split(FILE_NAME_SEPARATOR);
		if (tmps.length > 2) {
			String tmp = tmps[1];
			if (tmp.length() >= 1 && tmp.length() <= 5) {
				return tmp;
			}
		}
		return null;
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

	@Override
	public String toString() {
		return String.format("domain:%s", this.getName());
	}

	@Override
	public IDomain clone() {
		return (IDomain) Serializer.clone(this);
	}

	@Override
	public IDomain clone(boolean noChilds) {
		if (noChilds) {
			Domain domain = new Domain();
			domain.setName(this.getName());
			domain.setDescription(this.getDescription());
			domain.setShortName(this.getShortName());
			return domain;
		}
		return this.clone();
	}

	@Override
	public void buildMapping() {
		for (IBusinessObject bo : this.getBusinessObjects()) {
			this.buildMapping(bo);
		}
	}

	private void buildMapping(IBusinessObject bo) {
		if (bo.getMappedModel() != null) {
			for (IModel model : this.getModels()) {
				if (bo.getMappedModel().equals(model.getName())) {
					bo.setMappedModel(model);
					break;
				}
			}
		}
		for (IBusinessObject boItem : bo.getRelatedBOs()) {
			this.buildMapping(boItem);
		}
	}

}
