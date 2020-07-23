package org.colorcoding.tools.btulz.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Condition", namespace = Environment.NAMESPACE_BTULZ_UTIL)
@XmlRootElement(name = "Condition", namespace = Environment.NAMESPACE_BTULZ_UTIL)
public class Condition {

	@XmlAttribute(name = "Relation")
	private ConditionRelation relation;

	public final ConditionRelation getRelation() {
		if (this.relation == null) {
			this.relation = ConditionRelation.AND;
		}
		return relation;
	}

	public final void setRelation(ConditionRelation relation) {
		this.relation = relation;
	}

	@XmlAttribute(name = "Property")
	private String property;

	public final String getProperty() {
		return property;
	}

	public final void setProperty(String property) {
		this.property = property;
	}

	@XmlAttribute(name = "Operation")
	private ConditionOperation operation;

	public final ConditionOperation getOperation() {
		if (this.operation == null) {
			this.operation = ConditionOperation.EQUAL;
		}
		return operation;
	}

	public final void setOperation(ConditionOperation operation) {
		this.operation = operation;
	}

	@XmlAttribute(name = "Value")
	private String value;

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute(name = "BracketOpen")
	private int bracketOpen;

	public final int getBracketOpen() {
		return bracketOpen;
	}

	public final void setBracketOpen(int bracketOpen) {
		this.bracketOpen = bracketOpen;
	}

	@XmlAttribute(name = "BracketClose")
	private int bracketClose;

	public final int getBracketClose() {
		return bracketClose;
	}

	public final void setBracketClose(int bracketClose) {
		this.bracketClose = bracketClose;
	}

	@Override
	public String toString() {
		return String.format("{Condition: %s %s %s}", this.getProperty(), this.getOperation(),
				this.getValue() == null ? "" : this.getValue());
	}
}