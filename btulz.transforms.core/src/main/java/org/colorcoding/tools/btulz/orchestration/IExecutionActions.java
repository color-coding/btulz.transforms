package org.colorcoding.tools.btulz.orchestration;

import java.util.List;

/**
 * 执行动作集合
 * 
 * @author Niuren.Zhu
 *
 */
public interface IExecutionActions<T extends IExecutionAction> extends List<T> {
	/**
	 * 创建并添加实例
	 * 
	 * @return
	 */
	T create();
}
