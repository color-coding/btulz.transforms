package org.colorcoding.tools.btulz.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

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
	 * 以模板文件为准，不存则为utf-8
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getEncoding() throws UnsupportedEncodingException {
		if (this.encoding == null) {
			if (this.getTemplateFile() != null)
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
	public void export(Parameters parameters, String outputFile) throws Exception {
		this.export(parameters, new File(outputFile));
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
	public void export(Parameters parameters, File outputFile) throws Exception {
		if (!outputFile.exists()) {
			outputFile.getParentFile().mkdirs();
			outputFile.createNewFile();
		} else {
			outputFile.delete();
			outputFile.createNewFile();
		}
		this.export(parameters,
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), this.getEncoding())));
	}

	/**
	 * 模板已初始化标记
	 */
	private boolean initialized = false;

	private void parse() throws UnsupportedEncodingException, Exception {
		File tpltFile = new File(this.getTemplateFile());
		if (!tpltFile.exists() || !tpltFile.isFile()) {
			throw new FileNotFoundException(this.getTemplateFile());
		}
		this.parse(new FileInputStream(tpltFile));// 解析模板
	}

	/**
	 * 解析模板
	 * 
	 * @param template
	 *            模板流，注意编码方式
	 * @throws Exception
	 */
	public void parse(InputStream template) throws Exception {
		this.parse(new InputStreamReader(template, this.getEncoding()));// 解析模板
	}

	/**
	 * 解析模板
	 * 
	 * @param template
	 *            模板
	 * @throws Exception
	 */
	public void parse(InputStreamReader template) throws Exception {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(template);
			this.parse(reader);// 解析模板
			initialized = true;
			reader.close();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * 根据模板输出
	 * 
	 * @param parameters
	 *            参数
	 * @param writer
	 *            输出文件
	 * @throws Exception
	 */
	public void export(Parameters parameters, BufferedWriter writer) throws Exception {
		try {
			if (!initialized) {
				this.parse();
			}
			this.export(writer, parameters);// 输出数据
			writer.close();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	@Override
	protected Iterator<Parameter> getRegionParameters(Parameters pars) throws InvalidParameterException {
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

}
