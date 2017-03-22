package org.colorcoding.tools.btulz.bobas.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.bo.BusinessObject;
import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.core.RepositoryException;
import org.colorcoding.ibas.bobas.serialization.ISerializer;
import org.colorcoding.ibas.bobas.serialization.SerializerFactory;
import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.TransformException;
import org.colorcoding.tools.btulz.transformers.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 数据转换者，分析文件中数据并写入业务仓库
 * 
 * 仓库信息来着配置文件
 * 
 * @author Niuren.Zhu
 *
 */
public class DataTransformer extends Transformer {

	private List<URL> library;

	public final List<URL> getLibrary() {
		if (this.library == null) {
			this.library = new ArrayList<>();
		}
		return library;
	}

	public final void addLibrary(List<URL> library) {
		this.getLibrary().addAll(library);
	}

	public final void addLibrary(URL library) {
		this.getLibrary().add(library);
	}

	private String configFile;

	public final String getConfigFile() {
		return configFile;
	}

	public final void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	private String dataFile;

	public final String getDataFile() {
		return dataFile;
	}

	public final void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}

	private ClassLoader4bobas classLoader = null;

	protected final synchronized ClassLoader4bobas getClassLoader() throws ClassNotFoundException, IOException {
		if (this.classLoader == null) {
			URL[] urls = this.getLibrary().toArray(new URL[] {});
			// 父项类加载指向，当前的父项，以便进行隔离
			ClassLoader parentLoader = this.getClass().getClassLoader();
			this.classLoader = new ClassLoader4bobas(urls, parentLoader);
			this.classLoader.init();
		}
		return this.classLoader;
	}

	@Override
	public final void transform() throws TransformException, RepositoryException, IOException, ClassNotFoundException,
			SAXException, ParserConfigurationException, JAXBException {
		// 切换当前线程使用的classloader
		ClassLoader olderLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(this.getClassLoader());
		try {
			// 读取配置文件
			MyConfiguration.create(this.getConfigFile());
			// 获取业务对象
			File file = new File(this.getDataFile());
			if (!file.exists()) {
				throw new TransformException(String.format("data file [%s] not exists.", this.getDataFile()));
			}
			// 分析数据
			List<IBusinessObject> bos = this.analysisData(file);
			if (bos == null || bos.size() == 0) {
				Environment.getLogger().debug(String.format("no data in [%s].", file.getPath()));
				return;
			}
			Environment.getLogger().info(String.format("resolve data [%s], in [%s].", bos.size(), file.getPath()));
			// 保存数据
			this.saveDatas(bos);
		} finally {
			// 切换原始加载器
			if (olderLoader != null) {
				Thread.currentThread().setContextClassLoader(olderLoader);
			}
			if (this.classLoader != null) {
				this.classLoader.close();
				this.classLoader = null;
			}
		}
	}

	/**
	 * 分析文件中的数据
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws ClassNotFoundException
	 */
	protected List<IBusinessObject> analysisData(File file)
			throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
		ArrayList<IBusinessObject> bos = new ArrayList<>();
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				bos.addAll(this.analysisData(item));
			}
		} else if (file.isFile()) {
			ISerializer<?> serializer = SerializerFactory.create().createManager().create("xml");
			String name = file.getName().toLowerCase();
			if (name.startsWith("bo.") && name.endsWith(".xml")) {
				InputStream inputStream = new FileInputStream(file);
				String boName = this.getClassName(inputStream);
				Class<?> boType = this.getClass(boName);
				inputStream = new FileInputStream(file);
				IBusinessObject bo = (IBusinessObject) serializer.deserialize(inputStream, BusinessObject.class,
						boType);
				if (bo != null) {
					bos.add(bo);
				}
				inputStream.close();
			}
		}
		return bos;
	}

	/**
	 * 获取数据的名称（序列化标记名称）
	 * 
	 * @param stream
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	protected String getClassName(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
		String dataType = null;
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		Element element = document.getDocumentElement();
		if (element == null) {
			throw new SAXException("invaild xml data.");
		}
		String dataName = element.getNodeName().toLowerCase();
		String perfix = null;
		int index = dataName.indexOf(":");
		if (index > 0) {
			perfix = dataName.substring(0, index);
			dataName = dataName.substring(index + 1);
		}
		String xmlns = "xmlns";
		if (perfix != null) {
			xmlns = xmlns + ":" + perfix;
			for (int i = 0; i < element.getAttributes().getLength(); i++) {
				Node item = document.getDocumentElement().getAttributes().item(i);
				if (item.getNodeName().equalsIgnoreCase(xmlns)) {
					dataType = item.getNodeValue() + "/" + dataName;
					break;
				}
			}
		} else {
			dataType = dataName;
		}
		return dataType;
	}

	/**
	 * 获取数据的类型
	 * 
	 * @param name
	 *            标记的名称
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	protected Class<?> getClass(String name) throws ClassNotFoundException, IOException {
		String simpleName = name;
		if (simpleName.startsWith("http")) {
			simpleName = simpleName.substring(simpleName.lastIndexOf("/") + 1);
		}
		for (Entry<String, URL> item : this.getClassLoader().getClassesMap().entrySet()) {
			if (item.getKey().toLowerCase().endsWith(simpleName)) {
				Class<?> type = this.getClassLoader().loadClass(item.getKey());
				if (type != null) {
					XmlType xmlType = type.getAnnotation(XmlType.class);
					if (xmlType != null) {
						String xmlName = type.getSimpleName();
						if (!xmlType.name().equals("##default")) {
							xmlName = xmlType.name();
						}
						if (!xmlType.namespace().equals("##default")) {
							if (xmlType.namespace().equals("/")) {
								xmlName = xmlType.namespace() + xmlName;
							} else {
								xmlName = xmlType.namespace() + "/" + xmlName;
							}
						}
						if (xmlName.equalsIgnoreCase(name)) {
							return type;
						}
					}
				}
			}
		}
		throw new ClassNotFoundException(name);
	}

	/**
	 * 保存数据
	 * 
	 * @param datas
	 *            数据集合
	 * @throws RepositoryException
	 * @throws TransformException
	 */
	protected void saveDatas(List<IBusinessObject> datas) throws RepositoryException, TransformException {
		BORepository4Transformer boRepository = new BORepository4Transformer();
		IOperationResult<?> opRslt = null;
		try {
			boRepository.beginTransaction();// 开启事务
			for (IBusinessObject data : datas) {
				opRslt = boRepository.save(data);
				if (opRslt.getError() != null) {
					throw opRslt.getError();
				}
				if (opRslt.getResultCode() != 0) {
					throw new Exception(opRslt.getMessage());
				}
				Environment.getLogger().info(String.format("successfully saved [%s].", data.toString()));
			}
			boRepository.commitTransaction();// 提交事务
		} catch (Exception e) {
			boRepository.rollbackTransaction();// 回滚事务
			throw new TransformException(e);
		}
	}
}
