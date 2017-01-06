package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;

public abstract class FileTransformer extends Transformer implements IFileTransformer {

	protected void initialize() {
		super.initialize();
		this.setGroupingFile(true);
		this.setKeepResults(true);
	}

	private boolean groupingFile;

	public final boolean isGroupingFile() {
		return this.groupingFile;
	}

	public final void setGroupingFile(boolean value) {
		this.groupingFile = value;
	}

	@Override
	protected void clearResults() {
		if (!this.isKeepResults()) {
			this.domainModels = null;
		}
		super.clearResults();
	}

	private ArrayList<IDomain> domainModels;

	@Override
	public final IDomain[] getWorkingDomains() {
		return this.getDomainModels().toArray(new IDomain[] {});
	}

	protected final List<IDomain> getDomainModels() {
		if (this.domainModels == null) {
			this.domainModels = new ArrayList<>();
		}
		return this.domainModels;
	}

	@Override
	public final void load(String filePath, boolean includingSubFolder)
			throws TransformException, MultiTransformException {
		if (filePath == null) {
			return;
		}
		this.load(new File(filePath), includingSubFolder);
	}

	@Override
	public final void load(File file, boolean includingSubFolder) throws TransformException, MultiTransformException {
		this.clearResults();
		this.loadFile(file, includingSubFolder);
		// 如果有错误，则抛出错误
		if (!this.isInterruptOnError() && this.getErrors().length > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Exception exception : this.getErrors()) {
				stringBuilder.append(exception.getMessage());
			}
			throw new MultiTransformException(stringBuilder.toString(), this.getErrors());
		}
	}

	@Override
	public final void load(String[] filePathes, boolean includingSubFolder)
			throws TransformException, MultiTransformException {
		this.clearResults();
		if (filePathes == null) {
			return;
		}
		for (String filepath : filePathes) {
			this.load(filepath, includingSubFolder);
		}
		// 如果有错误，则抛出错误
		if (!this.isInterruptOnError() && this.getErrors().length > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Exception exception : this.getErrors()) {
				stringBuilder.append(exception.getMessage());
			}
			throw new MultiTransformException(stringBuilder.toString(), this.getErrors());
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 *            文件
	 * @param includingSubFolder
	 *            是否包含子文件内容
	 * @param clear
	 *            是否清除历史数据
	 */
	private void loadFile(File file, boolean includingSubFolder) throws TransformException {
		IDomain[] tmpDomains = null;
		if (file.isFile()) {
			try {
				tmpDomains = this.load(file);
			} catch (Exception e) {
				if (this.isInterruptOnError()) {
					// 中断错误
					throw new TransformException(e);
				} else {
					// 不中断错误，记录结果
					this.logError(e);
				}
			}
			if (tmpDomains != null) {
				for (IDomain domain : tmpDomains) {
					this.getDomainModels().add(domain);
				}
			}
		} else if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				if (subFile.isFile()) {
					this.loadFile(subFile, false);
				} else if (subFile.isDirectory()) {
					if (includingSubFolder) {
						this.loadFile(subFile, true);
					}
				}
			}
		}
	}

	@Override
	public void load(IDomain domain) {
		if (domain == null) {
			return;
		}
		this.clearResults();
		this.getDomainModels().add(domain);
	}

	@Override
	public final void save() throws TransformException, MultiTransformException {
		this.save(Environment.getWorkingFolder());
	}

	@Override
	public final void save(String folder) throws TransformException, MultiTransformException {
		File outFolder = new File(folder);
		if (!outFolder.exists()) {
			outFolder.mkdirs();
		}
		ArrayList<IDomain> outDomains = new ArrayList<>();
		if (this.isGroupingFile()) {
			// 分组输出
			outDomains = this.groupDomains(this.getWorkingDomains());
		} else {
			// 不分组输出
			for (IDomain domain : this.getDomainModels()) {
				if (domain == null)
					continue;
				if (domain.getModels().size() == 0 && domain.getBusinessObjects().size() == 0)
					continue;
				outDomains.add(domain);
			}
		}
		// 逐个保存文件
		for (IDomain domain : outDomains) {
			try {
				this.save(outFolder, domain);
			} catch (Exception e) {
				if (this.isInterruptOnError()) {
					// 中断错误
					throw new TransformException(e);
				} else {
					// 不中断错误，记录结果
					this.logError(e);
				}
			}
		}
		// 如果有错误，则抛出错误
		if (!this.isInterruptOnError() && this.getErrors().length > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			for (Exception exception : this.getErrors()) {
				stringBuilder.append(exception.getMessage());
			}
			throw new MultiTransformException(stringBuilder.toString(), this.getErrors());
		}
	}

	/**
	 * 重新分组领域模型
	 * 
	 * 默认按业务对象分组
	 * 
	 * @param domains
	 * @return
	 */
	protected ArrayList<IDomain> groupDomains(IDomain[] domains) {
		ArrayList<IDomain> groupDomains = new ArrayList<>();
		if (domains != null) {
			// 按领域归集业务对象（去重），并记录模型（不去重）
			ArrayList<IModel> allModels = new ArrayList<>();
			for (IDomain tmpDomain : domains) {
				if (tmpDomain == null) {
					continue;
				}
				for (IBusinessObject tmpBO : tmpDomain.getBusinessObjects()) {
					if (tmpBO == null) {
						continue;
					}
					IDomain newDomain = tmpDomain.clone(true);
					newDomain.getBusinessObjects().add(tmpBO.clone());
					groupDomains.add(newDomain);
				}
				allModels.addAll(tmpDomain.getModels());
			}
			// 添加业务对象涉及的模型
			for (IDomain domain : groupDomains) {
				for (IBusinessObject businessObject : domain.getBusinessObjects()) {
					domain.getModels().addAll(this.getModels(allModels, businessObject));
				}
			}
		}
		return groupDomains;
	}

	private Collection<IModel> getModels(ArrayList<IModel> models, IBusinessObject businessObject) {
		ArrayList<IModel> returnModels = new ArrayList<>();
		for (IModel model : models) {
			if (model == null) {
				continue;
			}
			if (model.getName().equals(businessObject.getMappedModel())) {
				returnModels.add(model.clone());
			}
		}
		for (IBusinessObjectItem boItem : businessObject.getRelatedBOs()) {
			returnModels.addAll(this.getModels(models, boItem));
		}
		return returnModels;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 *            文件
	 * @return
	 */
	protected abstract IDomain[] load(File file) throws Exception;

	/**
	 * 保存为文件
	 * 
	 * @param outFolder
	 *            输出的目录
	 * @param domain
	 *            领域模型
	 */
	protected abstract void save(File outFolder, IDomain domain) throws Exception;

}
