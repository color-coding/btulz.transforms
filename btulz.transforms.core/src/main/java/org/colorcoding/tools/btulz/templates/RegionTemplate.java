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

/**
 * 模板
 * 
 * @author Niuren.Zhu
 *
 */
public class RegionTemplate extends Region {

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

	private String outPutFile;

	/**
	 * 输出的文件
	 * 
	 * @return
	 */
	public String getOutPutFile() {
		return outPutFile;
	}

	public void setOutPutFile(String outPutFile) {
		this.outPutFile = outPutFile;
	}

	private String encoding = "utf-8";

	/**
	 * 文件编码
	 * 
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void parse() throws Exception {
		File tpltFile = new File(this.getTemplateFile());
		if (!tpltFile.exists() || !tpltFile.isFile()) {
			throw new FileNotFoundException(this.getTemplateFile());
		}
		File outFile = new File(this.getOutPutFile());
		if (!outFile.exists()) {
			outFile.getParentFile().mkdirs();
			outFile.createNewFile();
		}
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(tpltFile), this.getEncoding()));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), this.getEncoding()));
			this.parse(reader, writer);
			reader.close();
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
	protected Iterable<Parameter> getRegionParameters() throws InvalidParameterException {
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
