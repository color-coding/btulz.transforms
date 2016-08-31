package org.colorcoding.tools.btulz.transformers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.orchestration.SqlExecutionOrchestration;

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

}
