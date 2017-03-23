package org.colorcoding.tools.btulz.commands;

import java.util.ArrayList;

import org.colorcoding.tools.btulz.transformers.SqlTransformer;

/**
 * SQL脚本执行命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Sql.COMMAND_PROMPT)
public class Command4Sql extends Command<Command4Sql> {

	/**
	 * 命令符
	 */
	public final static String COMMAND_PROMPT = "sql";

	/**
	 * 返回值，300，转换错误
	 */
	public static final int RETURN_VALUE_TRANSFORM_FAILD = 300;

	public Command4Sql() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("运行SQL脚本说明");
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;// 有参数才调用
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-SqlFile", "使用的SQL文件"));
		arguments.add(new Argument("-Company", "公司标记，用于数据库对象前缀"));
		arguments.add(new Argument("-DbServer", "数据库地址"));
		arguments.add(new Argument("-DbPort", "数据库端口"));
		arguments.add(new Argument("-DbSchema", "应用架构"));
		arguments.add(new Argument("-DbName", "数据库名称"));
		arguments.add(new Argument("-DbUser", "数据库用户"));
		arguments.add(new Argument("-DbPassword", "数据库用户密码"));
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
		stringBuilder.append("-SqlFile=D:\\sql_mysql_ibas_initialization.xml");
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
		super.moreHelps(stringBuilder);
	}

	@Override
	public int run(Argument[] arguments) {
		try {
			SqlTransformer sqlTransformer = new SqlTransformer();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输出的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-SqlFile")) {
					sqlTransformer.setSqlFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Company")) {
					sqlTransformer.setCompany(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbServer")) {
					sqlTransformer.setDbServer(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbPort")) {
					sqlTransformer.setDbPort(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbSchema")) {
					sqlTransformer.setDbSchema(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbName")) {
					sqlTransformer.setDbName(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbUser")) {
					sqlTransformer.setDbUser(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-DbPassword")) {
					sqlTransformer.setDbPassword(argument.getValue());
				}
			}
			if (sqlTransformer != null && sqlTransformer.getSqlFile() != null) {
				// 必要参数赋值后才可运行
				sqlTransformer.transform();
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
