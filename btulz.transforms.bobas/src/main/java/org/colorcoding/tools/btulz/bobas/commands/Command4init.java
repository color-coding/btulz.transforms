package org.colorcoding.tools.btulz.bobas.commands;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.bobas.transformers.ClassLoader4Transformer;
import org.colorcoding.tools.btulz.bobas.transformers.DataTransformer4Jar;
import org.colorcoding.tools.btulz.commands.Argument;
import org.colorcoding.tools.btulz.commands.Command;
import org.colorcoding.tools.btulz.commands.Prompt;

/**
 * 初始化命令
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
		this.setDescription("导入初始化数据");
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-data", "数据文件，支持解析jar文件"));
		arguments.add(new Argument("-config", "配置文件"));
		arguments.add(new Argument("-classes", "加载的类库，多个时用“;”分隔"));
		arguments.add(new Argument("-test", "测试"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用代码的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("示例：");
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

	@Override
	protected int run(Argument[] arguments) {
		ClassLoader4Transformer classLoader = null;
		try {
			String argData = "";
			String argConfig = "";
			List<URL> argClasses = new ArrayList<>();
			boolean test = false;
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-data")) {
					argData = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-test")) {
					test = true;
				} else if (argument.getName().equalsIgnoreCase("-config")) {
					argConfig = argument.getValue();
				} else if (argument.getName().equalsIgnoreCase("-classes")) {
					String[] tmps = argument.getValue().split(";");
					for (String item : tmps) {
						if (item == null || item.isEmpty()) {
							continue;
						}
						File file = new File(item);
						if (!file.exists()) {
							this.print("类库[%s]不存在", item);
							continue;
						}
						argClasses.add(file.toURI().toURL());
					}
				}
			}
			ClassLoader parentLoader = this.getClass().getClassLoader();
			classLoader = new ClassLoader4Transformer(this.filter(argClasses), parentLoader);
			if (test) {
				// 测试类加载器
				this.testClassLoder(classLoader);
				return RETURN_VALUE_NO_COMMAND_EXECUTION;
			}
			Class<?> dtType = classLoader.findClass(DataTransformer4Jar.class.getName());
			Environment.getLogger().debug(
					String.format("DataTransformer loaded by %s.", dtType.getClassLoader().getClass().getSimpleName()));
			Object transformer = dtType.newInstance();
			Method method = dtType.getMethod("setConfigFile", String.class);
			method.invoke(transformer, argConfig);
			method = dtType.getMethod("setDataFile", String.class);
			method.invoke(transformer, argData);
			// method = dtType.getMethod("addLibrary", List.class);
			// method.invoke(transformer, argClasses);
			method = dtType.getMethod("setClassLoader", ClassLoader4Transformer.class);
			method.invoke(transformer, classLoader);
			method = dtType.getMethod("transform");
			method.invoke(transformer);
			return RETURN_VALUE_SUCCESS;
		} catch (Exception | Error e) {
			if (e instanceof InvocationTargetException) {
				this.print(e.getCause());
			} else {
				this.print(e);
			}
			return RETURN_VALUE_COMMAND_EXECUTION_FAILD;
		} finally {
			try {
				if (classLoader != null) {
					classLoader.close();
				}
			} catch (IOException e) {
				this.print(e);
			}
		}
	}

	/**
	 * 过滤输入类库
	 * 
	 * @param args
	 * @return
	 */
	private URL[] filter(List<URL> args) {
		ArrayList<URL> urls = new ArrayList<>();
		for (URL url : args) {
			if (url.toString().endsWith(".jar")) {
				if (url.toString().indexOf("bobas.businessobjectscommon-") > 0) {
					continue;
				}
				if (url.toString().indexOf("btulz.transforms.") > 0) {
					continue;
				}
			}
			urls.add(url);
		}
		return urls.toArray(new URL[] {});
	}

	private void testClassLoder(ClassLoader4Transformer classLoader) {
		for (URL url : classLoader.getURLs()) {
			this.print("registered library %s", url.toString());
		}
		for (String className : classLoader.getClassNames()) {
			Class<?> type = null;
			this.print("test class loader %s", className);
			try {
				type = classLoader.loadClass(className);
				if (type.getClassLoader().equals(classLoader)) {
					this.print("passed.");
				} else {
					this.print("faild, %s.", type.getClassLoader());
				}
			} catch (ClassNotFoundException e) {
				this.print(e);
			}
			if (type != null && !type.isInterface() && !type.isArray() && !type.isAnnotation() && !type.isEnum()
					&& !type.isPrimitive()
					// 非抽象类型
					&& !Modifier.isAbstract(type.getModifiers())
					// public
					&& Modifier.isPublic(type.getModifiers())) {
				this.print("test new instance %s", type.getName());
				for (Constructor<?> item : type.getConstructors()) {
					if (item.getParameterCount() == 0
							// public方法
							&& Modifier.isPublic(item.getModifiers())) {
						try {
							type.newInstance();
							this.print("pass new instance.");
						} catch (InstantiationException | IllegalAccessException e) {
							this.print(e);
						}
						break;
					}
				}
			}
		}
	}

}
