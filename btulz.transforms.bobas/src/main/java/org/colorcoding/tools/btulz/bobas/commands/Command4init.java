package org.colorcoding.tools.btulz.bobas.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.bo.BusinessObject;
import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.serialization.ISerializer;
import org.colorcoding.ibas.bobas.serialization.SerializerFactory;
import org.colorcoding.tools.btulz.commands.Argument;
import org.colorcoding.tools.btulz.commands.Command;
import org.colorcoding.tools.btulz.commands.Prompt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 初始化命令 注意：
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4init.COMMAND_PROMPT)
public class Command4init extends Command<Command4init> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "init";

	public Command4init() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("initialization");
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-data", "data file or data folder"));
		arguments.add(new Argument("-config", "config file"));
		arguments.add(new Argument("-classes", "library files"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用代码的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("sample");
		stringBuilder.append(NEW_LINE);
		stringBuilder.append("  ");
		stringBuilder.append(COMMAND_PROMPT);
		stringBuilder.append(" ");
		stringBuilder.append("-data=D:\\tomcat\\data\\");
		stringBuilder.append(" ");
		stringBuilder.append("-config=D:\\tomcat\\config\\app.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-classes=D:\\tomcat\\lib\\a.jar;D:\\tomcat\\lib\\b.jar;D:\\tomcat\\lib\\classes");
		super.moreHelps(stringBuilder);
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;
	}

	private ClassLoder4bobas classLoader = null;

	@Override
	protected int run(Argument[] arguments) {
		try {
			String argData = "";
			String argConfig = "";
			List<URL> argClasses = new ArrayList<>();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-data")) {
					argData = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-config")) {
					argConfig = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-classes")) {
					String[] tmps = argument.getValue().split(";");
					for (String item : tmps) {
						File file = new File(item);
						if (!file.exists()) {
							this.print("class file [%s] not exists.", item);
							continue;
						}
						argClasses.add(file.toURI().toURL());
					}
				}
			}
			// 读取配置文件
			boolean done = MyConfiguration.update(argConfig);
			if (!done) {
				throw new Exception(String.format("loading config [%s] faild.", argConfig));
			}
			// 获取业务对象
			File file = new File(argData);
			if (!file.exists()) {
				throw new Exception(String.format("data file [%s] not exists.", argData));
			}
			// 初始化classLoader
			this.classLoader = new ClassLoder4bobas(argClasses.toArray(new URL[] {}), this.getClass().getClassLoader());
			this.classLoader.init();
			List<IBusinessObject> bos = this.analysis(file);
			if (bos == null || bos.size() == 0) {
				return RETURN_VALUE_NO_COMMAND_EXECUTION;
			}
			BORepository4init boRepository = new BORepository4init();
			IOperationResult<?> opRslt = null;
			try {
				boRepository.beginTransaction();// 开启事务
				for (IBusinessObject bo : bos) {
					opRslt = boRepository.save(bo);
					if (opRslt.getError() != null) {
						throw opRslt.getError();
					}
					if (opRslt.getResultCode() != 0) {
						throw new Exception(opRslt.getMessage());
					}
					this.print("successfully saved [%s].", bo.toString());
				}
				boRepository.commitTransaction();
			} catch (Exception e) {
				boRepository.rollbackTransaction();
				throw e;
			}
			return RETURN_VALUE_SUCCESS;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_COMMAND_EXECUTION_FAILD;
		} finally {
			if (this.classLoader != null) {
				try {
					this.classLoader.close();
				} catch (IOException e) {
					this.print(e);
				}
			}
		}
	}

	private List<IBusinessObject> analysis(File file) throws Exception {
		ArrayList<IBusinessObject> bos = new ArrayList<>();
		ISerializer<?> serializer = SerializerFactory.create().createManager().create("xml");
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				if (item.isFile()) {
					String name = item.getName().toLowerCase();
					if (!name.startsWith("bo")) {
						continue;
					}
					if (!name.endsWith(".xml")) {
						continue;
					}
					InputStream inputStream = new FileInputStream(item);
					String boName = this.getBOName(inputStream);
					Class<?> boType = this.getBOType(boName);
					inputStream = new FileInputStream(item);
					IBusinessObject bo = (IBusinessObject) serializer.deserialize(inputStream, BusinessObject.class,
							boType);
					if (bo != null) {
						bos.add(bo);
					}
					inputStream.close();
				}
			}
		} else if (file.isFile()) {
			if (file.getName().toLowerCase().endsWith(".jar")) {
				JarFile jarFile = new JarFile(file);
				try {
					Enumeration<JarEntry> jarEntries = jarFile.entries();
					if (jarEntries != null) {
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
							if (jarEntry.isDirectory()) {
								continue;
							}
							String name = jarEntry.getName().toLowerCase();
							if (!name.startsWith("bo")) {
								continue;
							}
							if (!name.endsWith(".xml")) {
								continue;
							}
							InputStream inputStream = jarFile.getInputStream(jarEntry);
							String boName = this.getBOName(inputStream);
							Class<?> boType = this.getBOType(boName);
							inputStream = jarFile.getInputStream(jarEntry);
							IBusinessObject bo = (IBusinessObject) serializer.deserialize(inputStream,
									BusinessObject.class, boType);
							if (bo != null) {
								bos.add(bo);
							}
							inputStream.close();
						}
					}
				} finally {
					jarFile.close();
				}
			}
		}
		this.print("resolve bo [%s], in [%s].", bos.size(), file.getPath());
		return bos;
	}

	protected String getBOName(InputStream stream) throws SAXException, IOException, ParserConfigurationException {
		String boType = null;
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		Element element = document.getDocumentElement();
		if (element == null) {
			throw new SAXException("invaild xml data.");
		}
		String boName = element.getNodeName().toLowerCase();
		String perfix = null;
		int index = boName.indexOf(":");
		if (index > 0) {
			perfix = boName.substring(0, index);
			boName = boName.substring(index + 1);
		}
		String xmlns = "xmlns";
		if (perfix != null) {
			xmlns = xmlns + ":" + perfix;
			for (int i = 0; i < element.getAttributes().getLength(); i++) {
				Node item = document.getDocumentElement().getAttributes().item(i);
				if (item.getNodeName().equalsIgnoreCase(xmlns)) {
					boType = item.getNodeValue() + "/" + boName;
					break;
				}
			}
		} else {
			boType = boName;
		}
		return boType;
	}

	protected Class<?> getBOType(String name) throws ClassNotFoundException {
		String simpleName = name;
		if (simpleName.startsWith("http")) {
			simpleName = simpleName.substring(simpleName.lastIndexOf("/") + 1);
		}
		for (Entry<String, URL> item : this.classLoader.getClassesMap().entrySet()) {
			if (item.getKey().toLowerCase().endsWith(simpleName)) {
				Class<?> type = this.classLoader.findClass(item.getKey());
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
		return null;
	}

}
