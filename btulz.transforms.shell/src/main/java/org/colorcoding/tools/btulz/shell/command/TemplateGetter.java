package org.colorcoding.tools.btulz.shell.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 模板获取者
 * 
 * @author Niuren.Zhu
 *
 */
public abstract class TemplateGetter implements ValidValuesGetter {

	private String workFile;

	public final String getWorkFile() {
		return workFile;
	}

	public final void setWorkFile(String workFile) {
		this.workFile = workFile;
		// 补全路径
		if (this.workFile != null && this.workFile.indexOf(File.pathSeparator) < 0) {
			this.workFile = Environment.getWorkingFolder() + File.separator + this.workFile;
		}
	}

	@Override
	public ValidValue[] get() {
		ArrayList<ValidValue> values = new ArrayList<>();
		if (this.getWorkFile() != null && !this.getWorkFile().isEmpty()) {
			File workFile = new File(this.workFile);
			if (workFile.isDirectory()) {
				File[] files = workFile.listFiles();
				if (files != null) {
					for (File file : files) {
						ValidValue validValue = this.getValidValue(file);
						if (validValue != null) {
							values.add(validValue);
						}
					}
				}
			} else if (workFile.isFile() && workFile.getName().toLowerCase().endsWith(".jar")) {
				// 开始分析jar包
				JarFile jarFile = null;
				try {
					jarFile = new JarFile(workFile);
					Enumeration<JarEntry> jarEntries = jarFile.entries();
					if (jarEntries != null) {
						while (jarEntries.hasMoreElements()) {
							ValidValue validValue = this.getValidValue((JarEntry) jarEntries.nextElement());
							if (validValue != null) {
								values.add(validValue);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (jarFile != null) {
						try {
							jarFile.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return values.toArray(new ValidValue[] {});
	}

	/**
	 * 获取有效值
	 * 
	 * @param jarEntry
	 *            jar包实体
	 * @return null表示不存在
	 */
	protected abstract ValidValue getValidValue(JarEntry jarEntry);

	/**
	 * 获取有效值
	 * 
	 * @param file
	 *            文件实体
	 * @return null表示不存在
	 */
	protected abstract ValidValue getValidValue(File file);
}
