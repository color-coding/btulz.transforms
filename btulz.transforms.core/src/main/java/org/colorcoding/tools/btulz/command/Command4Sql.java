package org.colorcoding.tools.btulz.command;

import java.util.ArrayList;

import org.colorcoding.tools.btulz.transformer.SqlTransformer;

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
		this.setDescription("Run SQL scripts");
	}

	@Override
	protected boolean isRequiredArguments() {
		return true;// 有参数才调用
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		// 添加自身参数
		arguments.add(new Argument("-SqlFile", "SQL file to use"));
		arguments.add(new Argument("-Company", "Company tag for database object prefix"));
		arguments.add(new Argument("-DbServer", "Database server address"));
		arguments.add(new Argument("-DbPort", "Database port"));
		arguments.add(new Argument("-DbSchema", "Database schema"));
		arguments.add(new Argument("-DbName", "Database name"));
		arguments.add(new Argument("-DbUser", "Database user"));
		arguments.add(new Argument("-DbPassword", "Database user password"));
		return arguments.toArray(new Argument[] {});
	}

	/**
	 * 为帮助添加调用SQL脚本的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("Example:");
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
					// 没有输入的参数不做处理
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
