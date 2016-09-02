package org.colorcoding.tools.btulz.transformers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IDomain;

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
					templateFolder = file.getPath();
					return;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		this.templateFolder = templateFolder;
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

	@Override
	public void transform() throws Exception {
		this.transform(new File(this.getTemplateFolder()));
	}

	private void transform(File folder) throws Exception {
		if (folder == null || !folder.exists()) {
			return;
		}
		String tmpFolder = folder.getPath().replace(this.getTemplateFolder(), "");
		File outFolder = new File(this.getOutputFolder() + tmpFolder);
		if (!outFolder.exists()) {
			outFolder.mkdirs();
		}
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				this.transform(file);
			} else if (file.isFile()) {
				File output = new File(this.getOutputFolder() + file.getPath().replace(this.getTemplateFolder(), ""));
				if (file.getName().startsWith("Template_")) {
					if (file.getName().startsWith("Template_BO_")) {

						continue;
					} else if (file.getName().startsWith("Template_BOItem_")) {

						continue;
					} else if (file.getName().startsWith("Template_Model_")) {

						continue;
					}
					this.transformFile(file, output);
					continue;
				}
				// 仅复制文件
				this.copyFile(file, output);
			}
		}
	}

	private void copyFile(File source, File put) throws IOException {
		if (!source.exists()) {
			return;
		}
		if (!put.exists()) {
			put.getParentFile().mkdirs();
			put.createNewFile();
		}
		String encoding = Environment.getEncoding(source.getPath());
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), encoding));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(put), encoding));
		String readString = null;
		while ((readString = reader.readLine()) != null) {
			writer.write(readString);
			writer.newLine();
		}
		reader.close();
		writer.flush();
		writer.close();
	}

	private void transformFile(File source, File put) {

	}
}
