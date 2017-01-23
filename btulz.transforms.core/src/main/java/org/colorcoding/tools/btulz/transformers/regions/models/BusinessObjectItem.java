package org.colorcoding.tools.btulz.transformers.regions.models;

import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.models.IBusinessObjectItems;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.models.data.emBORelation;
import org.colorcoding.tools.btulz.util.NamingRules;

public class BusinessObjectItem implements IBusinessObjectItem {

	public BusinessObjectItem(IBusinessObjectItem boItem) {
		this.boItem = boItem;
	}

	private IBusinessObjectItem boItem;

	@Override
	public String getName() {
		return this.boItem.getName();
	}

	public String getName(String type) {
		return NamingRules.format(type, this.boItem.getName());
	}

	@Override
	public void setName(String name) {
		this.boItem.setName(name);
	}

	@Override
	public String getDescription() {
		return this.boItem.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.boItem.setDescription(description);
	}

	@Override
	public String getShortName() {
		return this.boItem.getShortName();
	}

	@Override
	public void setShortName(String name) {
		this.boItem.setShortName(name);
	}

	@Override
	public String getMappedModel() {
		return this.boItem.getMappedModel();
	}

	@Override
	public void setMappedModel(String name) {
		this.boItem.setMappedModel(name);
	}

	@Override
	public void setMappedModel(IModel model) {
		this.boItem.setMappedModel(model);
	}

	@Override
	public IBusinessObjectItems getRelatedBOs() {
		return this.boItem.getRelatedBOs();
	}

	@Override
	public emBORelation getRelation() {
		return this.boItem.getRelation();
	}

	@Override
	public void setRelation(emBORelation relation) {
		this.boItem.setRelation(relation);
	}

	@Override
	public IBusinessObjectItem clone() {
		return null;
	}

}
