package org.colorcoding.tools.btulz.transformer.region.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.model.IProperty;
import org.colorcoding.tools.btulz.model.data.emDataSubType;
import org.colorcoding.tools.btulz.model.data.emDataType;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Variable;
import org.colorcoding.tools.btulz.transformer.region.ParametersFactory;

/**
 * 数据类型映射
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "DataTypeMapping", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class DataTypeMapping {

	public DataTypeMapping() {

	}

	public DataTypeMapping(emDataType type, emDataSubType subType, String mapped) {
		this.setDataType(type);
		this.setSubType(subType);
		this.setMapped(mapped);
	}

	private emDataType dataType;

	@XmlAttribute(name = "DataType")
	public emDataType getDataType() {
		return dataType;
	}

	public void setDataType(emDataType DataType) {
		this.dataType = DataType;
	}

	private emDataSubType subType;

	@XmlAttribute(name = "SubType")
	public emDataSubType getSubType() {
		return subType;
	}

	public void setSubType(emDataSubType subType) {
		this.subType = subType;
	}

	private String declaredType;

	@XmlAttribute(name = "DeclaredType")
	public final String getDeclaredType() {
		return declaredType;
	}

	public final void setDeclaredType(String declaredType) {
		this.declaredType = declaredType;
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
			Parameter parameter = ParametersFactory.create().createParameter(property);
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
		return String.format("{data mapping: %s %s}", this.getDataType(), this.getMapped());
	}
}
