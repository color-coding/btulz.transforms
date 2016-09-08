package org.colorcoding.tools.btulz.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.colorcoding.tools.btulz.templates.Parameter;

/**
 * 命令
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class Command<C> {

	/**
	 * 帮助命令
	 */
	public static final String ARGUMENT_NAME_HELP = "-help";
	/**
	 * 新行字符
	 */
	public static final String NEW_LINE = System.getProperty("line.separator", "\r\n");

	private String name;

	public String getName() {
		if (this.name == null) {
			this.name = "btulz";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 打印消息
	 * 
	 * @param message
	 *            消息模板
	 * @param args
	 *            参数
	 */
	protected void print(String message, Object... args) {
		System.out.println(String.format("[%s]: ", this.getName()) + String.format(message, args));
	}

	/**
	 * 创建我的参数
	 * 
	 * @return
	 */
	protected abstract Argument[] createArguments();

	/**
	 * 运行
	 * 
	 * @param args
	 *            参数
	 * @return
	 */
	public final int run(String[] args) {
		Argument[] arguments = this.createArguments();
		if (arguments != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].toLowerCase().startsWith(ARGUMENT_NAME_HELP)) {
					// 包含帮助命名，打印帮助信息后退出
					this.printHelps(arguments);
					return 0;
				}
				for (Argument argument : arguments) {
					if (argument.check(args[i])) {
						// 匹配的参数
						argument.setOriginal(args[i]);
					}
				}
			}
		}
		if (arguments == null) {
			arguments = new Argument[] {};
		}
		this.print("begin to run.");
		this.run(arguments);
		this.print("end run.");
		return 0;
	}

	/**
	 * 打印帮助信息
	 * 
	 * @param arguments
	 */
	private void printHelps(Argument[] arguments) {
		StringBuilder stringBuilder = new StringBuilder();
		if (this.getDescription() != null && !this.getDescription().isEmpty())
			stringBuilder.append(this.getDescription());
		for (Argument argument : arguments) {
			stringBuilder.append(NEW_LINE);
			stringBuilder.append("    ");
			stringBuilder.append(argument.getName());
			for (int i = argument.getName().length(); i < 30; i++) {
				stringBuilder.append(" ");
			}
			stringBuilder.append(argument.getDescription());
		}
		stringBuilder.append(NEW_LINE);
		stringBuilder.append(NEW_LINE);
		this.moreHelps(stringBuilder);
		this.print(stringBuilder.toString());
	}

	/**
	 * 更多的帮助信息
	 * 
	 * @param stringBuilder
	 */
	protected void moreHelps(StringBuilder stringBuilder) {

	}

	/**
	 * 运行
	 * 
	 * @param arguments
	 *            参数
	 * @return
	 */
	protected abstract int run(Argument[] arguments);

	/**
	 * 创建参数
	 * 
	 * @param values
	 *            参数字符串
	 * @return
	 */
	protected Collection<Parameter> createParameters(String values) {
		ArrayList<Parameter> parameters = new ArrayList<>();
		if (values != null && !values.isEmpty()) {
			if (values.startsWith("[") && values.endsWith("]")) {
				ArrayList<String> tmpValues = new ArrayList<>();
				StringBuilder stringBuilder = null;
				char[] charValues = values.toCharArray();
				for (int i = 1; i < charValues.length - 1; i++) {
					if (charValues[i] == '{') {
						stringBuilder = new StringBuilder();
					}
					if (stringBuilder != null) {
						stringBuilder.append(charValues[i]);
					}
					if (charValues[i] == '}') {
						tmpValues.add(stringBuilder.toString());
						stringBuilder = null;
					}
				}
				for (String string : tmpValues) {
					Parameter parameter = Parameter.create(string);
					if (parameter != null) {
						parameters.add(parameter);
					}
				}
			} else if (values.startsWith("{") && values.endsWith("}")) {
				Parameter parameter = Parameter.create(values);
				if (parameter != null) {
					parameters.add(parameter);
				}
			}
		}
		return parameters;
	}
}
