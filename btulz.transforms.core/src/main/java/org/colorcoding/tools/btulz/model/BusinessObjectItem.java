package org.colorcoding.tools.btulz.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.Serializer;
import org.colorcoding.tools.btulz.model.data.emBORelation;
import org.colorcoding.tools.btulz.model.data.emModelType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "BusinessObjectItem", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlRootElement(name = "BusinessObjectItem", namespace = Environment.NAMESPACE_BTULZ_MODELS)
public class BusinessObjectItem extends BusinessObject implements IBusinessObjectItem, Cloneable {

	@XmlAttribute(name = "Relation")
	private emBORelation relation;

	@Override
	public emBORelation getRelation() {
		return this.relation;
	}

	@Override
	public void setRelation(emBORelation relation) {
		this.relation = relation;
	}

	@Override
	public void setMappedModel(IModel model) {
		super.setMappedModel(model);
		if (model == null) {
			return;
		}
		if (model.getModelType() == emModelType.MasterData || model.getModelType() == emModelType.Document
				|| model.getModelType() == emModelType.Simple) {
			this.setRelation(emBORelation.OneToOne);
		} else if (model.getModelType() == emModelType.MasterDataLine
				|| model.getModelType() == emModelType.DocumentLine || model.getModelType() == emModelType.SimpleLine) {
			this.setRelation(emBORelation.OneToMany);
		}
	}

	@Override
	public String toString() {
		return String.format("business object item:%s", this.getName());
	}

	@Override
	public IBusinessObjectItem clone() {
		return (IBusinessObjectItem) Serializer.clone(this);
	}
}
