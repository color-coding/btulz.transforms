package org.colorcoding.tools.btulz.command;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformer.DsTransformer;

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
		this.setDescription("Create data structures from models");
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
		arguments.add(new Argument("-TemplateFile", "Template to use"));
		arguments.add(new Argument("-Company", "Company tag for database object prefix"));
		arguments.add(new Argument("-DbServer", "Database server address"));
		arguments.add(new Argument("-DbPort", "Database port"));
		arguments.add(new Argument("-DbSchema", "Database schema"));
		arguments.add(new Argument("-DbName", "Database name"));
		arguments.add(new Argument("-DbUser", "Database user"));
		arguments.add(new Argument("-DbPassword", "Database user password"));
		arguments.add(new Argument("-Domains", "Model directory or file to use"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用数据结构的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("Example:");
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
					// 没有输入的参数不做处理
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
