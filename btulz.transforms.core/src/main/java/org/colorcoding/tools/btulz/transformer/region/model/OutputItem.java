package org.colorcoding.tools.btulz.transformer.region.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;
import org.colorcoding.tools.btulz.util.Condition;
import org.colorcoding.tools.btulz.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "OutputItem", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlRootElement(name = "OutputItem", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class OutputItem {

	@XmlElement(name = "Content")
	private String content;

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	@XmlElement(name = "Condition", type = Condition.class)
	private ArrayList<Condition> conditions;

	public final List<Condition> getConditions() {
		if (this.conditions == null) {
			this.setConditions(new ArrayList<Condition>());
		}
		return conditions;
	}

	private final void setConditions(ArrayList<Condition> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return String.format("{OutputItem %s %s}", this.getContent());
	}
}
