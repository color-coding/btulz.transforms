package org.colorcoding.tools.btulz.models;

import java.util.ArrayList;

import org.colorcoding.tools.btulz.models.data.emPropertyType;

public class Properties extends ArrayList<IProperty> implements IProperties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215636759503857382L;

	@Override
	public IProperty create(emPropertyType type) throws ClassNotFoundException {
		if (type == emPropertyType.pt_Data) {
			IPropertyData item = new PropertyData();
			if (this.add(item)) {
				return item;
			}
		} else if (type == emPropertyType.pt_Model) {
			IPropertyModel item = new PropertyModel();
			if (this.add(item)) {
				return item;
			}
		} else if (type == emPropertyType.pt_Models) {
			IPropertyModels item = new PropertyModels();
			if (this.add(item)) {
				return item;
			}
		}
		throw new ClassNotFoundException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends IProperty> P create(Class<P> type) throws InstantiationException, IllegalAccessException {
		IProperty item = type.newInstance();
		this.add(item);
		return (P) item;
	}

}
