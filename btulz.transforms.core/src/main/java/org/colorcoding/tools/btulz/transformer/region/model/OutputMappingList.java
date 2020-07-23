package org.colorcoding.tools.btulz.transformer.region.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.util.ArrayList;

@XmlRootElement(name = "ArrayList", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlType(name = "ArrayList", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlSeeAlso({ OutputMapping.class })
public class OutputMappingList extends ArrayList<OutputMapping> {

	private static final long serialVersionUID = -2593755555586051681L;

	/**
	 * 创建数据映射集合
	 * 
	 * @param fileName 数据文件路径
	 * @return
	 * @throws JAXBException
	 */
	public static OutputMappingList create(String fileName) throws JAXBException {
		return create(new File(fileName));
	}

	/**
	 * 创建数据映射集合
	 * 
	 * @param file 数据文件
	 * @return
	 * @throws JAXBException
	 */
	public static OutputMappingList create(File file) throws JAXBException {
		try {
			return create(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 创建数据映射集合
	 * 
	 * @param inputStream 数据文件路径
	 * @return
	 * @throws JAXBException
	 */
	public static OutputMappingList create(InputStream stream) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(OutputMappingList.class, OutputMapping.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		OutputMappingList mappings = (OutputMappingList) unmarshaller.unmarshal(stream);
		return mappings;
	}

	@XmlElement(name = "OutputMapping", type = OutputMapping.class)
	private ArrayList<OutputMapping> getItems() {
		return this;
	}

	@SuppressWarnings({ "unused" })
	private void setItems(ArrayList<OutputMapping> values) {
		for (Object e : values) {
			this.add((OutputMapping) e);
		}
	}
}