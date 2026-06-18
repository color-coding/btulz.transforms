package org.colorcoding.tools.btulz.model;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;

@XmlType(name = "Indexes", namespace = Environment.NAMESPACE_BTULZ_MODELS)
@XmlSeeAlso({ Index.class })
public class Indexes extends ArrayList<IIndex> implements IIndexes {

	private static final long serialVersionUID = 1104036494465184047L;

	@Override
	public IIndex create() {
		IIndex index = new Index();
		if (this.add(index)) {
			return index;
		}
		return null;
	}

}