package org.colorcoding.tools.btulz.orchestration;

import java.util.List;

/**
 * 执行步骤集合
 * 
 * @author Niuren.Zhu
 *
 */
public interface IExecutionActionSteps<T extends IExecutionActionStep> extends List<T> {
	/**
	 * 创建并添加实例
	 * 
	 * @return
	 */
	T create();
}
