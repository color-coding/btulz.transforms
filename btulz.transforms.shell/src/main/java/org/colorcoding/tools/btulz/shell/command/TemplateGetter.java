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
public class TemplateGetter implements ValidValuesGetter {

	public static final String DEFINITION_NAME_TEMPLATE = "Template";
	public static final String DEFINITION_NAME_WORK_FILE = "WorkFile";

	protected String[] parsingDefinitions(String name, String definitions) {
		ArrayList<String> values = new ArrayList<String>();
		if (definitions != null) {
			String[] temps = definitions.split(";");
			for (String item : temps) {
				if (item.startsWith(name + "=")) {
					values.add(item.substring(item.indexOf("=") + 1));
				}
			}
		}
		return values.toArray(new String[] {});
	}

	@Override
	public ValidValue[] get(String definitions) {
		ArrayList<ValidValue> values = new ArrayList<>();
		for (String template : this.parsingDefinitions(DEFINITION_NAME_TEMPLATE, definitions)) {
			if (template == null || template.isEmpty()) {
				continue;
			}
			for (String workFile : this.parsingDefinitions(DEFINITION_NAME_WORK_FILE, definitions)) {
				if (workFile == null) {
					continue;
				}
				if (workFile.indexOf(File.pathSeparator) < 0) {
					workFile = Environment.getWorkingFolder() + File.separator + workFile;
				}
				File file = new File(workFile);
				if (file.isDirectory()) {
					for (String item : this.getMatchingValues(file, template)) {
						values.add(new ValidValue(item, item + "@folder"));
					}
				} else if (file.isFile() && file.getName().toLowerCase().endsWith(".jar")) {
					JarFile jarFile = null;
					try {
						jarFile = new JarFile(file);
						for (String item : this.getMatchingValues(jarFile, template)) {
							values.add(new ValidValue(item, item + "@jar"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (jarFile != null) {
								jarFile.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return values.toArray(new ValidValue[] {});
	}

	private List<String> getMatchingValues(JarFile jarFile, String template) {
		List<String> values = new ArrayList<>();
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		if (jarEntries != null) {
			while (jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				if (!jarEntry.getName().startsWith(template)) {
					continue;
				}
				if (!jarEntry.isDirectory()) {
					continue;
				}
				if (jarEntry.getName().split("/").length > 2) {
					continue;
				}
				String value = jarEntry.getName().substring(template.length() + 1);
				if (value == null || value.isEmpty()) {
					continue;
				}
				if (value.endsWith("/")) {
					value = value.substring(0, value.length() - 1);
				}
				values.add(value);
			}
		}
		return values;
	}

	/**
	 * 获取有效值
	 * 
	 * @param file
	 *            文件实体
	 * @return null表示不存在
	 */
	private List<String> getMatchingValues(File file, String template) {
		List<String> values = new ArrayList<>();
		for (File item : file.listFiles()) {
			if (!item.isDirectory()) {
				continue;
			}
			if (!item.getName().startsWith(template)) {
				continue;
			}
			for (File subItem : item.listFiles()) {
				if (!item.isDirectory()) {
					continue;
				}
				values.add(subItem.getName());
			}
		}
		return values;
	}

}
