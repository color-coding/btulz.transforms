package org.colorcoding.tools.btulz.transformer;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformer.region.RegionDomain;

/**
 * 分析jar包中的sql，并在数据库中执行
 * 
 * @author Niuren.Zhu
 *
 */
public class SqlTransformer4Jar extends SqlTransformer {

	@Override
	public void transform() throws Exception {
		File file = new File(this.getSqlFile());
		if (!file.isFile() || !file.exists()) {
			return;
		}
		if (file.getName().toLowerCase().endsWith(".jar")) {
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			if (jarEntries != null) {
				while (jarEntries.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
					if (jarEntry.isDirectory()) {
						continue;
					}
					String name = jarEntry.getName().toLowerCase();
					if (name.startsWith("datastructures/sql_") && name.endsWith(".xml")) {
						if (this.getSqlFilter() != null) {
							if (name.indexOf(this.getSqlFilter()) < 0) {
								continue;
							}
						}
						try {
							Environment.getLogger().info(String.format("parsing jar entry [%s]. ", name));
							InputStream inputStream = jarFile.getInputStream(jarEntry);
							RegionDomain template = new RegionDomain();
							template.setEncoding("utf-8");
							template.parse(inputStream);
							File outputFile = new File(this.getOutputFile(name.replace("datastructures/sql_", "sql_")));
							template.export(this.getRuntimeParameters(), outputFile);
							this.execute(outputFile);
						} catch (Exception e) {
							Environment.getLogger().error(e);
						}
					}
				}
			}
			jarFile.close();
		} else {
			// 普通文件
			super.transform();
		}
	}
}
