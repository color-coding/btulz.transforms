package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Property", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlRootElement(name = "Property", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class Property implements IProperty {

	public boolean equals(IProperty property) {
		if (this.getName() != null && property != null && this.getName().equals(property.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object property) {
		return this.equals((IProperty) property);
	}

	public Property() {
		this.setDataType(emDataType.Alphanumeric);
		this.setDataSubType(emDataSubType.Default);
		this.setEditSize(8);
		this.setPrimaryKey(false);
		this.setUniqueKey(false);
	}

	@XmlAttribute(name = "Name")
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "Description")
	private String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlAttribute(name = "DeclaredType")
	private String declaredType;

	public String getDeclaredType() {
		return this.declaredType;
	}

	public void setDeclaredType(String declaredType) {
		this.declaredType = declaredType;
	}

	@XmlAttribute(name = "PrimaryKey")
	private boolean primaryKey;

	public boolean isPrimaryKey() {
		return this.primaryKey;
	}

	public void setPrimaryKey(boolean value) {
		this.primaryKey = value;
	}

	@XmlAttribute(name = "UniqueKey")
	private boolean uniqueKey;

	public boolean isUniqueKey() {
		return this.uniqueKey;
	}

	public void setUniqueKey(boolean value) {
		this.uniqueKey = value;
	}

	@XmlAttribute(name = "DataType")
	private emDataType dataType;

	public emDataType getDataType() {
		if (this.dataType == null) {
			this.dataType = emDataType.Alphanumeric;
		}
		return this.dataType;
	}

	public void setDataType(emDataType dataType) {
		this.dataType = dataType;
	}

	@XmlAttribute(name = "DataSubType")
	private emDataSubType dataSubType;

	public emDataSubType getDataSubType() {
		if (this.dataSubType == null) {
			this.dataSubType = emDataSubType.Default;
		}
		return this.dataSubType;
	}

	public void setDataSubType(emDataSubType dataSubType) {
		this.dataSubType = dataSubType;
	}

	@XmlAttribute(name = "EditSize")
	private int editSize;

	public int getEditSize() {
		return this.editSize;
	}

	public void setEditSize(int editSize) {
		this.editSize = editSize;
	}

	@XmlAttribute(name = "Mapped")
	private String mapped;

	public String getMapped() {
		return this.mapped;
	}

	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	@XmlAttribute(name = "Linked")
	private String linked;

	@Override
	public String getLinked() {
		return this.linked;
	}

	@Override
	public void setLinked(String linked) {
		this.linked = linked;
	}

	@Override
	public String toString() {
		return String.format("property:%s type:%s", this.getName(), this.getDeclaredType());
	}

	@Override
	public IProperty clone() {
		return (IProperty) Serializer.clone(this);
	}
}
