package org.colorcoding.tools.btulz.transformers;

import org.colorcoding.tools.btulz.models.IDomain;

/**
 * 文件转换
 * 
 * @author Niuren.Zhu
 *
 */
public interface IFileTransformer {

	/**
	 * 是否分组输出文件
	 * 
	 * @return
	 */
	boolean isGroupingFile();

	/**
	 * 设置是否分组输出文件
	 * 
	 * @param value
	 * @return
	 */
	void setGroupingFile(boolean value);

	/**
	 * 输入文件
	 * 
	 * @param filePath
	 *            路径
	 * @param includingSubFolder
	 * @throws TransformException
	 * @throws MultiTransformException
	 *             包含子项文件夹
	 */
	void load(String filePath, boolean includingSubFolder) throws TransformException, MultiTransformException;

	/**
	 * 输入文件数组
	 * 
	 * @param filePathes
	 *            路径
	 * @param includingSubFolder
	 * @throws TransformException
	 * @throws MultiTransformException
	 *             包含子项文件夹
	 */
	void load(String[] filePathes, boolean includingSubFolder) throws TransformException, MultiTransformException;

	/**
	 * 输入的模型
	 * 
	 * @param domain
	 */
	void load(IDomain domain);

	/**
	 * 获取当前的领域模型
	 * 
	 * @return
	 */
	IDomain[] getWorkingDomains();

	/**
	 * 保存模型
	 * 
	 * @throws TransformException
	 * @throws MultiTransformException
	 */
	void save() throws TransformException, MultiTransformException;

	/**
	 * 输出目录
	 * 
	 * @param folder
	 * @throws TransformException
	 * @throws MultiTransformException
	 */
	void save(String folder) throws TransformException, MultiTransformException;
}
