package org.colorcoding.tools.btulz.models;

public class Domain implements IDomain {

	private String name;

	
	public String getName() {
		return this.name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	private String shortName;

	
	public String getShortName() {
		return this.shortName;
	}

	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	private String description;

	
	public String getDescription() {
		return this.description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	private IModels models;

	
	public IModels getModels() {
		if (this.models == null) {
			this.models = new Models();
		}
		return this.models;
	}

	
	public String toString() {
		return String.format("domain:%s", this.getName());
	}
}
