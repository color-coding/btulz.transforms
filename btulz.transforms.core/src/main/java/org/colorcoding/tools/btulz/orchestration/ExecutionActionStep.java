package org.colorcoding.tools.btulz.orchestration;

import javax.xml.bind.annotation.XmlElement;

public abstract class ExecutionActionStep implements IExecutionActionStep {

	@XmlElement(name = "Name")
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "Description")
	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
