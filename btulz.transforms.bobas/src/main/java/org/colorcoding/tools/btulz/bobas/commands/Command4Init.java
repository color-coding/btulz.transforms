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

/**
 * 初始化命令
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
			// 获取类目录
			File classFolder = this.getBOClassFolder(file.getParentFile());
			if (classFolder == null) {
				throw new ClassNotFoundException("unable to get class folder from data folder.");
			}
			for (File item : file.listFiles()) {
				if (item.isFile()) {
					if (item.getName().startsWith("bo.")) {
						String name = item.getName().toLowerCase();
						if (!name.startsWith("bo.")) {
							continue;
						}
						String protocol = this.getProtocol(name);
						String boType = this.getBOType(name);
						InputStream inputStream = new FileInputStream(item);
						if (inputStream != null) {
							bos.add(this.analysis(protocol, boType, inputStream));
							inputStream.close();
						}
					}
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
							if (name.startsWith("initialization/bo.")) {
								String protocol = this.getProtocol(name);
								String boType = this.getBOType(name);
								InputStream inputStream = jarFile.getInputStream(jarEntry);
								if (inputStream != null) {
									bos.add(this.analysis(protocol, boType, inputStream));
									inputStream.close();
								}
							}
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

	/**
	 * 获取类目录
	 * 
	 * @param dataFolder
	 *            数据目录
	 * @return
	 */
	protected File getBOClassFolder(File folder) {
		if (folder == null) {
			return null;
		}
		if (folder.isDirectory()) {
			if (folder.getName().equalsIgnoreCase("bo")) {
				return folder;
			}
		}
		for (File item : folder.listFiles()) {
			File tmp = this.getBOClassFolder(item);
			if (tmp != null) {
				return tmp;
			}
		}
		return null;
	}

	protected String getProtocol(String fileName) throws Exception {
		int index = fileName.lastIndexOf(".");
		if (index < fileName.length()) {
			return fileName.substring(index + 1);
		}
		throw new Exception(String.format("[%s] cannot be resolved.", fileName));
	}

	protected String getBOType(String fileName) throws Exception {
		int sIndex = fileName.indexOf(".");
		int eIndex = fileName.indexOf(".", sIndex + 1);
		if (sIndex < eIndex) {
			return fileName.substring(sIndex + 1, eIndex);
		}
		throw new Exception(String.format("[%s] cannot be resolved.", fileName));
	}

	protected IBusinessObject analysis(String protocol, String boType, InputStream stream)
			throws ValidateException, IOException {
		ISerializer serializer = SerializerFactory.create().createManager().create(protocol);
		Class<?>[] knownTypes = this.getKnownTypes(boType);
		return (IBusinessObject) serializer.deserialize(stream, BusinessObject.class, knownTypes);
	}

	protected Class<?>[] getKnownTypes(String type) {
		ArrayList<Class<?>> knownTypes = new ArrayList<>();
		return knownTypes.toArray(new Class<?>[] {});
	}

}
