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

	private String driverName;

	@XmlElement(name = "DriverName")
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	private String dbUrl;

	@XmlElement(name = "DbUrl")
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	private String dbUser;

	@XmlElement(name = "DbUser")
	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	private String dbPassword;

	@XmlElement(name = "DbPassword")
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

	private boolean integrated = true;

	@Override
	@XmlElement(name = "Integrated")
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
		Environment.getLogger()
				.info(String.format("connect to [%s], by user [%s].", this.getDbUrl(), this.getDbUser()));
		Connection connection = DriverManager.getConnection(this.getDbUrl(), this.getDbUser(), this.getDbPassword());
		return connection;
	}

	@Override
	public void execute() throws Exception {
		long startTime = System.currentTimeMillis();
		Environment.getLogger().info(String.format("begin execution orchestration [%s].", this.getName()));
		Connection connection = this.createConnection();
		Statement statement = null;
		boolean myTrans = false;// 自己创建的事务
		try {
			for (ISqlExecutionAction action : this.getActions()) {
				if (action.isIsolated()) {
					// 要求独立隔离
					if (myTrans) {
						// 已经存在事务，则提交
						connection.commit();
					}
					if (statement != null) {
						// 回收数据库资源
						statement.close();
						statement = null;
					}
					connection.setAutoCommit(true);
					myTrans = false;
				} else {
					// 不要求隔离的动作
					if (this.isIntegrated() && !myTrans) {
						// 整体事务设置且尚未创建事务
						connection.setAutoCommit(false);
						myTrans = true;
					}
				}
				if (statement == null) {
					statement = connection.createStatement();
				}
				action.execute(statement);
			}
			if (myTrans) {
				connection.commit();
			}
		} catch (Exception e) {
			if (myTrans) {
				connection.rollback();
			}
			throw e;
		} finally {
		}
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		Environment.getLogger()
				.info(String.format("end execute orchestration [%s], used %s millisecond.", this.getName(), excTime));
	}

}
