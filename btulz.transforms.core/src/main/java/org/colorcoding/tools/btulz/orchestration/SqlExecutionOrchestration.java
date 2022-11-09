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
import org.colorcoding.tools.btulz.util.URIEncoder;

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

	private boolean encodeDbUrl = false;

	@XmlElement(name = "EncodeDbUrl")
	public boolean isEncodeDbUrl() {
		return encodeDbUrl;
	}

	public void setEncodeDbUrl(boolean encodeDbUrl) {
		this.encodeDbUrl = encodeDbUrl;
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

	/**
	 * 创建数据库连接
	 * 
	 * @param dbUrl 数据地址，为null时取默认
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected Connection createConnection(String dbUrl) throws ClassNotFoundException, SQLException {
		Class.forName(this.getDriverName());
		if (dbUrl == null || dbUrl.isEmpty()) {
			dbUrl = this.getDbUrl();
		}
		if (this.isEncodeDbUrl()) {
			dbUrl = URIEncoder.encodeURIParameters(dbUrl);
		}
		Environment.getLogger().info(String.format("connect to [%s], by user [%s].", dbUrl, this.getDbUser()));
		Connection connection = DriverManager.getConnection(dbUrl, this.getDbUser(), this.getDbPassword());
		return connection;
	}

	@SuppressWarnings("resource")
	@Override
	public void execute() throws Exception {
		long startTime = System.currentTimeMillis();
		Environment.getLogger().info(String.format("begin execution orchestration [%s].", this.getName()));
		Connection defaultConnection = null;// 默认连接
		Connection usingConnection = null;// 使用的连接
		Statement statement = null;
		boolean myTrans = false;// 自己创建的事务
		boolean closeConnection = false;// 关闭连接
		try {
			for (ISqlExecutionAction action : this.getActions()) {
				usingConnection = defaultConnection;// 使用默认连接
				if (action.isIsolated()) {
					// 要求独立隔离
					// 已经存在事务，则提交
					if (myTrans && usingConnection != null) {
						usingConnection.commit();
					}
					if (statement != null) {
						// 回收数据库资源
						statement.close();
						statement = null;
					}
					if (action.getDbUrl() != null && !action.getDbUrl().isEmpty()) {
						// 要求隔离且需要新的数据库连接
						usingConnection = this.createConnection(action.getDbUrl());
						usingConnection.setAutoCommit(true);
						closeConnection = true;
					} else if (usingConnection == null) {
						// 要求隔离但不需要独立数据库连接
						defaultConnection = this.createConnection(null);
						defaultConnection.setAutoCommit(true);
						usingConnection = defaultConnection;
						closeConnection = false;
					}
					myTrans = false;
				} else {
					// 不要求隔离的动作
					if (!myTrans && usingConnection != null) {
						// 没有创建统一事务则创建统一事务
						usingConnection.setAutoCommit(false);
						myTrans = true;
					}
				}
				if (usingConnection == null) {
					// 使用默认数据库连接
					defaultConnection = this.createConnection(null);
					defaultConnection.setAutoCommit(false);
					usingConnection = defaultConnection;
					closeConnection = false;
					myTrans = true;
				}
				if (statement == null) {
					// 创建新的命令
					statement = usingConnection.createStatement();
				}
				action.execute(statement);
				if (closeConnection) {
					closeConnection = false;
					if (statement != null) {
						statement.close();
						statement = null;
					}
					usingConnection.close();
					usingConnection = null;
				}
			}
			if (myTrans && defaultConnection != null) {
				defaultConnection.commit();
			}
		} catch (Exception e) {
			if (myTrans && defaultConnection != null) {
				defaultConnection.rollback();
			}
			throw e;
		} finally {
			// 释放数据库资源
			if (statement != null) {
				statement.close();
			}
			if (usingConnection != null && !usingConnection.isClosed()) {
				usingConnection.close();
			}
			if (defaultConnection != null && !defaultConnection.isClosed()) {
				defaultConnection.close();
			}
		}
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		Environment.getLogger()
				.info(String.format("end execute orchestration [%s], used %s second.", this.getName(), excTime));
	}

}
