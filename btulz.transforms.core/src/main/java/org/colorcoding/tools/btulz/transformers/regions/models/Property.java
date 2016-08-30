package org.colorcoding.tools.btulz.transformers.regions.models;

import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.models.data.emYesNo;

public class Property implements IProperty {

	public Property(org.colorcoding.tools.btulz.models.IProperty property) {
		this.property = property;
	}

	private IProperty property;

	@Override
	public String getName() {
		return this.property.getName();
	}

	@Override
	public void setName(String name) {
		this.property.setName(name);
	}

	@Override
	public String getDescription() {
		return this.property.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.property.setDescription(description);
	}

	@Override
	public String getDeclaredType() {
		return this.property.getDeclaredType();
	}

	@Override
	public void setDeclaredType(String declaredType) {
		this.property.setDeclaredType(declaredType);
	}

	@Override
	public emYesNo isPrimaryKey() {
		return this.property.isPrimaryKey();
	}

	@Override
	public void setPrimaryKey(emYesNo value) {
		this.property.setPrimaryKey(value);
	}

	@Override
	public emYesNo isUniqueKey() {
		return this.property.isUniqueKey();
	}

	@Override
	public void setUniqueKey(emYesNo value) {
		this.property.setUniqueKey(value);
	}

	@Override
	public emDataType getDataType() {
		return this.property.getDataType();
	}

	@Override
	public void setDataType(emDataType dataType) {
		this.property.setDataType(dataType);
	}

	@Override
	public emDataSubType getDataSubType() {
		return this.property.getDataSubType();
	}

	@Override
	public void setDataSubType(emDataSubType dataSubType) {
		this.property.setDataSubType(dataSubType);
	}

	@Override
	public int getEditSize() {
		return this.property.getEditSize();
	}

	@Override
	public void setEditSize(int editSize) {
		this.property.setEditSize(editSize);
	}

	@Override
	public String getMapped() {
		return this.property.getMapped();
	}

	@Override
	public void setMapped(String mapped) {
		this.property.setMapped(mapped);
	}

	@Override
	public IProperty clone() {
		return null;
	}

	private boolean last;

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public String toString() {
		return String.format("RegionProperty %s", this.getName());
	}

	public String getMappedType() {
		return String.format("nvarchar(%s)", this.getEditSize());
	}

	public String getSeparator(String value) {
		return this.isLast() ? "" : value;
	}
}
