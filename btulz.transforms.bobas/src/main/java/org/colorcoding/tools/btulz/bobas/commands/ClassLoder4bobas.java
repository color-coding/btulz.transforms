package org.colorcoding.tools.btulz.bobas.commands;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务对象类型加载器
 * 
 * @author manager
 *
 */
public class ClassLoder4bobas extends URLClassLoader {

	public ClassLoder4bobas(URL[] urls) {
		super(urls);
	}

	public void loadClasses() {
		for (URL item : this.getURLs()) {
			if (item.getProtocol().equals("file")) {
				try {
					File file = new File(java.net.URLDecoder.decode(item.getPath(), "UTF-8"));
					this.loadClasses(file, file.getPath() + File.separator);
				} catch (Exception e) {
				}
			} else if (item.getProtocol().equals("jar")) {

			}
		}
	}

	private void loadClasses(File file, String root) throws ClassFormatError, IOException {
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				this.loadClasses(item, root);
			}
		} else if (file.isFile()) {
			if (file.getName().endsWith(".class")) {
				FileInputStream inputStream = new FileInputStream(file);
				String name = file.getPath().replace(root, "");
				name = name.replace(".class", "");
				name = name.replace("\\", ".");
				this.defineClass(name, inputStream);
				inputStream.close();
			}
		}
	}

	protected Class<?> defineClass(String name, InputStream inputStream) throws IOException, ClassFormatError {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int data = inputStream.read();
		while (data != -1) {
			buffer.write(data);
			data = inputStream.read();
		}
		byte[] bytes = buffer.toByteArray();
		return this.defineClass(name, bytes, 0, bytes.length);
	}

	public List<String> getClassNames(String name) {
		ArrayList<String> names = new ArrayList<>();
		StringBuilder className = new StringBuilder();
		if (!name.startsWith("\\")) {
			className.append("\\");
		}
		className.append(name);
		if (!name.endsWith(".class")) {
			className.append(".class");
		}
		for (URL item : this.getURLs()) {
			if (item.getProtocol().equals("file")) {
				try {
					File file = new File(java.net.URLDecoder.decode(item.getPath(), "UTF-8"));
					for (String tmp : this.getClassNames(file, className.toString())) {
						tmp = tmp.replace(file.getPath(), "");
						if (tmp.startsWith("\\")) {
							tmp = tmp.substring(1);
						}
						tmp = tmp.replace("\\", ".");
						names.add(tmp);
						try {
							Class<?> type = this.loadClass(tmp, false);
							System.err.println(type.getName());
						} catch (Exception e) {
							System.err.println(e);
						}
					}
				} catch (Exception e) {
				}
			} else if (item.getProtocol().equals("jar")) {

			}
		}
		return names;
	}

	private List<String> getClassNames(File file, String name) {
		ArrayList<String> names = new ArrayList<>();
		if (file.isDirectory()) {
			for (File item : file.listFiles()) {
				names.addAll(this.getClassNames(item, name));
			}
		} else if (file.isFile()) {
			String tmp = file.getPath().toLowerCase();
			if (tmp.endsWith(name)) {
				names.add(file.getPath());
			}
		}
		return names;
	}

}
