package org.colorcoding.tools.btulz.model;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;

@XmlType(name = "IndexProperties", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlSeeAlso({ IndexProperty.class })
public class IndexProperties extends ArrayList<IIndexProperty> implements IIndexProperties {

	private static final long serialVersionUID = 2215636759503857383L;

	@Override
	public IIndexProperty create() {
		IIndexProperty item = new IndexProperty();
		if (this.add(item)) {
			return item;
		}
		return null;
	}

}