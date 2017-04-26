package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.models.IDomain;
import org.colorcoding.tools.btulz.util.ArrayList;

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
				ArrayList<JarEntry> JarEntryList = new ArrayList<>();
				while (jarEntries.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
					if (jarEntry.isDirectory()) {
						continue;
					}
					String name = jarEntry.getName().toLowerCase();
					if (!name.startsWith("datastructures/ds_") || !name.endsWith(".xml")) {
						continue;
					}
					JarEntryList.add(jarEntry);
				}
				// 排序，创建数据结构需要顺序
				JarEntryList.sort(new Comparator<JarEntry>() {
					@Override
					public int compare(JarEntry o1, JarEntry o2) {
						String name1 = o1.getName().toLowerCase();
						String name2 = o2.getName().toLowerCase();
						return name1.compareTo(name2);
					}
				});
				// 读取内容
				for (JarEntry jarEntry : JarEntryList) {
					try {
						InputStream inputStream = jarFile.getInputStream(jarEntry);
						XmlTransformer xmlTransformer = new XmlTransformer();
						xmlTransformer.load(inputStream);
						Environment.getLogger().info(String.format("jar entry [%s] has [%s] count domain. ",
								jarEntry.getName(), xmlTransformer.getWorkingDomains().length));
						for (IDomain domain : xmlTransformer.getWorkingDomains()) {
							this.addDomains(domain);
						}
					} catch (Exception e) {
						Environment.getLogger().error(e);
					}
				}
			}
			jarFile.close();
		} else {
			super.addDomains(file);
		}
	}

}
