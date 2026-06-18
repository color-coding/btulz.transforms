package org.colorcoding.tools.btulz.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.model.data.emIndexType;

/**
 * 索引
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Index", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlRootElement(name = "Index", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class Index implements IIndex, Cloneable {

	public boolean equals(IIndex index) {
		if (this.getName() != null && index != null && this.getName().equals(index.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IIndex) {
			return this.equals((IIndex) obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Index() {
		this.setIndexType(emIndexType.NonClustered);
		this.indexProperties = new IndexProperties();
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

	@XmlAttribute(name = "ShortName")
	private String shortName;

	@Override
	public String getShortName() {
		return this.shortName;
	}

	@Override
	public void setShortName(String name) {
		this.shortName = name;
	}

	@XmlAttribute(name = "Description")
	private String description;

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@XmlAttribute(name = "IndexType")
	private emIndexType indexType;

	@Override
	public emIndexType getIndexType() {
		if (this.indexType == null) {
			this.indexType = emIndexType.NonClustered;
		}
		return this.indexType;
	}

	@Override
	public void setIndexType(emIndexType indexType) {
		this.indexType = indexType;
	}

	@XmlElement(name = "IndexProperty", type = IndexProperty.class, required = false)
	private IIndexProperties indexProperties;

	@Override
	public IIndexProperties getIndexProperties() {
		if (this.indexProperties == null) {
			this.indexProperties = new IndexProperties();
		}
		return this.indexProperties;
	}

	@Override
	public String toString() {
		return String.format("index:%s type:%s", this.getName(), this.getIndexType());
	}

	@Override
	public IIndex clone() {
		return (IIndex) Serializer.clone(this);
	}
}