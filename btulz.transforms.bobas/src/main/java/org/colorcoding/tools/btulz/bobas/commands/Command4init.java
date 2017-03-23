package org.colorcoding.tools.btulz.bobas.commands;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.bobas.transformers.DataTransformer;
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
			DataTransformer transformer = new DataTransformer4Jar();
			transformer.setConfigFile(argConfig);
			transformer.setDataFile(argData);
			transformer.addLibrary(argClasses);
			transformer.transform();
			return RETURN_VALUE_SUCCESS;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_COMMAND_EXECUTION_FAILD;
		}
	}

}
