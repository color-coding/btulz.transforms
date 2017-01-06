package org.colorcoding.tools.btulz.test;

import java.io.File;

public final class Environment extends org.colorcoding.tools.btulz.Environment {

	public static String getOutputFolder() {
		return Environment.getWorkingFolder() + File.separator + "out";
	}

	public static String getDomainFolder() {
		return Environment.getWorkingFolder() + File.separator + "domain";
	}

	public static String getSqlsFolder() {
		return Environment.getWorkingFolder() + File.separator + "sqls";
	}

	public static String getXmlModelsFileOld() {
		return "//org//colorcoding//tools//btulz//test//transformers//domain_models_old.xml".replace("//",
				File.separator);
	}

	public static String getExcelModelsFile() {
		return "//classes//templates//domain_models_template_v4.0.xlsx".replace("//", File.separator);
	}
}
