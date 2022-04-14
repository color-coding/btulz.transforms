package org.colorcoding.tools.btulz.orchestration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SqlExecutionActionStep", namespace = Environment.NAMESPACE_BTULZ_ORCHESTRATION)
public class SqlExecutionActionStep extends ExecutionActionStep implements ISqlExecutionActionStep {

	public final static String SCRIPT_THROW_BREAK_EXCEPTION = "THROW BREAK EXCEPTION;";

	private Statement statement;

	protected Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement value) {
		this.statement = value;
	}

	private String runValue;

	@XmlAttribute(name = "RunOnValue")
	public String getRunOnValue() {
		return runValue;
	}

	public void setRunOnValue(String value) {
		this.runValue = value;
	}

	private String script;

	// @XmlAttribute(name = "Script")
	@XmlElement(name = "Script")
	public String getScript() {
		return this.script;
	}

	public void setScript(String value) {
		if (value != null) {
			this.script = value.trim();
			if (this.script.toUpperCase().startsWith("SELECT")) {
				this.setQuery(true);
			}
		} else {
			this.script = value;
		}
	}

	private boolean isQuery;

	@XmlAttribute(name = "Query")
	public boolean isQuery() {
		return isQuery;
	}

	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}

	@Override
	public Object execute() throws Exception {
		Statement statement = this.getStatement();
		if (statement == null) {
			throw new SQLException("database statement is not initialized.");
		}
		Object value = null;
		Environment.getLogger().info(String.format("runnig step [%s].", this.getName()));
		Environment.getLogger().debug(String.format("execute sql [%s].", this.getScript()));
		if (this.getScript().indexOf(SCRIPT_THROW_BREAK_EXCEPTION) >= 0) {
			// 抛错语句
			throw new BreakException(this.getName());
		} else if (this.isQuery()) {
			// 有结果的语句
			ResultSet resultSet = statement.executeQuery(this.getScript());
			if (resultSet.next()) {
				value = resultSet.getString(1);
			}
		} else {
			// 没有结果的语句
			statement.execute(this.getScript());
		}
		return value;
	}

	@Override
	public boolean check(Object value) {
		if (this.getRunOnValue() != null) {
			if (!this.getRunOnValue().equals(value)) {
				return false;
			}
		}
		return true;
	}

}
