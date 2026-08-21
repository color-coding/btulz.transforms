package org.colorcoding.tools.btulz.command;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformer.CodeTransformer;

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
	public static final int RETURN_VALUE_TRANSFORM_FAILED = 300;

	public Command4Code() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("Generate code from models");
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
		arguments.add(new Argument("-templateFolder", "Template to use"));
		arguments.add(new Argument("-outputFolder", "Output directory for code"));
		arguments.add(new Argument("-groupId", "Group namespace"));
		arguments.add(new Argument("-artifactId", "Project namespace"));
		arguments.add(new Argument("-projectVersion", "Version"));
		arguments.add(new Argument("-projectUrl", "Project URL"));
		arguments.add(new Argument("-domains", "Model directory or file to use"));
		arguments.add(new Argument("-parameters", "Additional parameters in JSON format"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用代码的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("Example:");
		stringBuilder.append(NEW_LINE);
		stringBuilder.append("  ");
		stringBuilder.append(COMMAND_PROMPT); // 命令
		stringBuilder.append(" ");
		stringBuilder.append("-templateFolder=eclipse/ibas_classic"); // 使用的模板
		stringBuilder.append(" ");
		stringBuilder.append("-outputFolder=D:\\temp"); // 输出目录
		stringBuilder.append(" ");
		stringBuilder.append("-groupId=org.colorcoding");// 组标记
		stringBuilder.append(" ");
		stringBuilder.append("-artifactId=ibas");// 项目标记
		stringBuilder.append(" ");
		stringBuilder.append("-projectVersion=0.0.1");// 项目版本
		stringBuilder.append(" ");
		stringBuilder.append("-projectUrl=http://colorcoding.org");// 项目地址
		stringBuilder.append(" ");
		stringBuilder.append("-domains=D:\\initialization"); // 模型文件
		stringBuilder.append(" ");
		stringBuilder.append(
				"-parameters=[{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]"); // 其他参数
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
				if (argument.getName().equalsIgnoreCase("-templateFolder")) {
					codeTransformer.setTemplateFolder(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-outputFolder")) {
					codeTransformer.setOutputFolder(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-groupId")) {
					codeTransformer.setGroupId(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-artifactId")) {
					codeTransformer.setArtifactId(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-projectVersion")) {
					codeTransformer.setProjectVersion(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-projectUrl")) {
					codeTransformer.setProjectUrl(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-domains")) {
					codeTransformer.addDomains(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-parameters")) {
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
			return RETURN_VALUE_TRANSFORM_FAILED;
		}
	}

}
