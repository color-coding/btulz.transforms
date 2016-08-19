package org.colorcoding.tools.btulz.models;

import org.colorcoding.tools.btulz.models.data.emPropertyType;

public abstract class Property implements IProperty {
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
