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
	 * 类型映射
	 */
	public static final String PARAMETER_NAME_OUTPUT_MAPPING = "OutputMapping";

	private ParametersFactory() {

	}

	public Parameter createParameter(String name, Object value) {
		return new Parameter(name, value);
	}

	public Parameter createParameter(IDomain entity, Parameter... parameters) {
		Domain domain = new Domain(entity);
		if (parameters != null) {
			for (Parameter item : parameters) {
				domain.addOutputMappings(item);
			}
		}
		return this.createParameter(RegionDomain.REGION_PARAMETER_NAME, domain);
	}

	public Parameter createParameter(IBusinessObject entity, Parameter... parameters) {
		BusinessObject businessObject = new BusinessObject(entity);
		if (parameters != null) {
			for (Parameter item : parameters) {
				businessObject.addOutputMappings(item);
			}
		}
		return this.createParameter(RegionBusinessObject.REGION_PARAMETER_NAME, businessObject);
	}

	public Parameter createParameter(IBusinessObjectItem entity, IBusinessObject parent, int index,
			Parameter... parameters) {
		BusinessObjectItem boItem = new BusinessObjectItem(entity);
		boItem.setParent(parent);
		boItem.setIndex(index);
		return this.createParameter(RegionBusinessObjectItem.REGION_PARAMETER_NAME, (Object) boItem);
	}

	public Parameter createParameter(IModel entity, Parameter... parameters) {
		Model model = new Model(entity);
		if (parameters != null) {
			for (Parameter item : parameters) {
				model.addOutputMappings(item);
			}
		}
		return this.createParameter(RegionModel.REGION_PARAMETER_NAME, model);
	}

	public Parameter createParameter(IProperty entity, Parameter... parameters) {
		Property property = new Property(entity);
		if (parameters != null) {
			for (Parameter item : parameters) {
				property.addOutputMappings(item);
			}
		}
		return this.createParameter(RegionProperty.REGION_PARAMETER_NAME, property);
	}

}
