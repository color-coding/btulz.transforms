package org.colorcoding.tools.btulz.transformer.region.model;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlRootElement(name = "TypeOutputMappings", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlType(name = "TypeOutputMappings", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlSeeAlso({ TypeOutputMapping.class })
public class TypeOutputMappings extends ArrayList<TypeOutputMapping> {

	private static final long serialVersionUID = -1588608213800837521L;

	/**
	 * 创建数据映射集合
	 * 
	 * @param fileName 数据文件路径
	 * @return
	 * @throws JAXBException
	 */
	public static TypeOutputMappings create(String fileName) throws JAXBException {
		return create(new File(fileName));
	}

	/**
	 * 创建数据映射集合
	 * 
	 * @param file 数据文件
	 * @return
	 * @throws JAXBException
	 */
	public static TypeOutputMappings create(File file) throws JAXBException {
		if (file.exists() && file.isFile()) {
			JAXBContext context = JAXBContext.newInstance(TypeOutputMappings.class, TypeOutputMapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			TypeOutputMappings mappings = (TypeOutputMappings) unmarshaller.unmarshal(file);
			return mappings;
		}
		return null;
	}

	/**
	 * 创建数据映射集合
	 * 
	 * @param file 数据文件
	 * @return
	 * @throws JAXBException
	 */
	public static TypeOutputMappings create(InputStream inputStream) throws JAXBException {
		if (inputStream != null) {
			JAXBContext context = JAXBContext.newInstance(TypeOutputMappings.class, TypeOutputMapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			TypeOutputMappings mappings = (TypeOutputMappings) unmarshaller.unmarshal(inputStream);
			return mappings;
		}
		return null;
	}

	@XmlElement(name = "Mapping")
	protected TypeOutputMapping[] getValues() {
		return this.toArray(new TypeOutputMapping[] {});
	}

	protected void setValues(TypeOutputMapping[] value) {
		this.clear();
		if (value != null) {
			for (TypeOutputMapping item : value) {
				this.add(item);
			}
		}
	}

}
