package org.colorcoding.tools.btulz.shell.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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

	private String workFolder;

	public final String getWorkFolder() {
		if (this.workFolder == null) {
			this.workFolder = Environment.getWorkingFolder();
		}
		if (!this.workFolder.endsWith(File.separator)) {
			// 自动补路径符
			this.workFolder = this.workFolder + File.separator;
		}
		return workFolder;
	}

	public final void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	private String[] jarFiles;

	public final String[] getJarFiles() {
		if (jarFiles == null) {
			jarFiles = new String[] { //
					"btulz.transforms.core-" // 核心jar包
			};
		}
		return jarFiles;
	}

	public final void setJarFiles(String[] jarFiles) {
		this.jarFiles = jarFiles;
	}

	@Override
	public ValidValue[] get() {
		ArrayList<ValidValue> values = new ArrayList<>();
		values.addAll(this.getJars());
		values.addAll(this.getFolders());
		return values.toArray(new ValidValue[] {});
	}

	private List<ValidValue> getJars() {
		ArrayList<ValidValue> values = new ArrayList<>();
		File workFolder = new File(this.getWorkFolder());
		File[] files = workFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (!file.isFile()) {
					continue;
				}
				if (!file.getName().toLowerCase().endsWith(".jar")) {
					continue;
				}
				// 判断jar包是否分析
				boolean done = false;
				String fileName = file.getName().toLowerCase();
				for (String string : this.getJarFiles()) {
					if (fileName.startsWith(string)) {
						done = true;
						break;
					}
				}
				if (!done) {
					continue;
				}
				// 开始分析jar包
				JarFile jarFile = null;
				try {
					jarFile = new JarFile(file);
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
		return values;
	}

	private List<ValidValue> getFolders() {
		return this.getFolders(new File(this.getWorkFolder()));
	}

	private List<ValidValue> getFolders(File workFolder) {
		ArrayList<ValidValue> values = new ArrayList<>();
		File[] files = workFolder.listFiles();
		if (files != null) {
			for (File file : files) {
				ValidValue validValue = this.getValidValue(file);
				if (validValue != null) {
					values.add(validValue);
				}
				if (file.isDirectory()) {
					values.addAll(this.getFolders(file));
				}
			}
		}
		return values;
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
