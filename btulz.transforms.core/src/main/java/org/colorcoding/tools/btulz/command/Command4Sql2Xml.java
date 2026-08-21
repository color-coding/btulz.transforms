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
		arguments.add(new Argument("-sqlFile", "SQL file to convert"));
		arguments.add(new Argument("-outputFile", "Output XML file"));
		arguments.add(new Argument("-dbType", "Database type, such as hana or mysql"));
		arguments.add(new Argument("-statementCount", "SQL statements per XML file"));
		arguments.add(new Argument("-company", "Company tag for database object prefix"));
		arguments.add(new Argument("-dbServer", "Database server address"));
		arguments.add(new Argument("-dbPort", "Database port"));
		arguments.add(new Argument("-dbSchema", "Database schema"));
		arguments.add(new Argument("-dbName", "Database name"));
		arguments.add(new Argument("-dbUser", "Database user"));
		arguments.add(new Argument("-dbPassword", "Database user password"));
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
