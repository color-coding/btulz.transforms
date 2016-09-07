package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMappings;
import org.colorcoding.tools.btulz.transformers.regions.models.Property;

/**
 * 数据结构与模型的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class DsTransformer extends DbTransformer {

	public static final String TEMPLATE_FOLDER_DATA_STRUCTURES = "ds";
	private List<IDomain> domains;

	public List<IDomain> getDomains() {
		if (domains == null) {
			domains = new ArrayList<>();
		}
		return domains;
	}

	public void addDomains(IDomain domain) {
		if (domain == null) {
			return;
		}
		this.getDomains().add(domain);
	}

	public void addDomains(Iterable<IDomain> domains) {
		if (domains == null) {
			return;
		}
		for (IDomain domain : domains) {
			this.getDomains().add(domain);
		}
	}

	public void addDomains(String file) throws TransformException, MultiTransformException {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.load(file, false);
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			this.getDomains().add(domain);
		}
	}

	private DataTypeMappings dataTypeMappings;

	public DataTypeMappings getDataTypeMappings() {
		if (dataTypeMappings == null) {
			if (this.getTemplateFile() != null && !this.getTemplateFile().equals("")) {
				try {
					dataTypeMappings = DataTypeMappings.create(
							this.getTemplateFile().replace(File.separatorChar + "ds_", File.separatorChar + "dm_"));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return dataTypeMappings;
	}

	/**
	 * 设置模板文件，若文件不含路径自动补齐，文件不存在则在资源文件中查询并释放到工作目录
	 */
	@Override
	public void setTemplateFile(String templateFile) {
		if (templateFile.indexOf(File.separator) < 0 && templateFile.startsWith("ds")) {
			// 不是完整的路径，补充目录到路径
			try {
				// 优先使用工作目录的模板
				File file = new File(Environment.getWorkingFolder() + File.separator + TEMPLATE_FOLDER_DATA_STRUCTURES
						+ File.separator + templateFile);
				if (file.exists() && file.isFile()) {
					super.setTemplateFile(file.getPath());
					return;
				}
				// 其次使用资源文件模板
				String resName = String.format("ds/%s", templateFile);
				InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resName);
				if (stream != null) {
					FileOutputStream fos = null;
					int len = -1;
					byte[] b = new byte[1024];
					while ((len = stream.read(b)) != -1) {
						if (fos == null) {
							file.getParentFile().mkdirs();
							file.createNewFile();
							fos = new FileOutputStream(file);
						}
						fos.write(b, 0, len);
					}
					fos.flush();
					fos.close();
					stream.close();
					super.setTemplateFile(file.getPath());
					// 输出数据类型转换文件
					stream = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(resName.replace("ds/ds_", "ds/dm_"));
					if (stream != null) {
						file = new File(
								Environment.getWorkingFolder() + File.separator + TEMPLATE_FOLDER_DATA_STRUCTURES
										+ File.separator + templateFile.replace("ds_", "dm_"));
						fos = null;
						len = -1;
						b = new byte[1024];
						while ((len = stream.read(b)) != -1) {
							if (fos == null) {
								file.getParentFile().mkdirs();
								file.createNewFile();
								fos = new FileOutputStream(file);
							}
							fos.write(b, 0, len);
						}
						fos.flush();
						fos.close();
						stream.close();
					}
					return;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		super.setTemplateFile(templateFile);
	}

	@Override
	protected Parameters getRuntimeParameters() {
		Parameters parameters = super.getRuntimeParameters();
		if (currentDomain != null) {
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, currentDomain));
		}
		parameters.add(new Parameter(Property.PARAMETER_NAME_MAPPED_TYPE, this.getDataTypeMappings()));
		return parameters;
	}

	protected String getOutputFile() {
		if (this.currentDomain != null && this.currentDomain.getName() != null
				&& !this.currentDomain.getName().equals("")) {
			String tmp = String.format("-%s", this.getDbName());
			String outputFile = super.getOutputFile();
			return outputFile.replace(tmp, tmp + "-" + this.currentDomain.getName());
		}
		return super.getOutputFile();
	}

	private IDomain currentDomain;

	@Override
	public void transform() throws Exception {
		long startTime = System.currentTimeMillis();
		Environment.getLogger().info(String.format("begin transform data structures."));
		for (IDomain domain : this.getDomains()) {
			this.currentDomain = domain;
			Environment.getLogger()
					.info(String.format("changed working domain to [%s].", this.currentDomain.getName()));
			super.transform();
		}
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		Environment.getLogger().info(String.format("end transform data structures, used %s second.", excTime));
	}

}
