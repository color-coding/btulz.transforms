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
	public static final int RETURN_VALUE_TRANSFORM_FAILED = 300;

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
		arguments.add(new Argument("-templateFile", "Template to use"));
		arguments.add(new Argument("-company", "Company tag for database object prefix"));
		arguments.add(new Argument("-dbServer", "Database server address"));
		arguments.add(new Argument("-dbPort", "Database port"));
		arguments.add(new Argument("-dbSchema", "Database schema"));
		arguments.add(new Argument("-dbName", "Database name"));
		arguments.add(new Argument("-dbUser", "Database user"));
		arguments.add(new Argument("-dbPassword", "Database user password"));
		arguments.add(new Argument("-domains", "Model directory or file to use"));
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
		stringBuilder.append("-templateFile=ds_mysql_ibas_classic.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-company=CC");
		stringBuilder.append(" ");
		stringBuilder.append("-dbServer=ibas-dev-mysql");
		stringBuilder.append(" ");
		stringBuilder.append("-dbPort=3306");
		stringBuilder.append(" ");
		stringBuilder.append("-dbSchema=");
		stringBuilder.append(" ");
		stringBuilder.append("-dbName=ibas_demo");
		stringBuilder.append(" ");
		stringBuilder.append("-dbUser=root");
		stringBuilder.append(" ");
		stringBuilder.append("-dbPassword=1q2w3e");
		stringBuilder.append(" ");
		stringBuilder.append("-domains=D:\\initialization");
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
				if (argument.getName().equalsIgnoreCase("-templateFile")) {
					dsTransformer.setTemplateFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-company")) {
					dsTransformer.setCompany(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbServer")) {
					dsTransformer.setDbServer(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbPort")) {
					dsTransformer.setDbPort(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbSchema")) {
					dsTransformer.setDbSchema(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbName")) {
					dsTransformer.setDbName(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbUser")) {
					dsTransformer.setDbUser(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbPassword")) {
					dsTransformer.setDbPassword(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-domains")) {
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
			return RETURN_VALUE_TRANSFORM_FAILED;
		}
	}

}
