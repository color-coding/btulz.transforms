package org.colorcoding.tools.btulz.bobas.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.collections4.map.HashedMap;
import org.colorcoding.tools.btulz.Environment;

/**
 * 业务对象类型加载器
 * 
 * @author manager
 *
 */
public class ClassLoder4bobas extends ClassLoader {

	private String workFolder;

	public String getWorkFolder() {
		return workFolder;
	}

	public void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	protected Class<?> defineClass(InputStream inputStream) throws IOException, ClassFormatError {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int data = inputStream.read();
		while (data != -1) {
			buffer.write(data);
			data = inputStream.read();
		}
		byte[] bytes = buffer.toByteArray();
		inputStream.close();
		return this.defineClass(null, bytes, 0, bytes.length);
	}

	protected void registerClass(Class<?> type) throws ClassNotFoundException {
		if (type == null) {
			return;
		}
		if (type.isInterface()) {
			return;
		}
		if (type.isAnnotation()) {
			return;
		}
		if (type.isArray()) {
			return;
		}
		Environment.getLogger().debug(String.format("registered class [%s].", type.getName()));
		XmlSeeAlso seeAlso = type.getAnnotation(XmlSeeAlso.class);
		if (seeAlso != null) {
			// 加载标记已知类型
			for (Class<?> item : seeAlso.value()) {
				Environment.getLogger().debug(String.format("registered reference class [%s].", item.getName()));
				this.loadClass(item.getName());
			}
		}
		XmlType xmlType = type.getAnnotation(XmlType.class);
		if (xmlType == null) {
			return;
		}
		String xmlName = type.getSimpleName();
		if (!xmlType.name().equals("##default")) {
			xmlName = xmlType.name();
		}
		if (!xmlType.namespace().equals("##default")) {
			if (xmlType.namespace().endsWith("/")) {
				xmlName = String.format("%s%s", xmlType.namespace(), xmlName);
			} else {
				xmlName = String.format("%s/%s", xmlType.namespace(), xmlName);
			}
		}
		this.getClassMap().put(xmlName, type);
	}

	private Map<String, Class<?>> classMap;

	protected Map<String, Class<?>> getClassMap() {
		if (this.classMap == null) {
			this.classMap = new HashedMap<String, Class<?>>();
		}
		return this.classMap;
	}

	public Class<?> getClass(String name) throws ClassNotFoundException, IOException {
		if (this.getClassMap().containsKey(name)) {
			return this.getClassMap().get(name);
		}
		return this.getClass(new File(this.getWorkFolder()), name);
	}

	protected Class<?> getClass(File file, String name) throws IOException, ClassNotFoundException, ClassFormatError {
		if (file.isDirectory()) {
			System.err.println(file.getPath());
			for (File item : file.listFiles()) {
				Class<?> tmpClass = this.getClass(item, name);
				if (tmpClass != null) {
					return tmpClass;
				}
			}
		} else if (file.isFile()) {
			String fileName = file.getName().toLowerCase();
			if (fileName.endsWith(".jar")) {
				JarFile jarFile = new JarFile(file);
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				if (jarEntries != null) {
					while (jarEntries.hasMoreElements()) {
						JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
						if (jarEntry.isDirectory()) {
							continue;
						}
						InputStream inputStream = jarFile.getInputStream(jarEntry);
						// 注册类型
						this.registerClass(this.defineClass(inputStream));
						// 注册成功，则返回
						if (this.getClassMap().containsKey(name)) {
							return this.getClassMap().get(name);
						}
					}
				}
				jarFile.close();
			} else if (fileName.endsWith(".class")) {
				InputStream inputStream = new FileInputStream(file);
				// 注册类型
				this.registerClass(this.defineClass(inputStream));
				// 注册成功，则返回
				if (this.getClassMap().containsKey(name)) {
					return this.getClassMap().get(name);
				}
			}
		}
		return null;
	}

}
