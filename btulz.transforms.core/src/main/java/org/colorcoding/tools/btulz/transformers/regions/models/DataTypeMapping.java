package org.colorcoding.tools.btulz.transformers.regions.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.models.data.emDataType;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Variable;

/**
 * 数据类型映射
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "DataTypeMapping", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class DataTypeMapping {
	/**
	 * 变量名称
	 */
	public static final String PARAMETER_NAME = "DataTypeMapping";

	public DataTypeMapping() {

	}

	public DataTypeMapping(emDataType type, emDataSubType subType, String mapped) {
		this.setDateType(type);
		this.setSubType(subType);
		this.setMappedType(mapped);
	}

	private emDataType dateType;

	@XmlAttribute(name = "DateType")
	public emDataType getDateType() {
		return dateType;
	}

	public void setDateType(emDataType dateType) {
		this.dateType = dateType;
	}

	private emDataSubType subType;

	@XmlAttribute(name = "SubType")
	public emDataSubType getSubType() {
		return subType;
	}

	public void setSubType(emDataSubType subType) {
		this.subType = subType;
	}

	private String mappedType;

	@XmlAttribute(name = "Mapped")
	public String getMappedType() {
		return mappedType;
	}

	public void setMappedType(String mappedType) {
		this.mappedType = mappedType;
	}

	public String getMappedType(IProperty property) throws Exception {
		// 处理映射中的变量
		String mapValue = this.getMappedType();
		Variable[] variables = Variable.discerning(this.getMappedType());
		if (variables.length > 0) {
			Parameter parameter = new Parameter();
			parameter.setName("Property");
			parameter.setValue(property);
			for (Variable variable : variables) {
				Object value = parameter.getValue(variable.getValuePath());
				if (value != null) {
					mapValue = mapValue.replace(variable.getOriginal(), String.valueOf(value));
				}
			}
		}
		return mapValue;
	}
}
