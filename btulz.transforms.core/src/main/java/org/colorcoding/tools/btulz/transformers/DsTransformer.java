package org.colorcoding.tools.btulz.transformers;

import java.io.File;
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

	public void addDomains(String file) throws Exception {
		this.addDomains(new File(file));
	}

	public void addDomains(File file) throws Exception {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.load(file, false);
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			this.getDomains().add(domain);
		}
	}

	private String templateFile;

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		if (templateFile != null && !templateFile.isEmpty()) {
			if (templateFile.indexOf(File.separator) < 0) {
				// 不包含路径，用工作目录补全
				File file = new File(
						Environment.getWorkingFolder() + File.separator + "ds" + File.separatorChar + templateFile);
				if (file.isFile() && file.exists()) {
					// 工作目录存在文件
					this.templateFile = file.getPath();
					return;
				}
			}
		}
		this.templateFile = templateFile;
	}

	/**
	 * 获取输出文件名称
	 * 
	 * @return
	 */
	protected String getOutputFile() {
		File file = new File(this.getTemplateFile());
		return this.getOutputFile(file.isFile() ? file.getName() : this.getTemplateFile());
	}

	private DataTypeMappings dataTypeMappings;

	public DataTypeMappings getDataTypeMappings() {
		if (dataTypeMappings == null) {
			if (this.getTemplateFile() != null && !this.getTemplateFile().isEmpty()) {
				try {
					if (this.getTemplateFile().indexOf(File.separator) > 0) {
						// 有路径符，表示使用的是物理文件
						dataTypeMappings = DataTypeMappings.create(
								this.getTemplateFile().replace(File.separatorChar + "ds_", File.separatorChar + "dm_"));
					} else {
						// 无路径符，尝试使用资源流
						String resName = String.format("ds/%s",
								this.getTemplateFile().replace("ds_", "dm_").toLowerCase());
						InputStream inputStream = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream(resName);
						if (inputStream != null) {
							dataTypeMappings = DataTypeMappings.create(inputStream);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return dataTypeMappings;
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

	private IDomain currentDomain;

	@Override
	public void transform() throws Exception {
		if (this.getTemplateFile() == null || this.getTemplateFile().isEmpty()) {
			throw new Exception("no template file specified.");
		}
		long startTime = System.currentTimeMillis();
		Environment.getLogger().info(String.format("begin transform data structures."));
		for (IDomain domain : this.getDomains()) {
			domain.buildMapping();// 构建关系
			this.currentDomain = domain;
			Environment.getLogger()
					.info(String.format("changed working domain to [%s].", this.currentDomain.getName()));
			File dsFile = new File(this.getOutputFile());
			RegionDomain template = new RegionDomain();
			if (this.getTemplateFile().indexOf(File.separator) > 0) {
				// 有路径符，表示使用的是物理文件
				template.setTemplateFile(this.getTemplateFile());
			} else {
				// 无路径符，尝试使用资源流
				String resName = String.format("ds/%s", this.getTemplateFile()).toLowerCase();// 自动按小写处理
				InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resName);
				if (inputStream == null) {
					throw new Exception(String.format("not found template file [%s].", resName));
				}
				template.setEncoding("utf-8");
				template.parse(inputStream);
			}

			template.export(this.getRuntimeParameters(), dsFile);
			// 调用基类的结构编排调用
			super.execute(dsFile);
		}
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		Environment.getLogger().info(String.format("end transform data structures, used %s second.", excTime));
	}

}
