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

@XmlRootElement(name = "TypeValueMappings", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlType(name = "TypeValueMappings", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlSeeAlso({ TypeValueMapping.class })
public class TypeValueMappings extends ArrayList<TypeValueMapping> {

	private static final long serialVersionUID = -1588608213800837521L;

	/**
	 * 创建数据映射集合
	 * 
	 * @param fileName
	 *            数据文件路径
	 * @return
	 * @throws JAXBException
	 */
	public static TypeValueMappings create(String fileName) throws JAXBException {
		return create(new File(fileName));
	}

	/**
	 * 创建数据映射集合
	 * 
	 * @param file
	 *            数据文件
	 * @return
	 * @throws JAXBException
	 */
	public static TypeValueMappings create(File file) throws JAXBException {
		if (file.exists() && file.isFile()) {
			JAXBContext context = JAXBContext.newInstance(TypeValueMappings.class, TypeValueMapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			TypeValueMappings mappings = (TypeValueMappings) unmarshaller.unmarshal(file);
			return mappings;
		}
		return null;
	}

	/**
	 * 创建数据映射集合
	 * 
	 * @param file
	 *            数据文件
	 * @return
	 * @throws JAXBException
	 */
	public static TypeValueMappings create(InputStream inputStream) throws JAXBException {
		if (inputStream != null) {
			JAXBContext context = JAXBContext.newInstance(TypeValueMappings.class, TypeValueMapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			TypeValueMappings mappings = (TypeValueMappings) unmarshaller.unmarshal(inputStream);
			return mappings;
		}
		return null;
	}

	@XmlElement(name = "Mapping")
	protected TypeValueMapping[] getValues() {
		return this.toArray(new TypeValueMapping[] {});
	}

	protected void setValues(TypeValueMapping[] value) {
		this.clear();
		if (value != null) {
			for (TypeValueMapping item : value) {
				this.add(item);
			}
		}
	}

}
