package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.colorcoding.tools.btulz.Environment;

/**
 * 模板
 * 
 * @author Niuren.Zhu
 *
 */
public class Template extends TemplateRegion {

	private String templateFile;

	/**
	 * 模板文件
	 * 
	 * @return
	 */
	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	private String encoding = null;// "utf-8";

	/**
	 * 文件编码
	 * 
	 * @return
	 */
	public String getEncoding() {
		if (this.encoding == null) {
			this.encoding = Environment.getEncoding(this.getTemplateFile());
		}
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 根据模板输出
	 * 
	 * @param parameters
	 *            参数
	 * @param outputFile
	 *            输出文件
	 * @throws Exception
	 */
	public void export(List<Parameter> parameters, String outputFile) throws Exception {
		File outFile = new File(outputFile);
		export(parameters, outFile);
	}

	/**
	 * 根据模板输出
	 * 
	 * @param parameters
	 *            参数
	 * @param outputFile
	 *            输出文件
	 * @throws Exception
	 */
	public void export(List<Parameter> parameters, File outputFile) throws Exception {
		if (!outputFile.exists()) {
			outputFile.getParentFile().mkdirs();
			outputFile.createNewFile();
		} else {
			outputFile.delete();
			outputFile.createNewFile();
		}
		export(parameters,
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), this.getEncoding())));
	}

	/**
	 * 模板已初始化标记
	 */
	private boolean initialized = false;

	/**
	 * 根据模板输出
	 * 
	 * @param parameters
	 *            参数
	 * @param writer
	 *            输出文件
	 * @throws Exception
	 */
	public void export(List<Parameter> parameters, BufferedWriter writer) throws Exception {
		File tpltFile = new File(this.getTemplateFile());
		if (!tpltFile.exists() || !tpltFile.isFile()) {
			throw new FileNotFoundException(this.getTemplateFile());
		}
		BufferedReader reader = null;
		try {
			if (!initialized) {
				Environment.getLogger().info(String.format("start to parse template file [%s].", tpltFile.getName()));
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(tpltFile), this.getEncoding()));
				this.parse(reader);// 解析模板
				initialized = true;
				reader.close();
			}
			this.export(writer, parameters);// 输出数据
			writer.close();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	@Override
	protected Iterable<Parameter> getRegionParameters(List<Parameter> pars) throws InvalidParameterException {
		return new Iterable<Parameter>() {
			@Override
			public Iterator<Parameter> iterator() {
				// 仅运行一次，且空值
				return new Iterator<Parameter>() {
					boolean done = false;// 是否运行过

					@Override
					public boolean hasNext() {
						return !done;
					}

					@Override
					public Parameter next() {
						done = true;// 返回一次值，则标记运行过
						return null;
					}
				};
			}

		};
	}
}
