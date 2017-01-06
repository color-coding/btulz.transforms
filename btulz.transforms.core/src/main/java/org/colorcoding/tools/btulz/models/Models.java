package org.colorcoding.tools.btulz.models;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;

@XmlType(name = "Models", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlSeeAlso({ Model.class })
public class Models extends ArrayList<IModel> implements IModels {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1104036494465184046L;

	public IModel create() {
		IModel model = new Model();
		if (this.add(model)) {
			return model;
		}
		return null;
	}

	@Override
	public IModel firstOrDefault() {
		if (this.size() > 0) {
			return this.get(0);
		}
		return null;
	}

}
