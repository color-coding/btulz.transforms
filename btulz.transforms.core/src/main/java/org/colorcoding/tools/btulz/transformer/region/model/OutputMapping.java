package org.colorcoding.tools.btulz.transformer.region.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;
import org.colorcoding.tools.btulz.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "OutputMapping", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlRootElement(name = "OutputMapping", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class OutputMapping {

	@XmlAttribute(name = "Name")
	private String name;

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "Binding")
	private String binding;

	public final String getBinding() {
		return binding;
	}

	public final void setBinding(String binding) {
		this.binding = binding;
	}

	@XmlAttribute(name = "Unmatched")
	private UnmatchedHandling unmatched;

	public final UnmatchedHandling getUnmatched() {
		return unmatched;
	}

	public final void setUnmatched(UnmatchedHandling unmatched) {
		this.unmatched = unmatched;
	}

	@XmlElement(name = "OutputItem", type = OutputItem.class)
	private ArrayList<OutputItem> items;

	public final List<OutputItem> getItems() {
		if (this.items == null) {
			this.setItems(new ArrayList<OutputItem>());
		}
		return items;
	}

	private final void setItems(ArrayList<OutputItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return String.format("{OutputMapping %s %s}", this.getName(), this.getBinding());
	}

}
