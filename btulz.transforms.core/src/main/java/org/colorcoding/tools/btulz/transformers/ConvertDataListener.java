package org.colorcoding.tools.btulz.transformers;

/**
 * 数据解释监听
 * 
 * @author Niuren.Zhu
 *
 */
public interface ConvertDataListener {

	/**
	 * 转换数据
	 * 
	 * @param event
	 *            事件
	 * @return 转换的数据
	 */
	Object convertData(ConvertDataEvent event);
}
