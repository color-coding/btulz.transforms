package org.colorcoding.tools.btulz.orchestration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SqlExecutionActionStep", namespace = Environment.NAMESPACE_BTULZ_ORCHESTRATION)
public class SqlExecutionActionStep extends ExecutionActionStep implements ISqlExecutionActionStep {

	private Statement statement;

	protected Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement value) {
		this.statement = value;
	}

	@XmlAttribute(name = "RunValue")
	private String runValue;

	public String getRunValue() {
		return runValue;
	}

	public void setRunValue(String value) {
		this.runValue = value;
	}

	@XmlAttribute(name = "Script")
	private String script;

	public String getScript() {
		return this.script;
	}

	public void setScript(String value) {
		this.script = value;
	}

	@Override
	public Object execute() throws Exception {
		Statement statement = this.getStatement();
		if (statement == null) {
			throw new SQLException("database statement is not initialized.");
		}
		Object value = null;
		ResultSet resultSet = statement.executeQuery(this.getScript());
		if (resultSet.next()) {
			value = resultSet.getString(1);
		}

		return value;
	}

	@Override
	public boolean check(Object value) {
		if (this.getRunValue() != null) {
			if (!this.getRunValue().equals(value)) {
				return false;
			}
		}
		return true;
	}

}
