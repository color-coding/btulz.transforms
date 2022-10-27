package org.colorcoding.tools.btulz.bobas.command;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.bobas.transformer.RoutingTransformer;
import org.colorcoding.tools.btulz.command.Argument;
import org.colorcoding.tools.btulz.command.Command;
import org.colorcoding.tools.btulz.command.Prompt;

/**
 * 服务路由文件命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Routing.COMMAND_PROMPT)
public class Command4Routing extends Command<Command4Routing> {

	public Command4Routing() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("查询已注册模块生成路由文件");
	}

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "routing";

	protected final static String MSG_INVAILD_ARGUMENT = "invaild argument [%s].";

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-config", "配置文件"));
		arguments.add(new Argument("-query", "查询语句（可选）"));
		arguments.add(new Argument("-dataUrl", "数据地址"));
		arguments.add(new Argument("-viewUrl", "视图地址"));
		arguments.add(new Argument("-out", "输出文件路径（可选）"));
		arguments.add(new Argument("-ignore", "忽略错误"));
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
		stringBuilder.append("-config=D:\\tomcat\\config\\app.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-dataUrl=http://localhost:8080/${ModuleName}/services/rest/data");
		stringBuilder.append(" ");
		stringBuilder.append("-viewUrl=.../${ModuleName}/");
		stringBuilder.append(" ");
		stringBuilder.append("-out=service_routing.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-ignore");
		super.moreHelps(stringBuilder);
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;
	}

	@Override
	protected int run(Argument[] arguments) {
		try {
			RoutingTransformer transformer = new RoutingTransformer();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-config")) {
					transformer.setConfigFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-query")) {
					transformer.setQuery(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dataUrl")) {
					transformer.setDataUrl(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-viewUrl")) {
					transformer.setViewUrl(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-out")) {
					transformer.setOutFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ignore")) {
					transformer.setInterruptOnError(!true);
				}
			}
			if (transformer.getQuery() == null || transformer.getQuery().isEmpty()) {
				transformer.setQuery("SELECT * FROM ${Company}_SYS_MODULE");
			}
			transformer.transform();
			return RETURN_VALUE_SUCCESS;
		} catch (Exception | Error e) {
			if (e instanceof InvocationTargetException) {
				this.print(e.getCause());
			} else {
				this.print(e);
			}
			return RETURN_VALUE_COMMAND_EXECUTION_FAILD;
		}
	}

}
