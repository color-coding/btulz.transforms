package org.colorcoding.tools.btulz.bobas.transformer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.ParserConfigurationException;

import org.colorcoding.ibas.bobas.bo.BusinessObject;
import org.colorcoding.ibas.bobas.bo.IBusinessObject;
import org.colorcoding.ibas.bobas.serialization.ISerializer;
import org.colorcoding.ibas.bobas.serialization.SerializationFactory;
import org.colorcoding.tools.btulz.transformer.TransformException;
import org.xml.sax.SAXException;

/**
 * 数据转换者，分析文件中数据并写入业务仓库
 * 
 * 仓库信息来着配置文件
 * 
 * @author Niuren.Zhu
 *
 */
public class DataTransformer4Jar extends DataTransformer {

	@Override
	protected List<IBusinessObject> analysisData(File file)
			throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException, TransformException {
		if (file.isFile()
				&& (file.getName().toLowerCase().endsWith(".jar") || file.getName().toLowerCase().endsWith(".war"))) {
			ArrayList<IBusinessObject> bos = new ArrayList<>();
			JarFile jarFile = new JarFile(file);
			try {
				ISerializer serializer = SerializationFactory.createManager().create("xml");
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				if (jarEntries != null) {
					ArrayList<JarEntry> JarEntryList = new ArrayList<>();
					while (jarEntries.hasMoreElements()) {
						JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
						if (jarEntry.isDirectory()) {
							continue;
						}
						String name = jarEntry.getName().toLowerCase();
						if (name.indexOf("/") > 0) {
							name = name.substring(name.lastIndexOf("/") + 1);
						}
						if (!name.startsWith("bo.")) {
							continue;
						}
						if (!name.endsWith(".xml")) {
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
						String boName = this.getClassName(jarFile.getInputStream(jarEntry));
						Class<?> boType = this.getClass(boName);
						IBusinessObject bo = (IBusinessObject) serializer.deserialize(
								this.applyVariables(jarFile.getInputStream(jarEntry)), BusinessObject.class, boType);
						if (bo != null) {
							bos.add(bo);
						}
					}
				}
			} finally {
				jarFile.close();
			}
			return bos;
		} else {
			return super.analysisData(file);
		}
	}

}
