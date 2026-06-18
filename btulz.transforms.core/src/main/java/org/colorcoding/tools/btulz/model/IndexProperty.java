package org.colorcoding.tools.btulz.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * 索引属性
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "IndexProperty", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlRootElement(name = "IndexProperty", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class IndexProperty implements IIndexProperty, Cloneable {

	public boolean equals(IIndexProperty indexProperty) {
		if (this.getName() != null && indexProperty != null && this.getName().equals(indexProperty.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IIndexProperty) {
			return this.equals((IIndexProperty) obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public IndexProperty() {
		this.setDirection("ASC");
	}

	@XmlAttribute(name = "Name")
	private String name;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "Direction")
	private String direction;

	@Override
	public String getDirection() {
		if (this.direction == null) {
			this.direction = "ASC";
		}
		return this.direction;
	}

	@Override
	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return String.format("indexProperty:%s %s", this.getName(), this.getDirection());
	}

	@Override
	public IIndexProperty clone() {
		try {
			return (IIndexProperty) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}