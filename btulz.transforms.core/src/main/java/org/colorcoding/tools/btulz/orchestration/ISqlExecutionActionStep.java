package org.colorcoding.tools.btulz.orchestration;

import java.sql.Statement;
import java.util.List;

/**
 * SQL执行步骤
 * 
 * @author Niuren.Zhu
 *
 */
public interface ISqlExecutionActionStep extends IExecutionActionStep {

	/**
	 * 设置
	 * 
	 * @param statement
	 */
	void setStatement(Statement statement);

	/**
	 * 检查是否执行
	 * 
	 * @param value
	 *            运行的状态值
	 * @return 是否执行此步骤
	 */
	boolean check(Object value);

	/**
	 * 获取-运行值
	 * 
	 * @return
	 */
	String getRunValue();

	/**
	 * 设置-运行值
	 */
	void setRunValue(String value);

	/**
	 * 运行的sql语句
	 * 
	 * @return
	 */
	List<String> getScripts();
}
