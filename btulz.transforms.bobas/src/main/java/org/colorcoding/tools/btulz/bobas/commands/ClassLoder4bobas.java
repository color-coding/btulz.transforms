package org.colorcoding.tools.btulz.bobas.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 业务对象类型加载器
 * 
 * @author manager
 *
 */
public class ClassLoder4bobas extends URLClassLoader {

	public ClassLoder4bobas(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	private Map<String, URL> classesMap;

	public Map<String, URL> getClassesMap() {
		if (this.classesMap == null) {
			this.classesMap = new HashMap<>();
		}
		return this.classesMap;
	}

	public void init() throws IOException {
		for (URL item : this.getURLs()) {
			if (item.getProtocol().equals("file")) {
				File file = new File(java.net.URLDecoder.decode(item.getPath(), "UTF-8"));
				if (file.getName().endsWith(".jar")) {
					JarFile jarFile = new JarFile(file);
					Enumeration<JarEntry> jarEntries = jarFile.entries();
					if (jarEntries != null) {
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
							if (jarEntry.isDirectory()) {
								continue;
							}
							if (jarEntry.getName().toLowerCase().endsWith(".class")) {
								String name = jarEntry.getName().replace("/", ".");
								name = name.replace(".class", "");
								this.getClassesMap().put(name, new URL(String.format("jar:file:%s%s%s%s",
										file.getPath(), "!", "/", jarEntry.toString())));
							}
						}
					}
					jarFile.close();
				} else {
					for (String tmp : this.getClasses(file)) {
						String name = tmp.replace(file.getPath(), "");
						if (name.startsWith(File.separator)) {
							name = name.substring(1);
						}
						name = name.replace(File.separator, ".");
						name = name.replace(".class", "");
						this.getClassesMap().put(name, new URL("file", "", tmp));
					}
				}
			}
		}
	}

	private List<String> getClasses(File file) throws IOException {
		ArrayList<String> names = new ArrayList<>();
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				names.addAll(this.getClasses(item));
			}
		} else if (file.isFile()) {
			String tmp = file.getPath().toLowerCase();
			if (tmp.endsWith(".class")) {
				names.add(file.getPath());
			}
		}
		return names;
	}

	protected Class<?> defineClass(String name, InputStream inputStream) throws IOException, ClassFormatError {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int data = inputStream.read();
		while (data != -1) {
			buffer.write(data);
			data = inputStream.read();
		}
		byte[] bytes = buffer.toByteArray();
		inputStream.close();
		return this.defineClass(name, bytes, 0, bytes.length);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (this.getClassesMap().containsKey(name)) {
			URL url = this.getClassesMap().get(name);
			try {
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream inputStream = connection.getInputStream();
				try {
					this.defineClass(name, inputStream);
				} catch (ClassFormatError e) {
					// 加载出错，可能缺少连接引用
					// 加载连接引用
					this.loadClass(e.getMessage());
					// 重新调用加载
					this.loadClass(name);
				}
			} catch (Exception e) {
				throw new ClassNotFoundException(e.getMessage());
			}
		}
		throw new ClassNotFoundException(name);
	}
}
