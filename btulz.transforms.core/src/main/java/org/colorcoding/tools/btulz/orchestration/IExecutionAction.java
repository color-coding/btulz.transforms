package org.colorcoding.tools.btulz.orchestration;

/**
 * 执行动作
 * 
 * @author Niuren.Zhu
 *
 */
public interface IExecutionAction {
	/**
	 * 获取-名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 设置-名称 *
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 获取-描述
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * 设置-描述
	 * 
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * 步骤
	 * 
	 * @return
	 */
	IExecutionActionSteps<?> getSteps();

	/**
	 * 执行
	 * 
	 * @throws Exception
	 */
	void execute() throws Exception;
}
