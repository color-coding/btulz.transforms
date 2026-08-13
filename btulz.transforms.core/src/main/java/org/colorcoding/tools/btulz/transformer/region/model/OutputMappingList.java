package org.colorcoding.tools.btulz.transformer.region.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Predicate;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ArrayList", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlType(name = "ArrayList", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlSeeAlso({ OutputMapping.class })
public class OutputMappingList implements Iterable<OutputMapping> {

	@XmlElement(name = "OutputMapping", type = OutputMapping.class)
	private java.util.ArrayList<OutputMapping> items = new java.util.ArrayList<>();

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
	 * @param inputStream 数据文件流
	 * @return
	 * @throws JAXBException
	 */
	public static OutputMappingList create(InputStream stream) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(OutputMappingList.class, OutputMapping.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller.unmarshal(new javax.xml.transform.stream.StreamSource(stream), OutputMappingList.class)
				.getValue();
	}

	@Override
	public Iterator<OutputMapping> iterator() {
		return items.iterator();
	}

	public boolean add(OutputMapping value) {
		return items.add(value);
	}

	public boolean addAll(Iterable<? extends OutputMapping> values) {
		if (values != null) {
			for (OutputMapping value : values) {
				items.add(value);
			}
		}
		return true;
	}

	public int size() {
		return items.size();
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	public OutputMapping get(int index) {
		return items.get(index);
	}

	public OutputMapping firstOrDefault(Predicate<? super OutputMapping> filter) {
		for (OutputMapping value : items) {
			if (value != null && filter.test(value)) {
				return value;
			}
		}
		return null;
	}
}
