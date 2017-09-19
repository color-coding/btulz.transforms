package org.colorcoding.tools.btulz.orchestration;

/**
 * 执行步骤
 * 
 * @author Niuren.Zhu
 *
 */
public interface IExecutionActionStep {
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
	 * 执行
	 * 
	 * @throws Exception
	 */
	Object execute() throws Exception;
}
