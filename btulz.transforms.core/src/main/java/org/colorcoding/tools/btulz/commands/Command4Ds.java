package org.colorcoding.tools.btulz.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.DsTransformer;

/**
 * 数据结构创建命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Ds.COMMAND_PROMPT)
public class Command4Ds extends Command4Release<Command4Code> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "ds";

	/**
	 * 返回值，300，转换错误
	 */
	public static final int RETURN_VALUE_TRANSFORM_FAILD = 300;

	public Command4Ds() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("根据模型创建数据结构");
	}

	@Override
	protected String getResourceSign(Argument releaseArgument) {
		return "ds/";// 仅释放ds文件夹下资源
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
		arguments.add(new Argument("-TemplateFile", "使用的模板"));
		arguments.add(new Argument("-Company", "公司标记，用于数据库对象前缀"));
		arguments.add(new Argument("-DbServer", "数据库地址"));
		arguments.add(new Argument("-DbPort", "数据库端口"));
		arguments.add(new Argument("-DbSchema", "应用架构"));
		arguments.add(new Argument("-DbName", "数据库名称"));
		arguments.add(new Argument("-DbUser", "数据库用户"));
		arguments.add(new Argument("-DbPassword", "数据库用户密码"));
		arguments.add(new Argument("-Domains", "使用的模型目录或文件"));
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
		stringBuilder.append("-TemplateFile=ds_mysql_ibas_classic.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-Company=CC");
		stringBuilder.append(" ");
		stringBuilder.append("-DbServer=ibas-dev-mysql");
		stringBuilder.append(" ");
		stringBuilder.append("-DbPort=3306");
		stringBuilder.append(" ");
		stringBuilder.append("-DbSchema=");
		stringBuilder.append(" ");
		stringBuilder.append("-DbName=ibas_demo");
		stringBuilder.append(" ");
		stringBuilder.append("-DbUser=root");
		stringBuilder.append(" ");
		stringBuilder.append("-DbPassword=1q2w3e");
		stringBuilder.append(" ");
		stringBuilder.append("-Domains=D:\\initialization");
		super.moreHelps(stringBuilder);
	}

	@Override
	protected int go(Argument[] arguments) {
		try {
			DsTransformer dsTransformer = new DsTransformer();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-TemplateFile")) {
					dsTransformer.setTemplateFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Company")) {
					dsTransformer.setCompany(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbServer")) {
					dsTransformer.setDbServer(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbPort")) {
					dsTransformer.setDbPort(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbSchema")) {
					dsTransformer.setDbSchema(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbName")) {
					dsTransformer.setDbName(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbUser")) {
					dsTransformer.setDbUser(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbPassword")) {
					dsTransformer.setDbPassword(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Domains")) {
					dsTransformer.addDomains(argument.getValue());
				}
			}
			if (dsTransformer != null && dsTransformer.getTemplateFile() != null) {
				// 必要参数赋值后才可运行
				dsTransformer.transform();
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
