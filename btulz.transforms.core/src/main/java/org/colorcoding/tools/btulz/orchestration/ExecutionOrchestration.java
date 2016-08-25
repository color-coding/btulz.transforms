package org.colorcoding.tools.btulz.orchestration;

import javax.xml.bind.annotation.XmlElement;

/**
 * 执行规划实现
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class ExecutionOrchestration implements IExecutionOrchestration {

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
