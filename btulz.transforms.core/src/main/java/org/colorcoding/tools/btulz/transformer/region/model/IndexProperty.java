package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IIndexProperty;

public class IndexProperty extends Entity implements IIndexProperty {

	public IndexProperty(IIndexProperty indexProperty) {
		this.entity = indexProperty;
	}

	private IIndexProperty entity;

	@Override
	public String getName() {
		return this.entity.getName();
	}

	@Override
	public void setName(String name) {
		this.entity.setName(name);
	}

	@Override
	public String getDirection() {
		return this.entity.getDirection();
	}

	@Override
	public void setDirection(String direction) {
		this.entity.setDirection(direction);
	}

	private String mapped;

	public String getMapped() {
		if (this.mapped == null) {
			return this.getName();
		}
		return mapped;
	}

	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	private boolean last;

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	@Override
	public String toString() {
		return String.format("{indexProperty: %s %s}", this.getName(), this.getDirection());
	}

}