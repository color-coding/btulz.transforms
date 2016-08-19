package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emPropertyType;

public abstract class Property implements IProperty {

	public boolean equals(IProperty property) {
		if (this.getName() != null && property != null && this.getName().equals(property.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object property) {
		return this.equals((IProperty) property);
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public abstract emPropertyType getPropertyType();

	private String declaredType;

	public String getDeclaredType() {
		return this.declaredType;
	}

	public void setDeclaredType(String declaredType) {
		this.declaredType = declaredType;
	}

	public String toString() {
		return String.format("property:%s type:%s", this.getName(), this.getPropertyType());
	}
}
