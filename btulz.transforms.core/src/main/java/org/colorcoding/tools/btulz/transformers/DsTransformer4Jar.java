package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IDomain;

/**
 * 分析jar包中的数据结构文件，并创建到数据库
 * 
 * @author Niuren.Zhu
 *
 */
public class DsTransformer4Jar extends DsTransformer {

	@Override
	public void addDomains(File file) throws Exception {
		if (!file.isFile() || !file.exists()) {
			return;
		}
		if (file.getName().toLowerCase().endsWith(".jar")) {
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			if (jarEntries != null) {
				while (jarEntries.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
					String name = jarEntry.getName().toLowerCase();
					if (name.startsWith("datastructures/ds") && name.endsWith(".xml")) {
						try {
							InputStream inputStream = jarFile.getInputStream(jarEntry);
							XmlTransformer xmlTransformer = new XmlTransformer();
							xmlTransformer.load(inputStream);
							Environment.getLogger().info(String.format("jar entry [%s] has [%s] count domain. ", name,
									xmlTransformer.getWorkingDomains().length));
							for (IDomain domain : xmlTransformer.getWorkingDomains()) {
								this.addDomains(domain);
							}
						} catch (Exception e) {
							Environment.getLogger().error(e);
						}
					}
				}
			}
			jarFile.close();
		} else {
			super.addDomains(file);
		}
	}

}
