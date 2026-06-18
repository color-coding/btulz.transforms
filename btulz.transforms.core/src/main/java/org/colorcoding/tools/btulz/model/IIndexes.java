package org.colorcoding.tools.btulz.model;

import org.colorcoding.tools.btulz.util.List;

/**
 * 索引集合
 * 
 * @author Niuren.Zhu
 *
 */
public interface IIndexes extends List<IIndex> {
	/**
	 * 创建并添加索引
	 * 
	 * @return
	 */
	IIndex create();
}