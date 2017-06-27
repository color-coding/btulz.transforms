package org.colorcoding.tools.btulz.transformers.regions.models;

import org.colorcoding.tools.btulz.models.IBusinessObject;
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
		String name = this.boItem.getShortName();
		if ((name == null || name.isEmpty()) && this.getParent() != null) {
			name = this.getParent().getShortName();
		}
		if (name == null || name.isEmpty()) {
			name = this.boItem.getName().toUpperCase();
		}
		return name;
	}

	public String getShortName(String type) {
		if (type != null && type.equalsIgnoreCase("index")) {
			if (this.getParent() instanceof BusinessObjectItem) {
				BusinessObjectItem parent = (BusinessObjectItem) this.getParent();
				return String.format("%s.%s", parent.getShortName("index"), this.getIndex());
			}
			return String.format("%s.%s", this.getShortName(), this.getIndex());
		}
		return this.getShortName();
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

	private int index;

	public final int getIndex() {
		return index;
	}

	public final void setIndex(int index) {
		this.index = index;
	}

	private IBusinessObject parent;

	public final IBusinessObject getParent() {
		return this.parent;
	}

	public final void setParent(IBusinessObject parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return String.format("{bo item: %s %s}", this.getName(), this.getRelation());
	}
}
