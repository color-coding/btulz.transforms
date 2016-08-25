package org.colorcoding.tools.btulz.orchestration;

import java.sql.Statement;

/**
 * SQL执行行动
 * 
 * @author Niuren.Zhu
 *
 */
public interface ISqlExecutionAction extends IExecutionAction {

	@Override
	ISqlExecutionActionSteps getSteps();

	/**
	 * 是否保存步骤结果
	 * 
	 * @return
	 */
	boolean isKeepStepResult();

	/**
	 * 设置-保存步骤结果
	 * 
	 * @param value
	 */
	void setKeepStepResult(boolean value);

	/**
	 * 执行
	 * 
	 * @param statement
	 * 
	 * @throws Exception
	 */
	void execute(Statement statement) throws Exception;
}
