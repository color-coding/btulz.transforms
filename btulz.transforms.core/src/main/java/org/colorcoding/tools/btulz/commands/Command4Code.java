package org.colorcoding.tools.btulz.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.CodeTransformer;

/**
 * 代码创建命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Code.COMMAND_PROMPT)
public class Command4Code extends Command4Release<Command4Code> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "code";

	/**
	 * 返回值，300，转换错误
	 */
	public static final int RETURN_VALUE_TRANSFORM_FAILD = 300;

	public Command4Code() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("根据模型创建代码。");
	}

	@Override
	protected String getResourceSign(Argument releaseArgument) {
		return "code/";// 仅释放code文件夹下资源
	}

	@Override
	protected File getReleaseFolder(Argument releaseArgument) {
		return new File(Environment.getWorkingFolder());// 输出到当前工作目录
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;// 有参数才调用
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		for (Argument argument : super.createArguments()) {
			// 保留基类参数
			arguments.add(argument);
		}
		// 添加自身参数
		arguments.add(new Argument("-TemplateFolder", "使用的模板"));
		arguments.add(new Argument("-OutputFolder", "代码输出的目录"));
		arguments.add(new Argument("-GroupId", "组命名空间"));
		arguments.add(new Argument("-ArtifactId", "项目命名空间"));
		// arguments.add(new Argument("-ProjectId", "项目标记"));
		arguments.add(new Argument("-ProjectVersion", "版本"));
		arguments.add(new Argument("-ProjectUrl", "项目的地址"));
		arguments.add(new Argument("-Domains", "使用的模型目录或文件"));
		arguments.add(new Argument("-Parameters", "其他参数数据，json格式字符串"));
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
		stringBuilder.append(COMMAND_PROMPT); // 命令
		stringBuilder.append(" ");
		stringBuilder.append("-TemplateFolder=eclipse/ibas_classic"); // 使用的模板
		stringBuilder.append(" ");
		stringBuilder.append("-OutputFolder=D:\\temp"); // 输出目录
		stringBuilder.append(" ");
		stringBuilder.append("-GroupId=org.colorcoding");// 组标记
		stringBuilder.append(" ");
		stringBuilder.append("-ArtifactId=ibas");// 项目标记
		// stringBuilder.append(" ");
		// stringBuilder.append("-ProjectId=d42441ec-841e-4585-83f6-eff2a6b0ef45");//
		// 项目ID
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
	protected int go(Argument[] arguments) {
		try {
			CodeTransformer codeTransformer = null;
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (codeTransformer == null)
					codeTransformer = new CodeTransformer();
				if (argument.getName().equalsIgnoreCase("-TemplateFolder")) {
					codeTransformer.setTemplateFolder(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-OutputFolder")) {
					codeTransformer.setOutputFolder(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-GroupId")) {
					codeTransformer.setGroupId(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ArtifactId")) {
					codeTransformer.setArtifactId(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ProjectId")) {
					codeTransformer.setProjectVersion(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ProjectVersion")) {
					codeTransformer.setProjectVersion(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ProjectUrl")) {
					codeTransformer.setProjectUrl(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Domains")) {
					codeTransformer.addDomains(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Parameters")) {
					codeTransformer.addParameters(this.createParameters(argument.getValue()));
				}
			}
			if (codeTransformer != null && codeTransformer.getTemplateFolder() != null
					&& !codeTransformer.getTemplateFolder().isEmpty()) {
				// 必要参数赋值后才可运行
				codeTransformer.transform();
				return RETURN_VALUE_SUCCESS;
			}
			// 没有执行方法
			return RETURN_VALUE_NO_COMMAND_EXECUTION;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_TRANSFORM_FAILD;
		}
	}

}
