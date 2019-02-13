package org.colorcoding.tools.btulz.bobas.transformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.bo.BusinessObject;
import org.colorcoding.ibas.bobas.bo.IBOStorageTag;
import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.ICriteria;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.core.Daemon;
import org.colorcoding.ibas.bobas.core.RepositoryException;
import org.colorcoding.ibas.bobas.serialization.ISerializer;
import org.colorcoding.ibas.bobas.serialization.SerializerFactory;
import org.colorcoding.tools.btulz.bobas.Environment;
import org.colorcoding.tools.btulz.transformer.TransformException;
import org.colorcoding.tools.btulz.transformer.Transformer;
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

	private boolean forceSave;

	/**
	 * 强制保存，不做任何检查
	 * 
	 * @return
	 */
	public final boolean isForceSave() {
		return forceSave;
	}

	public final void setForceSave(boolean forceSave) {
		this.forceSave = forceSave;
	}

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

	private ClassLoader4Transformer classLoader;

	public final ClassLoader4Transformer getClassLoader() {
		if (this.classLoader == null) {
			ClassLoader parentLoader = this.getClass().getClassLoader();
			this.classLoader = new ClassLoader4Transformer(this.getLibrary().toArray(new URL[] {}), parentLoader);
		}
		return classLoader;
	}

	public final void setClassLoader(ClassLoader4Transformer classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public final void transform() throws Exception {
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(this.getClassLoader());
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
				Environment.getLogger().info(String.format("no data in [%s].", file.getPath()));
				return;
			}
			Environment.getLogger().info(String.format("resolve data [%s], in [%s].", bos.size(), file.getPath()));
			// 保存数据
			this.saveDatas(bos);
		} finally {
			if (oldLoader != null) {
				Thread.currentThread().setContextClassLoader(oldLoader);
			}
			// 结束ibas线程
			Daemon.destory();
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
	 * @throws TransformException
	 */
	protected List<IBusinessObject> analysisData(File file)
			throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException, TransformException {
		ArrayList<IBusinessObject> bos = new ArrayList<>();
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File item : files) {
					bos.addAll(this.analysisData(item));
				}
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
	 * @param name 标记的名称
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws TransformException
	 */
	protected Class<?> getClass(String name) throws ClassNotFoundException, IOException, TransformException {
		String simpleName = name;
		if (simpleName.startsWith("http")) {
			simpleName = simpleName.substring(simpleName.lastIndexOf("/") + 1);
		}
		// 遍历加载器可识别类名称，找到可能的
		for (String className : this.getClassLoader().getClassNames()) {
			if (className.toLowerCase().endsWith(simpleName)) {
				Class<?> type = this.getClassLoader().findClass(className);
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
	 * @param datas 数据集合
	 * @throws RepositoryException
	 * @throws TransformException
	 */
	protected void saveDatas(List<IBusinessObject> datas) throws RepositoryException, TransformException {
		IBORepository4Transformer boRepository = new BORepository4Transformer();
		IOperationResult<?> opRslt = null;
		try {
			boRepository.beginTransaction();// 开启事务
			for (IBusinessObject data : datas) {
				// 标记数据
				if (data instanceof IBOStorageTag) {
					IBOStorageTag tag = (IBOStorageTag) data;
					tag.setDataSource(Environment.SIGN_DATA_SOURCE);
				}
				// 处理已存在数据
				ICriteria criteria = data.getCriteria();
				if (criteria != null && !criteria.getConditions().isEmpty()) {
					opRslt = boRepository.fetchData(criteria, data.getClass());
					if (!opRslt.getResultObjects().isEmpty()) {
						// 已存在数据
						if (this.isForceSave()) {
							// 强制保存，删除旧数据
							for (Object item : opRslt.getResultObjects()) {
								if (item instanceof IBusinessObject) {
									IBusinessObject boItem = (IBusinessObject) item;
									boItem.delete();
									Environment.getLogger()
											.info(String.format("delete exists data [%s].", boItem.toString()));
									opRslt = boRepository.saveData(boItem);
									if (opRslt.getResultCode() != 0) {
										// 保存失败
										Environment.getLogger()
												.error(String.format("delete faild [%s].", opRslt.getMessage()));
										if (this.isInterruptOnError()) {
											throw new Exception(opRslt.getMessage());
										}
									}
								}
							}
						} else {
							// 非强制保存，跳过
							continue;
						}
					}
				}
				// 开始保存正数据
				opRslt = boRepository.saveData(data);
				if (opRslt.getResultCode() != 0) {
					// 保存失败
					Environment.getLogger().error(String.format("save faild [%s].", opRslt.getMessage()));
					if (this.isInterruptOnError()) {
						throw new Exception(opRslt.getMessage());
					}
				} else {
					// 保存成功
					Environment.getLogger().info(String.format("save successfully [%s].", data.toString()));
				}
			}
			boRepository.commitTransaction();// 提交事务
		} catch (Exception e) {
			boRepository.rollbackTransaction();// 回滚事务
			throw new TransformException(e);
		}
	}

}
