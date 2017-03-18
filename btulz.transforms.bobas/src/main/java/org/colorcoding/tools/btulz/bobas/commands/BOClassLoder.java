package org.colorcoding.tools.btulz.bobas.commands;

import java.io.File;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.commons.collections4.map.HashedMap;

/**
 * 业务对象类型加载器
 * 
 * @author manager
 *
 */
public class BOClassLoder extends ClassLoader {

	private String workFolder;

	public String getWorkFolder() {
		return workFolder;
	}

	public void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {

		return super.loadClass(name);
	}

	private Map<String, Class<?>> classMap;

	protected Map<String, Class<?>> getClassMap() {
		if (this.classMap == null) {
			this.classMap = new HashedMap<String, Class<?>>();
		}
		return this.classMap;
	}

	public Class<?> getClass(String name) throws ClassNotFoundException {
		if (this.getClassMap().containsKey(name)) {
			return this.getClassMap().get(name);
		}
		return this.getClass(new File(this.getWorkFolder()), name);
	}

	protected Class<?> getClass(File file, String name) throws ClassNotFoundException {
		if (file.isDirectory()) {

		} else if (file.isFile()) {
			String fileName = file.getName().toLowerCase();
			if (fileName.endsWith(".jar")) {

			} else if (fileName.endsWith(".class")) {
				/*
				 * URL myUrl = new URL(fileName); URLConnection connection =
				 * myUrl.openConnection(); InputStream input =
				 * connection.getInputStream(); ByteArrayOutputStream buffer =
				 * new ByteArrayOutputStream(); int data = input.read(); while
				 * (data != -1) { buffer.write(data); data = input.read(); }
				 * input.close(); byte[] classData = buffer.toByteArray();
				 * return defineClass(noSuffix(className), classData, 0,
				 * classData.length);
				 */
			}
		}
		throw new ClassNotFoundException();
	}

	protected Class<?> getClass(JarFile jarFile) throws ClassNotFoundException {

		throw new ClassNotFoundException();
	}

}
