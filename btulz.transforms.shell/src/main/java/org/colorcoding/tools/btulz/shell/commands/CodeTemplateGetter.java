package org.colorcoding.tools.btulz.shell.commands;

import java.io.File;
import java.util.jar.JarEntry;

/**
 * 模板获取者
 * 
 * @author Niuren.Zhu
 *
 */
public class CodeTemplateGetter extends TemplateGetter {

	private static final String TEMPLATE_FOLDER_SIGN = "code";
	private static final int TEMPLATE_FOLDER_LEVEL = 3;

	@Override
	protected ValidValue getValidValue(JarEntry jarEntry) {
		if (!jarEntry.isDirectory()) {
			return null;
		}
		String folder_sign = TEMPLATE_FOLDER_SIGN + "/";
		if (jarEntry.getName().equals(folder_sign)) {
			return null;
		}
		if (!jarEntry.getName().startsWith(folder_sign)) {
			return null;
		}
		if (jarEntry.getName().split("/").length != TEMPLATE_FOLDER_LEVEL) {
			// 仅考虑三级目录，code/eclipse/ibas_classic
			return null;
		}
		String value = jarEntry.getName().substring(folder_sign.length());
		return new ValidValue(value, value + "@jar");
	}

	@Override
	protected ValidValue getValidValue(File file) {
		if (!file.isDirectory()) {
			return null;
		}
		String path = file.getPath();
		String folder_sign = TEMPLATE_FOLDER_SIGN + File.separator;
		if (!path.startsWith(this.getWorkFolder() + folder_sign)) {
			// 不是code目录下的
			return null;
		}
		path = path.replace(this.getWorkFolder(), File.separator);// 工作目录多了一个分隔符
		int count = 0;
		char separator = File.separator.toCharArray()[0];
		for (char item : path.toCharArray()) {
			if (item == separator) {
				count++;
			}
		}
		if (count != TEMPLATE_FOLDER_LEVEL) {
			// 仅考虑三级目录，code/eclipse/ibas_classic
			return null;
		}
		return new ValidValue(file.getPath(), path.substring(folder_sign.length() + 1) + "@folder");
	}

}
