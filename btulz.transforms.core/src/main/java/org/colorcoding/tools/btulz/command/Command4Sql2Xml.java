package org.colorcoding.tools.btulz.command;

import java.util.ArrayList;

import org.colorcoding.tools.btulz.transformer.SqlFileTransformer;

/** SQL 文件转可执行 XML 命令。 */
@Prompt(Command4Sql2Xml.COMMAND_PROMPT)
public class Command4Sql2Xml extends Command<Command4Sql2Xml> {
	public static final String COMMAND_PROMPT = "sql2xml";

	public Command4Sql2Xml() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("Convert SQL file to executable XML");
	}

	@Override
	protected boolean isRequiredArguments() { return true; }

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		arguments.add(new Argument("-SqlFile", "SQL file to convert"));
		arguments.add(new Argument("-OutputFile", "Output XML file"));
		arguments.add(new Argument("-DbType", "Database type, such as hana or mysql"));
		arguments.add(new Argument("-StatementCount", "SQL statements per XML file"));
		arguments.add(new Argument("-Company", "Company tag for database object prefix"));
		arguments.add(new Argument("-DbServer", "Database server address"));
		arguments.add(new Argument("-DbPort", "Database port"));
		arguments.add(new Argument("-DbSchema", "Database schema"));
		arguments.add(new Argument("-DbName", "Database name"));
		arguments.add(new Argument("-DbUser", "Database user"));
		arguments.add(new Argument("-DbPassword", "Database user password"));
		return arguments.toArray(new Argument[] {});
	}

	@Override
	public int run(Argument[] arguments) {
		try {
			SqlFileTransformer transformer = new SqlFileTransformer();
			for (Argument argument : arguments) {
				if (!argument.isInputed()) continue;
				switch (argument.getName().toLowerCase()) {
				case "-sqlfile": transformer.setSqlFile(argument.getValue()); break;
				case "-outputfile": transformer.setOutputFile(argument.getValue()); break;
				case "-dbtype": transformer.setDbType(argument.getValue()); break;
				case "-statementcount": transformer.setStatementCount(Integer.parseInt(argument.getValue())); break;
				case "-company": transformer.setCompany(argument.getValue()); break;
				case "-dbserver": transformer.setDbServer(argument.getValue()); break;
				case "-dbport": transformer.setDbPort(argument.getValue()); break;
				case "-dbschema": transformer.setDbSchema(argument.getValue()); break;
				case "-dbname": transformer.setDbName(argument.getValue()); break;
				case "-dbuser": transformer.setDbUser(argument.getValue()); break;
				case "-dbpassword": transformer.setDbPassword(argument.getValue()); break;
				default: break;
				}
			}
			if (transformer.getSqlFile() == null) return RETURN_VALUE_NO_COMMAND_EXECUTION;
			transformer.transform();
			return RETURN_VALUE_SUCCESS;
		} catch (Exception e) {
			this.print(e);
			return RETURN_VALUE_COMMAND_EXECUTION_FAILED;
		}
	}
}
