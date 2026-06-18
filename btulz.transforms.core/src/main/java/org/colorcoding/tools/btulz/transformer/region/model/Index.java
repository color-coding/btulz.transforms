package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IIndex;
import org.colorcoding.tools.btulz.model.IIndexProperties;
import org.colorcoding.tools.btulz.model.data.emIndexType;

public class Index extends Entity implements IIndex {

	public Index(IIndex index) {
		this.entity = index;
	}

	private IIndex entity;

	@Override
	public String getName() {
		return this.entity.getName();
	}

	@Override
	public void setName(String name) {
		this.entity.setName(name);
	}

	@Override
	public String getShortName() {
		return this.entity.getShortName();
	}

	@Override
	public void setShortName(String name) {
		this.entity.setShortName(name);
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
	public emIndexType getIndexType() {
		return this.entity.getIndexType();
	}

	@Override
	public void setIndexType(emIndexType indexType) {
		this.entity.setIndexType(indexType);
	}

	@Override
	public IIndexProperties getIndexProperties() {
		return this.entity.getIndexProperties();
	}

	@Override
	public IIndex clone() {
		return (IIndex) super.clone();
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
		return String.format("{index: %s %s}", this.getName(), this.getIndexType());
	}

}