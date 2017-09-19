package org.colorcoding.tools.btulz.shell.commands;

import java.io.File;
import java.util.jar.JarEntry;

/**
 * 模板获取者
 * 
 * @author Niuren.Zhu
 *
 */
public class DSTemplateGetter extends TemplateGetter {

	private static final String TEMPLATE_FOLDER_SIGN = "ds";

	@Override
	protected ValidValue getValidValue(JarEntry jarEntry) {
		if (jarEntry.isDirectory()) {
			return null;
		}
		if (!jarEntry.getName().endsWith(".xml")) {
			return null;
		}
		String folder_sign = TEMPLATE_FOLDER_SIGN + "/ds_";
		if (!jarEntry.getName().startsWith(folder_sign)) {
			return null;
		}
		String value = jarEntry.getName().substring(TEMPLATE_FOLDER_SIGN.length() + 1);
		return new ValidValue(value, value + "@jar");
	}

	@Override
	protected ValidValue getValidValue(File file) {
		if (file.isDirectory()) {
			return null;
		}
		if (!file.getName().toLowerCase().endsWith(".xml")) {
			return null;
		}
		String folder_sign = TEMPLATE_FOLDER_SIGN;
		if (!file.getName().toLowerCase().startsWith(folder_sign)) {
			return null;
		}
		return new ValidValue(file.getPath(), file.getName() + "@folder");
	}

}
