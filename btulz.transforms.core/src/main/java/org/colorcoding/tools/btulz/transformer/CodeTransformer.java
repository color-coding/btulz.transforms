package org.colorcoding.tools.btulz.transformer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.model.IBusinessObject;
import org.colorcoding.tools.btulz.model.IBusinessObjectItem;
import org.colorcoding.tools.btulz.model.IDomain;
import org.colorcoding.tools.btulz.model.IModel;
import org.colorcoding.tools.btulz.model.data.emBORelation;
import org.colorcoding.tools.btulz.template.Parameter;
import org.colorcoding.tools.btulz.template.Parameters;
import org.colorcoding.tools.btulz.template.Variable;
import org.colorcoding.tools.btulz.transformer.region.ParametersFactory;
import org.colorcoding.tools.btulz.transformer.region.RegionBusinessObject;
import org.colorcoding.tools.btulz.transformer.region.RegionDomain;
import org.colorcoding.tools.btulz.transformer.region.model.OutputMappingList;

/**
 * 代码的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class CodeTransformer extends Transformer {
	public static final String TEMPLATE_FOLDER_CODE = "code";
	public static final String TEMPLATE_FILE = "Template_";
	public static final String TEMPLATE_FILE_BO = TEMPLATE_FILE + "BO_";
	public static final String TEMPLATE_FILE_BO_ITEM = TEMPLATE_FILE + "BOItem_";
	public static final String TEMPLATE_FILE_BO_MODEL = TEMPLATE_FILE + "BOModel_";
	public static final String TEMPLATE_FILE_PARAMETER = "~";

	private String templateFolder;

	public String getTemplateFolder() {
		return templateFolder;
	}

	/**
	 * 设置模板文件夹
	 * 
	 * @param templateFolder
	 */
	public void setTemplateFolder(String templateFolder) {
		if (templateFolder != null) {
			File file = new File(templateFolder);
			if (file.exists() && file.isDirectory()) {
				this.templateFolder = file.getPath();
				return;
			} else {
				// 不是完整的路径，补充目录到路径
				try {
					// 优先使用工作目录的模板
					file = new File(Environment.getWorkingFolder() + File.separator + TEMPLATE_FOLDER_CODE
							+ File.separator + templateFolder);
					if (file.exists() && file.isDirectory()) {
						this.templateFolder = file.getPath();
						return;
					}
					URI uri = Environment.getResource(TEMPLATE_FOLDER_CODE + "/" + templateFolder);
					if (uri != null && uri.getPath() != null) {
						file = new File(uri.getPath());
						if (file.exists() && file.isDirectory()) {
							this.templateFolder = file.getPath();
							return;
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		this.templateFolder = templateFolder;
		return;
	}

	private String outputFolder;

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	private List<IDomain> domains;

	public List<IDomain> getDomains() {
		if (domains == null) {
			domains = new ArrayList<>();
		}
		return domains;
	}

	public void addDomains(Iterable<IDomain> domains) {
		if (domains == null) {
			return;
		}
		for (IDomain domain : domains) {
			this.getDomains().add(domain);
		}
	}

	public void addDomain(IDomain domain) {
		if (domain == null) {
			return;
		}
		this.getDomains().add(domain);
	}

	public void addDomains(String file) throws TransformException, MultiTransformException {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.load(file, false);
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			this.getDomains().add(domain);
		}
	}

	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	private String artifactId;

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	private String projectVersion;

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	private String projectUrl;

	public String getProjectUrl() {
		return projectUrl;
	}

	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	private List<Parameter> parameters;

	public List<Parameter> getParameters() {
		if (this.parameters == null) {
			this.parameters = new ArrayList<Parameter>();
		}
		return this.parameters;
	}

	public void addParameters(Parameter parameter) {
		if (parameter == null) {
			return;
		}
		this.getParameters().add(parameter);
	}

	public void addParameters(Iterable<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			this.addParameters(parameter);
		}
	}

	/**
	 * 获取运行时参数，可重载添加新的
	 * 
	 * @return
	 */
	protected Parameters getRuntimeParameters() {
		Parameters parameters = new Parameters();
		parameters.add(ParametersFactory.create().createParameter("AppName", "btulz.transforms"));
		parameters.add(ParametersFactory.create().createParameter("GroupId", this.getGroupId()));
		parameters.add(ParametersFactory.create().createParameter("ArtifactId", this.getArtifactId()));
		parameters.add(ParametersFactory.create().createParameter("ProjectVersion", this.getProjectVersion()));
		parameters.add(ParametersFactory.create().createParameter("ProjectUrl", this.getProjectUrl()));
		parameters.add(ParametersFactory.create().createParameter("ID", new RuntimeParameter()));
		parameters.addAll(this.getParameters());
		return parameters;
	}

	@Override
	public final void transform() throws Exception {
		// 组合同名模型
		ArrayList<IDomain> domains = new ArrayList<>();
		for (IDomain domain : this.getDomains()) {
			IDomain newDomain = null;
			for (IDomain tmpDomain : domains) {
				if (tmpDomain.getName() != null && tmpDomain.getName().equals(domain.getName())) {
					newDomain = tmpDomain;
					break;
				}
			}
			if (newDomain == null) {
				domains.add(domain.clone());
			} else {
				IDomain tmpDomian = domain.clone();
				newDomain.getBusinessObjects().addAll(tmpDomian.getBusinessObjects());
				newDomain.getModels().addAll(tmpDomian.getModels());
			}
		}
		for (IDomain domain : domains) {
			Environment.getLogger().info(String.format("begin transform domain [%s] to codes.", domain.getName()));
			domain.buildMapping();// 构建关系
			File outFolder = new File(this.getOutputFolder() + File.separator + domain.getName());
			Parameters parameters = this.getRuntimeParameters();
			parameters.add(ParametersFactory.create().createParameter(domain));
			this.transform(new File(this.getTemplateFolder()), outFolder, parameters);
			Environment.getLogger().info(String.format("end transform codes."));
		}
	}

	/**
	 * 替换字符中的变量
	 * 
	 * @param string     字符串
	 * @param parameters 变量集合
	 * @return
	 * @throws Exception
	 */
	protected String replaceVariables(String string, List<Parameter> parameters) throws Exception {
		ArrayList<Variable> variables = new ArrayList<>();
		if (string != null && !string.isEmpty()) {
			if (parameters != null) {
				String pattern = "\\{([a-zA-Z].*?)\\}";
				Matcher matcher = Pattern.compile(pattern).matcher(string);
				while (matcher.find()) {
					Variable variable = new Variable();
					variable.setOriginal(matcher.group(0));
					String tmp = variable.getOriginal().replace("{", "").replace("}", "");
					String[] tmps = tmp.split("\\.");
					if (tmps.length > 1) {
						variable.setName(tmps[0]);
						variable.setValuePath(tmp.substring(variable.getName().length() + 1));
					} else {
						variable.setName(tmp);
					}
					variables.add(variable);
				}
				for (Variable variable : variables) {
					for (Parameter parameter : parameters) {
						if (parameter.getName().equalsIgnoreCase(variable.getName())) {
							Object value = null;
							if (variable.getValuePath() != null && variable.getValuePath().endsWith("folder")) {
								value = parameter
										.getValue(variable.getValuePath().replace(".folder", "").replace("folder", ""));
								if (value != null) {
									value = this.nsfolder(value.toString());
								}
							} else {
								value = parameter.getValue(variable.getValuePath());
							}
							if (value != null) {
								string = string.replace(variable.getOriginal(),
										variable.isLowerCase() ? value.toString().toLowerCase() : value.toString());
							}
							break;
						}
					}
				}
			}
			if (string.endsWith(".txt") && string.indexOf(".") != string.lastIndexOf(".")) {
				string = string.substring(0, string.lastIndexOf(".txt"));
			}
		}
		return string;
	}

	protected String nsfolder(String value) {
		return value.replace(".", File.separator);
	}

	/**
	 * 转换文件
	 * 
	 * @param folder 模板文件夹
	 * @throws Exception
	 */
	private final void transform(File tpltFolder, File rootFolder, Parameters parameters) throws Exception {
		if (tpltFolder == null || !tpltFolder.exists()) {
			return;
		}
		Environment.getLogger().info(String.format("transform folder [.%s].",
				tpltFolder.getAbsolutePath().replace(this.getTemplateFolder(), "")));
		// 文件排序，先文件后目录
		File[] tmpfiles = tpltFolder.listFiles();
		if (tmpfiles != null) {
			List<File> files = Arrays.asList(tmpfiles);
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					if (o1.isDirectory() && o2.isFile())
						return 1;
					if (o1.isFile() && o2.isDirectory())
						return -1;
					if (o1.getName().startsWith(TEMPLATE_FILE_PARAMETER)
							&& !o2.getName().startsWith(TEMPLATE_FILE_PARAMETER))
						return -1;
					if (!o1.getName().startsWith(TEMPLATE_FILE_PARAMETER)
							&& o2.getName().startsWith(TEMPLATE_FILE_PARAMETER))
						return 1;
					return o1.getName().compareTo(o2.getName());
				}
			});
			String tmpFolder = tpltFolder.getPath().replace(this.getTemplateFolder(), "");
			tmpFolder = this.replaceVariables(tmpFolder, parameters);
			File outFolder = new File(
					tmpFolder == null ? rootFolder.getPath() : rootFolder.getPath() + File.separator + tmpFolder);
			if (!outFolder.exists() && outFolder.getPath().indexOf("{") < 0) {
				// 输出文件夹不存在，且不存在变量是创建文件夹。
				outFolder.mkdirs();
			}
			// 遍历处理文件清单
			for (File file : files) {
				if (file.isDirectory()) {
					// 子文件夹处理
					this.transform(file, rootFolder, parameters);
				} else if (file.isFile()) {
					if (file.getName().startsWith(TEMPLATE_FILE_PARAMETER)) {
						// ~ 开始文件，为参数文件
						parameters.add(this.loadParameters(file));
					} else if (file.getName().equalsIgnoreCase("putout_domain_models.txt")) {
						// 输出数据结构在此
						for (Parameter parameter : parameters) {
							if (parameter.getName().equalsIgnoreCase(RegionDomain.REGION_PARAMETER_NAME)) {
								if (parameter.getValue() instanceof IDomain) {
									IDomain domain = (IDomain) parameter.getValue();
									XmlTransformer xmlTransformer = new XmlTransformerDom4j();
									xmlTransformer.setInterruptOnError(true);
									xmlTransformer.load(domain);
									xmlTransformer.save(outFolder.getPath());
								}
							}
						}
					} else if (file.getName().startsWith(TEMPLATE_FILE)) {
						// 转换文件，每个文件一组变量避免冲突
						this.transformFile(file, outFolder, new Parameters(parameters));
					} else {
						// 仅复制文件
						this.copyFile(file, outFolder);
					}
				}
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param source 源文件
	 * @param output 输出文件
	 * @throws Exception
	 */
	private void copyFile(File source, File outFolder) throws Exception {
		if (!source.exists()) {
			return;
		}
		if (!outFolder.exists()) {
			outFolder.mkdirs();
		}
		String name = this.replaceVariables(source.getName(), null);
		File outFile = new File(outFolder.getPath() + File.separator + name);
		// 获取模板文件的编码，以此编码输出文件
		String encoding = Environment.getEncoding(source.getPath());
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), encoding));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), encoding));
		String readString = null;
		while ((readString = reader.readLine()) != null) {
			writer.write(readString);
			writer.newLine();
		}
		reader.close();
		writer.flush();
		writer.close();
	}

	private String getFilePath(File folder, String name, Parameters parameters) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		if (folder != null && folder.getPath() != null) {
			stringBuilder.append(this.replaceVariables(folder.getPath(), parameters));
		}
		if (name != null) {
			stringBuilder.append(File.separator);
			stringBuilder.append(this.replaceVariables(name, parameters));
		}
		return stringBuilder.toString();
	}

	/**
	 * 转换文件
	 * 
	 * @param source
	 * @param output
	 * @throws Exception
	 */
	protected void transformFile(File source, File output, Parameters parameters) throws Exception {
		IDomain domain = parameters.getValue(RegionDomain.REGION_PARAMETER_NAME, IDomain.class);
		if (domain == null) {
			return;
		}
		if (source.getName().startsWith(TEMPLATE_FILE_BO) || source.getName().startsWith(TEMPLATE_FILE_BO_ITEM)
				|| source.getName().startsWith(TEMPLATE_FILE_BO_MODEL)) {
			// 和业务对象输出相关的
			for (IBusinessObject businessObject : domain.getBusinessObjects()) {
				parameters.add(ParametersFactory.create().createParameter(businessObject));
				parameters.add(ParametersFactory.create()
						.createParameter("Master" + RegionBusinessObject.REGION_PARAMETER_NAME, businessObject));
				if (source.getName().startsWith(TEMPLATE_FILE_BO)) {
					RegionDomain template = new RegionDomain();
					template.setTemplateFile(source.getPath());
					template.export(parameters,
							this.getFilePath(output, source.getName().replace(TEMPLATE_FILE_BO, ""), parameters));
				} else if (source.getName().startsWith(TEMPLATE_FILE_BO_ITEM)) {
					this.transformFileBOItems(source, output, parameters, businessObject);
				} else if (source.getName().startsWith(TEMPLATE_FILE_BO_MODEL)) {
					this.transformFileBOModels(source, output, parameters, businessObject);
				}
			}
		} else {
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(source.getPath());
			template.export(parameters,
					this.getFilePath(output, source.getName().replace(TEMPLATE_FILE, ""), parameters));
		}
	}

	protected void transformFileBOItems(File source, File output, Parameters parameters, IBusinessObject businessObject)
			throws Exception {
		IDomain domain = parameters.getValue(RegionDomain.REGION_PARAMETER_NAME, IDomain.class);
		if (domain == null) {
			return;
		}
		for (int i = 0; i < businessObject.getRelatedBOs().size(); i++) {
			IBusinessObjectItem businessObjectItem = businessObject.getRelatedBOs().get(i);
			if (businessObjectItem.getRelation() != emBORelation.OneToMany) {
				// 非1：n关系不生产
				continue;
			}
			parameters.add(ParametersFactory.create().createParameter(businessObjectItem, businessObject, i + 1));
			for (IModel model : domain.getModels()) {
				if (!businessObjectItem.getMappedModel().equals(model.getName())) {
					continue;
				}
				parameters.add(ParametersFactory.create().createParameter(model,
						parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING)));
				RegionDomain template = new RegionDomain();
				template.setTemplateFile(source.getPath());
				template.export(parameters,
						this.getFilePath(output, source.getName().replace(TEMPLATE_FILE_BO_ITEM, ""), parameters));
				// 如果有子项继续
				Parameters nParameters = new Parameters(parameters);
				nParameters.add(ParametersFactory.create().createParameter((IBusinessObject) businessObjectItem));
				this.transformFileBOItems(source, output, nParameters, businessObjectItem);
			}
		}
	}

	protected void transformFileBOModels(File source, File output, Parameters parameters,
			IBusinessObject businessObject) throws Exception {
		IDomain domain = parameters.getValue(RegionDomain.REGION_PARAMETER_NAME, IDomain.class);
		if (domain == null) {
			return;
		}
		for (IModel model : domain.getModels()) {
			if (businessObject.getMappedModel().equals(model.getName())) {
				Parameters usingParameters = new Parameters(parameters);
				usingParameters.add(ParametersFactory.create().createParameter(model,
						parameters.get(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING)));
				RegionDomain template = new RegionDomain();
				template.setTemplateFile(source.getPath());
				template.export(usingParameters, this.getFilePath(output,
						source.getName().replace(TEMPLATE_FILE_BO_MODEL, ""), usingParameters));
			}
		}
		for (IBusinessObjectItem businessObjectItem : businessObject.getRelatedBOs()) {
			// 如果有子项继续
			Parameters usingParameters = new Parameters(parameters);
			usingParameters.add(ParametersFactory.create().createParameter((IBusinessObject) businessObjectItem));
			this.transformFileBOModels(source, output, usingParameters, businessObjectItem);
		}
	}

	/**
	 * 加载额外的参数
	 * 
	 * @param source
	 * @return
	 */
	protected Parameter loadParameters(File source) {
		if (source.getName().equals("~parameter_entity_output.xml")) {
			// 属性类型的输出说明
			try {
				return ParametersFactory.create().createParameter(ParametersFactory.PARAMETER_NAME_OUTPUT_MAPPING,
						OutputMappingList.create(source));
			} catch (JAXBException e) {
				Environment.getLogger().error(e);
			}
		}
		return null;
	}

}
