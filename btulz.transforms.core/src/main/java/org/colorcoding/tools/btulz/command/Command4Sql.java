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
	public static final int RETURN_VALUE_TRANSFORM_FAILED = 300;

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
		arguments.add(new Argument("-sqlFile", "SQL file to use"));
		arguments.add(new Argument("-sqlFilter", "SQL file name prefix when scanning a directory"));
		arguments.add(new Argument("-company", "Company tag for database object prefix"));
		arguments.add(new Argument("-dbServer", "Database server address"));
		arguments.add(new Argument("-dbPort", "Database port"));
		arguments.add(new Argument("-dbSchema", "Database schema"));
		arguments.add(new Argument("-dbName", "Database name"));
		arguments.add(new Argument("-dbUser", "Database user"));
		arguments.add(new Argument("-dbPassword", "Database user password"));
		arguments.add(new Argument("-ignore", "Ignore errors and continue with the next SQL file"));
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
		stringBuilder.append("-sqlFile=D:\\sql_mysql_ibas_initialization.xml");
		stringBuilder.append(" ");
		stringBuilder.append("-sqlFilter=sql_mysql_");
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
		stringBuilder.append("-ignore");
		super.moreHelps(stringBuilder);
	}

	@Override
	public int run(Argument[] arguments) {
		try {
			SqlTransformer sqlTransformer = new SqlTransformer();
			boolean ignore = false;
			for (Argument argument : arguments) {
				if (!argument.isInputed()) {
					// 没有输入的参数不做处理
					continue;
				}
				if (argument.getName().equalsIgnoreCase("-sqlFile")) {
					sqlTransformer.setSqlFile(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-sqlFilter")) {
					sqlTransformer.setSqlFilter(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-company")) {
					sqlTransformer.setCompany(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbServer")) {
					sqlTransformer.setDbServer(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbPort")) {
					sqlTransformer.setDbPort(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbSchema")) {
					sqlTransformer.setDbSchema(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbName")) {
					sqlTransformer.setDbName(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbUser")) {
					sqlTransformer.setDbUser(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-dbPassword")) {
					sqlTransformer.setDbPassword(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ignore")) {
					ignore = true;
				}
			}
			if (sqlTransformer != null && sqlTransformer.getSqlFile() != null) {
				// 必要参数赋值后才可运行
				sqlTransformer.setInterruptOnError(!ignore);
				sqlTransformer.transform();
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
