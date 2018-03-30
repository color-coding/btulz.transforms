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

@XmlRootElement(name = "DataTypeMappings", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlType(name = "DataTypeMappings", namespace = Environment.NAMESPACE_BTULZ_TRANSFORMERS)
@XmlSeeAlso({ DataTypeMapping.class })
public class DataTypeMappings extends ArrayList<DataTypeMapping> {
	/**
	 * 创建数据映射集合
	 * 
	 * @param fileName
	 *            数据文件路径
	 * @return
	 * @throws JAXBException
	 */
	public static DataTypeMappings create(String fileName) throws JAXBException {
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
	public static DataTypeMappings create(File file) throws JAXBException {
		if (file.exists() && file.isFile()) {
			JAXBContext context = JAXBContext.newInstance(DataTypeMappings.class, DataTypeMapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DataTypeMappings mappings = (DataTypeMappings) unmarshaller.unmarshal(file);
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
	public static DataTypeMappings create(InputStream inputStream) throws JAXBException {
		if (inputStream != null) {
			JAXBContext context = JAXBContext.newInstance(DataTypeMappings.class, DataTypeMapping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DataTypeMappings mappings = (DataTypeMappings) unmarshaller.unmarshal(inputStream);
			return mappings;
		}
		return null;
	}

	private static final long serialVersionUID = -424849332351253868L;

	@XmlElement(name = "Mapping")
	protected DataTypeMapping[] getValues() {
		return this.toArray(new DataTypeMapping[] {});
	}

	protected void setValues(DataTypeMapping[] value) {
		this.clear();
		if (value != null) {
			for (DataTypeMapping item : value) {
				this.add(item);
			}
		}
	}

}
