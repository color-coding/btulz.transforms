package org.colorcoding.tools.btulz.orchestration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public abstract class ExecutionAction implements IExecutionAction {

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
