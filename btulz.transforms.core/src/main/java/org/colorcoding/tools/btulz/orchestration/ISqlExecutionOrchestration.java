package org.colorcoding.tools.btulz.orchestration;

/**
 * SQL执行规划
 * 
 * @author Niuren.Zhu
 *
 */
public interface ISqlExecutionOrchestration extends IExecutionOrchestration {
	/**
	 * 获取-数据库驱动名称
	 * 
	 * @return
	 */
	String getDriverName();

	/**
	 * 设置-数据库驱动名称
	 * 
	 * @param value
	 */
	void setDriverName(String value);

	/**
	 * 获取-数据库地址
	 * 
	 * @return
	 */
	String getDbUrl();

	/**
	 * 设置-数据库地址
	 * 
	 * @param description
	 */
	void setDbUrl(String value);

	/**
	 * 获取-数据库用户
	 * 
	 * @return
	 */
	String getDbUser();

	/**
	 * 设置-数据库用户
	 * 
	 * @param value
	 */
	void setDbUser(String value);

	/**
	 * 获取-数据库密码
	 * 
	 * @return
	 */
	String getDbPassword();

	/**
	 * 设置-数据库密码
	 * 
	 * @param value
	 */
	void setDbPassword(String value);

	/**
	 * 是否一体化处理（一体化事务）
	 * 
	 * @return
	 */
	boolean isIntegrated();

	/**
	 * 设置-一体化处理
	 * 
	 * @param value
	 */
	void setIntegrated(boolean value);
}
