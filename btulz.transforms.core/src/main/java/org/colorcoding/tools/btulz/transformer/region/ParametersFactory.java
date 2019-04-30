package org.colorcoding.tools.btulz.transformer.region;

import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.transformer.region.model.BusinessObject;
import org.colorcoding.tools.btulz.transformer.region.model.BusinessObjectItem;
import org.colorcoding.tools.btulz.transformer.region.model.Domain;
import org.colorcoding.tools.btulz.transformer.region.model.Model;
import org.colorcoding.tools.btulz.transformer.region.model.Property;

public class ParametersFactory {

	private static ParametersFactory instance;

	public static ParametersFactory create() {
		if (instance == null) {
			instance = new ParametersFactory();
		}
		return instance;
	}

	/**
	 * 数据库字段映射类型
	 */
	public static final String PARAMETER_NAME_MAPPED_TYPE = "MappedType";
	/**
	 * 开发语言属性映射类型
	 */
	public static final String PARAMETER_NAME_DECLARED_TYPE = "DeclaredType";
	/**
	 * 默认值映射
	 */
	public static final String PARAMETER_NAME_DEFAULT_VALUE = "DefaultValue";
	/**
	 * 类型映射
	 */
	public static final String PARAMETER_NAME_TYPE_OUTPUT = "TypeOutput";

	private ParametersFactory() {

	}

	public Parameter createParameter(String name, Object value) {
		return new Parameter(name, value);
	}

	public Parameter createParameter(IDomain entity) {
		return this.createParameter(RegionDomain.REGION_PARAMETER_NAME, new Domain(entity));
	}

	public Parameter createParameter(IBusinessObject entity) {
		return this.createParameter(RegionBusinessObject.REGION_PARAMETER_NAME, new BusinessObject(entity));
	}

	public Parameter createParameter(IBusinessObjectItem entity) {
		return this.createParameter(RegionBusinessObjectItem.REGION_PARAMETER_NAME, entity);
	}

	public Parameter createParameter(String name, IBusinessObjectItem entity) {
		return this.createParameter(name, (Object) new BusinessObjectItem(entity));
	}

	public Parameter createParameter(IBusinessObjectItem entity, IBusinessObject parent, int index) {
		BusinessObjectItem boItem = new BusinessObjectItem(entity);
		boItem.setParent(parent);
		boItem.setIndex(index);
		return this.createParameter(RegionBusinessObjectItem.REGION_PARAMETER_NAME, (Object) boItem);
	}

	public Parameter createParameter(IModel entity) {
		return this.createParameter(RegionModel.REGION_PARAMETER_NAME, new Model(entity));
	}

	public Parameter createParameter(IProperty entity) {
		return this.createParameter(RegionProperty.REGION_PARAMETER_NAME, new Property(entity));
	}

}
