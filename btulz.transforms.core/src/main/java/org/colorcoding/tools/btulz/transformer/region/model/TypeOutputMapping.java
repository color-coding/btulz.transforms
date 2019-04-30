package org.colorcoding.tools.btulz.transformer.region.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
@XmlType(name = "TypeOutputMapping", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class TypeOutputMapping {

	public TypeOutputMapping() {

	}

	public TypeOutputMapping(emDataType type, emDataSubType subType, String output) {
		this.setDataType(type);
		this.setSubType(subType);
		this.setOutput(output);
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

	private String category;

	@XmlAttribute(name = "Category")
	public final String getCategory() {
		return category;
	}

	public final void setCategory(String category) {
		this.category = category;
	}

	private String output;

	@XmlElement(name = "Output")
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getOutput(IProperty property) throws Exception {
		// 处理映射中的变量
		String outputValue = this.getOutput();
		Variable[] variables = Variable.discerning(this.getOutput());
		if (variables.length > 0) {
			Parameter parameter = ParametersFactory.create().createParameter(property);
			for (Variable variable : variables) {
				Object value = parameter.getValue(variable.getValuePath());
				if (value != null) {
					outputValue = outputValue.replace(variable.getOriginal(), String.valueOf(value));
				}
			}
		}
		return outputValue;
	}

	@Override
	public String toString() {
		return String.format("{type output: %s %s}",
				this.getDeclaredType() != null ? this.getDeclaredType() : this.getDataType(),
				(this.getOutput().substring(0, this.getOutput().length() > 30 ? 30 : this.getOutput().length()))
						.replaceAll("\\s*|\t|\r|\n", ""));
	}
}
