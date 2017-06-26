package org.colorcoding.tools.btulz.transformers.regions.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IProperty;
import org.colorcoding.tools.btulz.models.data.emDataSubType;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Variable;

/**
 * 数据类型映射
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "TypeValueMapping", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class TypeValueMapping {
	/**
	 * 变量名称
	 */

	public TypeValueMapping() {

	}

	public TypeValueMapping(String type, emDataSubType subType, String mapped) {
		this.setDataType(type);
		this.setMapped(mapped);
	}

	private String DataType;

	@XmlAttribute(name = "DataType")
	public String getDataType() {
		return DataType;
	}

	public void setDataType(String DataType) {
		this.DataType = DataType;
	}

	private String mapped;

	@XmlAttribute(name = "Mapped")
	public String getMapped() {
		return mapped;
	}

	public void setMapped(String mappedType) {
		this.mapped = mappedType;
	}

	public String getMapped(IProperty property) throws Exception {
		// 处理映射中的变量
		String mapValue = this.getMapped();
		Variable[] variables = Variable.discerning(this.getMapped());
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

	@Override
	public String toString() {
		return String.format("{type mapping: %s %s}", this.getMapped(), this.getDataType());
	}
}
