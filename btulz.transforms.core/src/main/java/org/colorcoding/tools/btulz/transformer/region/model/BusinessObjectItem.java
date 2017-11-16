package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.data.emBORelation;

public class BusinessObjectItem extends BusinessObject implements IBusinessObjectItem, Cloneable {

	public BusinessObjectItem(IBusinessObjectItem boItem) {
		super(boItem);
		this.entity = boItem;
	}

	private IBusinessObjectItem entity;

	@Override
	public String getShortName() {
		String name = this.entity.getShortName();
		if ((name == null || name.isEmpty()) && this.getParent() != null) {
			name = this.getParent().getShortName();
		}
		if (name == null || name.isEmpty()) {
			name = this.entity.getName().toUpperCase();
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
	public emBORelation getRelation() {
		return this.entity.getRelation();
	}

	@Override
	public void setRelation(emBORelation relation) {
		this.entity.setRelation(relation);
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
