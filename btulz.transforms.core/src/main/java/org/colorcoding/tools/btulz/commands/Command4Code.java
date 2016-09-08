package org.colorcoding.tools.btulz.commands;

import java.util.ArrayList;

/**
 * 代码创建命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Code.COMMAND_PROMPT)
public class Command4Code extends Command<Command4Code> {

	public final static String COMMAND_PROMPT = "code";

	public Command4Code() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("根据模型创建代码。");
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		arguments.add(new Argument("-TemplateFolder", "使用的模板")); // 使用的模板
		arguments.add(new Argument("-OutputFolder", "代码输出的目录")); // 输出目录
		arguments.add(new Argument("-GroupId", "组命名空间"));// 组标记
		arguments.add(new Argument("-ArtifactId", "项目命名空间"));// 项目标记
		arguments.add(new Argument("-ProjectVersion", "版本"));// 项目版本
		arguments.add(new Argument("-ProjectUrl", "项目的地址"));// 项目地址
		arguments.add(new Argument("-Domains", "使用的模型目录或文件")); // 模型文件
		arguments.add(new Argument("-Parameters", "其他参数数据，json格式字符串")); // 其他参数数据
		return arguments.toArray(new Argument[] {});
	}

	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("示例：");
		stringBuilder.append(NEW_LINE);
		stringBuilder.append("  ");
		stringBuilder.append(Command4Code.COMMAND_PROMPT); // 命令
		stringBuilder.append(" ");
		stringBuilder.append("-TemplateFolder=eclipse/ibas_classic"); // 使用的模板
		stringBuilder.append(" ");
		stringBuilder.append("-OutputFolder=D:\\temp"); // 输出目录
		stringBuilder.append(" ");
		stringBuilder.append("-GroupId=org.colorcoding");// 组标记
		stringBuilder.append(" ");
		stringBuilder.append("-ArtifactId=ibas");// 项目标记
		stringBuilder.append(" ");
		stringBuilder.append("-ProjectVersion=0.0.1");// 项目版本
		stringBuilder.append(" ");
		stringBuilder.append("-ProjectUrl=http://colorcoding.org");// 项目地址
		stringBuilder.append(" ");
		stringBuilder.append("-Domains=D:\\initialization"); // 模型文件
		stringBuilder.append(" ");
		stringBuilder.append(
				"-Parameters=[{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]"); // 其他参数
		super.moreHelps(stringBuilder);
	}

	@Override
	protected int run(Argument[] arguments) {
		// TODO Auto-generated method stub
		return 0;
	}
}
