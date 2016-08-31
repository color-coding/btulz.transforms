package org.colorcoding.tools.btulz.transformers;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.orchestration.SqlExecutionOrchestration;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMapping;

/**
 * 数据结构编排
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "DataStructureOrchestration", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlRootElement(name = "DataStructureOrchestration", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
public class DataStructureOrchestration extends SqlExecutionOrchestration {

	private ArrayList<DataTypeMapping> dataTypeMappings;

	@XmlElementWrapper(name = "DataTypeMappings")
	@XmlElement(name = "Mapping", type = DataTypeMapping.class)
	public ArrayList<DataTypeMapping> getDataTypeMappings() {
		return dataTypeMappings;
	}

	public void setDataTypeMappings(ArrayList<DataTypeMapping> dataTypeMappings) {
		this.dataTypeMappings = dataTypeMappings;
	}

}
