package org.colorcoding.tools.btulz.shell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * 序列化对象
 */
public class Serializer {
	/**
	 * 输出字符串类型，XML
	 */
	public final static String OUT_TYPE_XML = "xml";

	/**
	 * 通过序列化与反序列化克隆对象
	 * 
	 * @param object
	 *            被克隆对象
	 * 
	 * @return 克隆的对象实例
	 * @throws JAXBException
	 */
	public static Object clone(Object object, Class<?>... types) {
		try {
			Class<?>[] knownTypes = new Class[types.length + 1];
			knownTypes[0] = object.getClass();
			for (int i = 0; i < types.length; i++) {
				knownTypes[i + 1] = types[0];
			}
			JAXBContext context = JAXBContext.newInstance(knownTypes);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);// 是否格式化生成的xml串
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			marshaller.marshal(object, outputStream);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			return unmarshaller.unmarshal(inputStream);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 格式化字符串输出
	 * 
	 * @param type
	 *            类型
	 * @param object
	 *            对象
	 * @param formated
	 *            是否缩进
	 * @return
	 * @throws JAXBException
	 */
	public static String toString(String type, Object object, boolean formated, Class<?>... types)
			throws JAXBException {
		if (OUT_TYPE_XML.equals(type)) {
			return toXmlString(object, formated, types);
		}
		return object.toString();
	}

	/**
	 * 输出字符串
	 * 
	 * @param object
	 *            对象
	 * @param formated
	 *            是否格式化
	 * @return 对象的字符串
	 * @throws JAXBException
	 */
	public static String toXmlString(Object object, boolean formated, Class<?>... types) throws JAXBException {

		Class<?>[] knownTypes = new Class[types.length + 1];
		knownTypes[0] = object.getClass();
		for (int i = 0; i < types.length; i++) {
			knownTypes[i + 1] = types[0];
		}
		JAXBContext context = JAXBContext.newInstance(knownTypes);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formated);// 是否格式化生成的xml串
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
		StringWriter writer = new StringWriter();
		marshaller.marshal(object, writer);
		return writer.toString();

	}

	/**
	 * 从xml字符形成对象
	 * 
	 * @param value
	 *            字符串
	 * @param types
	 *            相关对象
	 * @return 对象实例
	 * @throws JAXBException
	 */
	public static Object fromXmlString(String value, Class<?>... types) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(types);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(value.getBytes());
		return unmarshaller.unmarshal(inputStream);

	}

	/**
	 * 从xml字符流形成对象
	 * 
	 * @param inputStream
	 *            字符流
	 * @param types
	 *            相关对象
	 * @return 对象实例
	 * @throws JAXBException
	 */
	public static Object fromXmlString(InputStream inputStream, Class<?>... types) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(types);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller.unmarshal(inputStream);
	}
}
