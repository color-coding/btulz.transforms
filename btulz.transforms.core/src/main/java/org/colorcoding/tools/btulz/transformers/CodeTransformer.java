package org.colorcoding.tools.btulz.transformers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IBusinessObject;
import org.colorcoding.tools.btulz.models.IBusinessObjectItem;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.models.IModel;
import org.colorcoding.tools.btulz.templates.Parameter;
import org.colorcoding.tools.btulz.templates.Parameters;
import org.colorcoding.tools.btulz.templates.Variable;
import org.colorcoding.tools.btulz.transformers.regions.RegionBusinessObject;
import org.colorcoding.tools.btulz.transformers.regions.RegionBusinessObjectItem;
import org.colorcoding.tools.btulz.transformers.regions.RegionBusinessObjectModel;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;
import org.colorcoding.tools.btulz.transformers.regions.models.DataTypeMappings;
import org.colorcoding.tools.btulz.transformers.regions.models.Property;

/**
 * 代码的转换器
 * 
 * @author Niuren.Zhu
 *
 */
public class CodeTransformer extends Transformer {

	private String templateFolder;

	public String getTemplateFolder() {
		return templateFolder;
	}

	public void setTemplateFolder(String templateFolder) {
		if (templateFolder.indexOf(File.separator) < 0) {
			// 不是完整的路径，补充目录到路径
			try {
				// 优先使用工作目录的模板
				File file = new File(
						Environment.getWorkingFolder() + File.separator + "code" + File.separator + templateFolder);
				if (file.exists() && file.isDirectory()) {
					this.templateFolder = file.getPath();
					return;
				}
				URI uri = Environment.getResource("code" + File.separator + templateFolder);
				if (uri != null) {
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
		this.templateFolder = new File(templateFolder).getPath();
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

	public void addDomains(String file) throws TransformException, MultiTransformException {
		XmlTransformer xmlTransformer = new XmlTransformer();
		xmlTransformer.setInterruptOnError(true);
		xmlTransformer.load(file, false);
		for (IDomain domain : xmlTransformer.getWorkingDomains()) {
			this.getDomains().add(domain);
		}
	}

	/**
	 * 获取运行时参数，可重载添加新的
	 * 
	 * @return
	 */
	protected Parameters getRuntimeParameters() {
		Parameters parameters = new Parameters();
		parameters.add(new Parameter("AppName", "btulz.transforms"));

		return parameters;
	}

	@Override
	public final void transform() throws Exception {
		for (IDomain domain : this.getDomains()) {
			Environment.getLogger().info(String.format("begin transform domain [%s].", domain.getName()));
			File outFolder = new File(this.getOutputFolder() + File.separator + domain.getName());
			Parameters parameters = this.getRuntimeParameters();
			parameters.add(new Parameter(RegionDomain.REGION_DELIMITER, domain));
			this.transform(new File(this.getTemplateFolder()), outFolder, parameters);
			Environment.getLogger().info(String.format("end transform domain [%s].", domain.getName()));
		}
	}

	/**
	 * 替换字符中的变量
	 * 
	 * @param string
	 *            字符串
	 * @param parameters
	 *            变量集合
	 * @return
	 * @throws Exception
	 */
	protected String replaceVariables(String string, List<Parameter> parameters) throws Exception {
		ArrayList<Variable> variables = new ArrayList<>();
		if (string != null && !string.equals("")) {
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
							Object value = parameter.getValue(variable.getValuePath());
							if (value != null) {
								string = string.replace(variable.getOriginal(),
										variable.isLowerCase() ? value.toString().toLowerCase() : value.toString());
							}
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

	/**
	 * 转换文件
	 * 
	 * @param folder
	 *            模板文件夹
	 * @throws Exception
	 */
	private final void transform(File tpltFolder, File rootFolder, Parameters parameters) throws Exception {
		if (tpltFolder == null || !tpltFolder.exists()) {
			return;
		}
		Environment.getLogger().info(String.format("transform folder [%s].", tpltFolder.getName()));
		String tmpFolder = tpltFolder.getPath().replace(this.getTemplateFolder(), "");
		tmpFolder = this.replaceVariables(tmpFolder, parameters);
		File outFolder = new File(
				tmpFolder == null ? rootFolder.getPath() : rootFolder.getPath() + File.separator + tmpFolder);
		if (!outFolder.exists()) {
			outFolder.mkdirs();
		}
		for (File file : tpltFolder.listFiles()) {
			if (file.isDirectory()) {
				// 子文件夹处理，每个文件夹一组变量避免冲突
				this.transform(file, rootFolder, new Parameters(parameters));
			} else if (file.isFile()) {
				if (file.getName().startsWith("~")) {
					// ~ 开始文件，为参数文件
					parameters.add(this.loadParameters(file));
				} else if (file.getName().equalsIgnoreCase("putout_domain_models.txt")) {
					// 输出数据结构在此
					for (Parameter parameter : parameters) {
						if (parameter.getName().equalsIgnoreCase(RegionDomain.REGION_PARAMETER_NAME)) {
							if (parameter.getValue() instanceof IDomain) {
								IDomain domain = (IDomain) parameter.getValue();
								XmlTransformer xmlTransformer = new XmlTransformer();
								xmlTransformer.setInterruptOnError(true);
								xmlTransformer.load(domain);
								xmlTransformer.save(outFolder.getPath());
							}
						}
					}
				} else if (file.getName().startsWith("Template_")) {
					// 转换文件
					this.transformFile(file, outFolder, parameters);
				} else {
					// 仅复制文件
					this.copyFile(file, outFolder);
				}
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param source
	 *            源文件
	 * @param output
	 *            输出文件
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
		if (source.getName().startsWith("Template_BO_") || source.getName().startsWith("Template_BOItem_")
				|| source.getName().startsWith("Template_Model_")) {
			// 和业务对象输出相关的
			for (IBusinessObject businessObject : domain.getBusinessObjects()) {
				parameters.add(new Parameter(RegionBusinessObject.REGION_PARAMETER_NAME, businessObject));
				if (source.getName().startsWith("Template_BO_")) {
					RegionDomain template = new RegionDomain();
					template.setTemplateFile(source.getPath());
					String name = this.replaceVariables(source.getName().replace("Template_BO_", ""), parameters);
					template.export(parameters, output.getPath() + File.separator + name);

				} else if (source.getName().startsWith("Template_BOItem_")) {
					this.transformFile(source, output, parameters, businessObject);
				} else if (source.getName().startsWith("Template_Model_")) {
					for (IModel model : domain.getModels()) {
						if (businessObject.getMappedModel().equals(model.getName())) {
							parameters.add(
									new Parameter(RegionBusinessObjectModel.REGION_PARAMETER_NAME, businessObject));
							RegionDomain template = new RegionDomain();
							template.setTemplateFile(source.getPath());
							String name = this.replaceVariables(source.getName().replace("Template_Model_", ""),
									parameters);
							template.export(parameters, output.getPath() + File.separator + name);
						}
					}
				}
			}
		} else {
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(source.getPath());
			String name = this.replaceVariables(source.getName(), parameters);
			template.export(parameters, output.getPath() + File.separator + name);
		}
	}

	protected void transformFile(File source, File output, Parameters parameters, IBusinessObject businessObject)
			throws Exception {
		IDomain domain = parameters.getValue(RegionDomain.REGION_PARAMETER_NAME, IDomain.class);
		if (domain == null) {
			return;
		}
		for (IBusinessObjectItem businessObjectItem : businessObject.getRelatedBOs()) {
			parameters.add(new Parameter(RegionBusinessObject.REGION_PARAMETER_NAME, businessObject));
			parameters.add(new Parameter(RegionBusinessObjectItem.REGION_PARAMETER_NAME, businessObjectItem));
			RegionDomain template = new RegionDomain();
			template.setTemplateFile(source.getPath());
			String name = this.replaceVariables(source.getName().replace("Template_BOItem_", ""), parameters);
			template.export(parameters, output.getPath() + File.separator + name);
		}
	}

	/**
	 * 加载额外的参数
	 * 
	 * @param source
	 * @return
	 */
	protected Parameter loadParameters(File source) {
		if (source.getName().equals("~parameter_property_declared_type.xml")) {
			// 属性的定义类型说明
			try {
				return new Parameter(Property.PARAMETER_NAME_DECLARED_TYPE,
						(DataTypeMappings.create(this.getTemplateFolder() + File.separator + "")));
			} catch (JAXBException e) {
				Environment.getLogger().error(e);
			}
		}
		return null;
	}
}
