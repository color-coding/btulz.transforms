package org.colorcoding.tools.btulz.transformer.region.model;

import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.data.emDataSubType;
import org.colorcoding.tools.btulz.model.data.emDataType;

public class Property extends Entity implements IProperty {

	public Property(IProperty property) {
		this.entity = property;
	}

	private IProperty entity;

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
	public boolean isPrimaryKey() {
		return this.entity.isPrimaryKey();
	}

	@Override
	public void setPrimaryKey(boolean value) {
		this.entity.setPrimaryKey(value);
	}

	@Override
	public boolean isUniqueKey() {
		return this.entity.isUniqueKey();
	}

	@Override
	public void setUniqueKey(boolean value) {
		this.entity.setUniqueKey(value);
	}

	@Override
	public boolean isSearchKey() {
		return this.entity.isSearchKey();
	}

	@Override
	public void setSearchKey(boolean value) {
		this.entity.setSearchKey(value);
	}

	@Override
	public emDataType getDataType() {
		return this.entity.getDataType();
	}

	@Override
	public void setDataType(emDataType dataType) {
		this.entity.setDataType(dataType);
	}

	@Override
	public emDataSubType getDataSubType() {
		return this.entity.getDataSubType();
	}

	@Override
	public void setDataSubType(emDataSubType dataSubType) {
		this.entity.setDataSubType(dataSubType);
	}

	@Override
	public int getEditSize() {
		return this.entity.getEditSize();
	}

	@Override
	public void setEditSize(int editSize) {
		this.entity.setEditSize(editSize);
	}

	@Override
	public String getMapped() {
		if (this.entity.getMapped() == null) {
			return this.getName();
		}
		return this.entity.getMapped();
	}

	@Override
	public String getDefaultValue() {
		return this.entity.getDefaultValue();
	}

	@Override
	public void setDefaultValue(String value) {
		this.entity.setDefaultValue(value);
	}

	@Override
	public void setMapped(String mapped) {
		this.entity.setMapped(mapped);
	}

	@Override
	public String getLinked() {
		return this.entity.getMapped();
	}

	@Override
	public void setLinked(String linked) {
		this.entity.setMapped(linked);
	}

	@Override
	public IProperty clone() {
		return (IProperty) super.clone();
	}

	@Override
	public String getDeclaredType() {
		return this.entity.getDeclaredType();
	}

	@Override
	public void setDeclaredType(String declaredType) {
		this.entity.setDeclaredType(declaredType);
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
		return String.format("{property: %s %s}", this.getName(), this.getDataType());
	}

}
