package org.colorcoding.tools.btulz.transformers;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.regions.RegionDomain;

/**
 * 分析jar包中的sql，并在数据库中执行
 * 
 * @author Niuren.Zhu
 *
 */
public class SqlTransformer4Jar extends SqlTransformer {
	private Unmarshaller unmarshaller = null;

	private Unmarshaller createUnmarshaller() throws JAXBException {
		if (unmarshaller == null) {
			JAXBContext context = JAXBContext.newInstance(DataStructureOrchestration.class);
			unmarshaller = context.createUnmarshaller();
		}
		return this.unmarshaller;
	}

	private String sqlFilter;

	public String getSqlFilter() {
		return sqlFilter;
	}

	public void setSqlFilter(String sqlFilter) {
		this.sqlFilter = sqlFilter;
	}

	@Override
	public void transform() throws Exception {
		File file = new File(this.getTemplateFile());
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
							Environment.getLogger().info(
									String.format("try to execute orchestration file [%s].", outputFile.getPath()));
							DataStructureOrchestration orchestration = (DataStructureOrchestration) this
									.createUnmarshaller().unmarshal(outputFile);
							orchestration.execute();
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
