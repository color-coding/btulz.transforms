package org.colorcoding.tools.btulz.orchestration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.colorcoding.tools.btulz.Environment;

/**
 * SQL执行规划实现
 * 
 * @author Niuren.Zhu
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SqlExecutionOrchestration", namespace = Environment.NAMESPACE_BTULZ_ORCHESTRATION)
@XmlRootElement(name = "SqlExecutionOrchestration", namespace = Environment.NAMESPACE_BTULZ_ORCHESTRATION)
public class SqlExecutionOrchestration extends ExecutionOrchestration implements ISqlExecutionOrchestration {

	@XmlElement(name = "DriverName")
	private String driverName;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@XmlElement(name = "DbUrl")
	private String dbUrl;

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	@XmlElement(name = "DbUser")
	private String dbUser;

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	@XmlElement(name = "DbPassword")
	private String dbPassword;

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	// @XmlElementWrapper(name = "Actions")
	@XmlElement(name = "Action", type = SqlExecutionAction.class)
	private SqlExecutionActions actions = new SqlExecutionActions();

	@Override
	public ISqlExecutionActions getActions() {
		if (this.actions == null) {
			this.actions = new SqlExecutionActions();
		}
		return this.actions;
	}

	@XmlElement(name = "Integrated")
	private boolean integrated = true;

	@Override
	public boolean isIntegrated() {
		return this.integrated;
	}

	@Override
	public void setIntegrated(boolean value) {
		this.integrated = value;
	}

	/**
	 * 创建数据库连接
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName(this.getDriverName());
		Connection connection = DriverManager.getConnection(getDbUrl(), this.getDbUser(), this.getDbPassword());
		return connection;
	}

	@Override
	public void execute() throws Exception {
		Connection connection = this.createConnection();
		if (this.isIntegrated()) {
			// 整体事务
			connection.setAutoCommit(false);
		}
		Statement statement = connection.createStatement();
		try {
			for (ISqlExecutionAction action : this.getActions()) {
				action.execute(statement);
			}
			if (this.isIntegrated()) {
				connection.commit();
			}
		} catch (Exception e) {
			if (this.isIntegrated()) {
				connection.rollback();
			}
			throw e;
		} finally {
			if (this.isIntegrated()) {
				// 整体事务
				connection.setAutoCommit(true);
			}
		}
	}

}
