package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IBusinessObjects;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModels;

public class Domain extends Entity implements IDomain {

	public Domain(IDomain domain) {
		this.entity = domain;
	}

	private IDomain entity;

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
		return this.entity.getShortName();
	}

	@Override
	public void setShortName(String shortName) {
		this.entity.setShortName(shortName);
	}

	@Override
	public IDomain clone(boolean noChilds) {
		return this.entity.clone(noChilds);
	}

	@Override
	public IDomain clone() {
		return this.entity.clone();
	}

	@Override
	public void buildMapping() {
		this.entity.buildMapping();
	}

	@Override
	public IModels getModels() {
		return this.entity.getModels();
	}

	@Override
	public IBusinessObjects getBusinessObjects() {
		return this.entity.getBusinessObjects();
	}

}
