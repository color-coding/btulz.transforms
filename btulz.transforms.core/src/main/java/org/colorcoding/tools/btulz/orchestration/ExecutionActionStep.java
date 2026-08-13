package org.colorcoding.tools.btulz.orchestration;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class ExecutionActionStep implements IExecutionActionStep {

	private String name;

	@XmlAttribute(name = "Name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String description;

	@XmlAttribute(name = "Description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
