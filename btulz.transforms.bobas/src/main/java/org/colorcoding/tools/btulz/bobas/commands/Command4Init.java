package org.colorcoding.tools.btulz.bobas.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.ibas.bobas.bo.BusinessObject;
import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.common.IOperationResult;
import org.colorcoding.ibas.bobas.serialization.ISerializer;
import org.colorcoding.ibas.bobas.serialization.SerializerFactory;
import org.colorcoding.ibas.bobas.serialization.ValidateException;
import org.colorcoding.tools.btulz.commands.Argument;
import org.colorcoding.tools.btulz.commands.Command;
import org.colorcoding.tools.btulz.commands.Prompt;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 初始化命令 注意：
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Init.COMMAND_PROMPT)
public class Command4Init extends Command<Command4Init> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "init";

	public Command4Init() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("initialization");
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-data", "data file or data folder"));
		arguments.add(new Argument("-config", "config file"));
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
		super.moreHelps(stringBuilder);
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;
	}

	@Override
	protected int run(Argument[] arguments) {
		try {
			String argData = "";
			String argConfig = "";
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-data")) {
					argData = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-config")) {
					argConfig = argument.getValue();
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
			List<IBusinessObject> bos = this.analysis(file);
			if (bos == null || bos.size() == 0) {
				return RETURN_VALUE_NO_COMMAND_EXECUTION;
			}
			BORepository4Init boRepository = new BORepository4Init();
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
		}
	}

	private List<IBusinessObject> analysis(File file) throws Exception {
		ArrayList<IBusinessObject> bos = new ArrayList<>();
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				if (item.isFile()) {
					String name = item.getName().toLowerCase();
					InputStream inputStream = new FileInputStream(item);
					IBusinessObject bo = this.analysis(name, inputStream);
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
							InputStream inputStream = jarFile.getInputStream(jarEntry);
							IBusinessObject bo = this.analysis(name, inputStream);
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

	protected IBusinessObject analysis(String name, InputStream stream)
			throws ValidateException, IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
		if (name.indexOf("/") > 0) {
			name = name.substring(name.indexOf("/"));
		}
		if (!name.startsWith("bo")) {
			return null;
		}
		if (!name.endsWith(".xml")) {
			return null;
		}
		String boType = null;
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		for (int i = 0; i < document.getDocumentElement().getAttributes().getLength(); i++) {
			Node item = document.getDocumentElement().getAttributes().item(i);
			if (item.getNodeName().startsWith("xmlns")) {
				boType = item.getNodeValue();
			}
		}
		// 获取数据文件指向的对象类型
		if (boType == null) {
			throw new ClassNotFoundException(name);
		}
		// 获取对象类型
		Class<?> boClass = this.getKnownType(boType);
		if (boClass == null) {
			throw new ClassNotFoundException(boType);
		}
		ISerializer<?> serializer = SerializerFactory.create().createManager().create(".xml");
		return (IBusinessObject) serializer.deserialize(stream, BusinessObject.class, boClass);
	}

	protected Class<?> getKnownType(String type) {
		return null;
	}

}
