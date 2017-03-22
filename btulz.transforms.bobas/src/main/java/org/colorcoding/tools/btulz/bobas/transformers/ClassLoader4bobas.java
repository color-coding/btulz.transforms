package org.colorcoding.tools.btulz.bobas.transformers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;
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
public class ClassLoader4bobas extends URLClassLoader {

	public ClassLoader4bobas(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public ClassLoader4bobas(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public ClassLoader4bobas(URL[] urls) {
		super(urls);
	}

	private volatile HashMap<String, URL> classesMap;

	public synchronized Map<String, URL> getClassesMap() {
		if (this.classesMap == null) {
			this.classesMap = new HashMap<>();
		}
		return this.classesMap;
	}

	private static String CLASSES_FOLDER = String.format("%1$sclasses%1$s", File.separator);

	public void init() throws IOException, ClassNotFoundException {
		for (URL item : this.getURLs()) {
			if (item == null) {
				continue;
			}
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
						String name = tmp;
						if (!name.equalsIgnoreCase(file.getPath())) {
							name = tmp.replace(file.getPath(), "");
						} else if (name.indexOf(CLASSES_FOLDER) > 0) {
							// 加载本模块.class资源
							name = name.substring(name.indexOf(CLASSES_FOLDER) + CLASSES_FOLDER.length());
						}
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

	protected Class<?> defineClass(String name, InputStream inputStream)
			throws IOException, ClassFormatError, NoClassDefFoundError, ClassNotFoundException {
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

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> type = this.findLoadedClass(name);
		if (type != null) {
			return type;
		}
		if (this.getClassesMap().containsKey(name)) {
			URL url = this.getClassesMap().get(name);
			try {
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream inputStream = connection.getInputStream();
				try {
					return this.defineClass(name, inputStream);
				} catch (LinkageError e1) {
					// 加载出错，可能缺少连接引用
					// 加载连接引用
					this.findClass(e1.getMessage());
					// 重新调用加载
					return this.findClass(name);
				}
			} catch (IOException e2) {
				throw new ClassNotFoundException(e2.getMessage());
			}
		}
		return super.findClass(name);
	}
}
