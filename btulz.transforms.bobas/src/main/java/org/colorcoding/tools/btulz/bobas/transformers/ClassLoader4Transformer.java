package org.colorcoding.tools.btulz.bobas.transformers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.Environment;

/**
 * 业务对象类型加载器
 * 
 * @author manager
 *
 */
public class ClassLoader4Transformer extends URLClassLoader {

	public ClassLoader4Transformer(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public ClassLoader4Transformer(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public ClassLoader4Transformer(URL[] urls) {
		super(urls);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name);
	}

	public Iterable<String> getClassNames() {
		return new Iterable<String>() {

			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {
					/**
					 * 类库文件的迭代器
					 */
					private Iterator<URL> urlIterator = new Iterator<URL>() {
						int index = 0;

						@Override
						public boolean hasNext() {
							if (index < getURLs().length) {
								return true;
							}
							return false;
						}

						@Override
						public URL next() {
							URL item = getURLs()[index];
							index++;
							return item;
						}
					};

					private Iterator<String> classNameIterator;

					@Override
					public boolean hasNext() {
						if (this.classNameIterator != null) {
							if (this.classNameIterator.hasNext()) {
								return true;
							}
						}
						if (this.urlIterator.hasNext()) {
							return true;
						}
						return false;
					}

					@Override
					public String next() {
						if (this.classNameIterator != null && this.classNameIterator.hasNext()) {
							return this.classNameIterator.next();
						} else {
							try {
								while (this.urlIterator.hasNext()) {
									ArrayList<String> classNames = new ArrayList<>();
									URL url = this.urlIterator.next();
									File file = new File(java.net.URLDecoder.decode(url.getPath(), "UTF-8"));
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
													classNames.add(name);
												}
											}
										}
										jarFile.close();
									}
									this.classNameIterator = classNames.iterator();
									return this.next();
								}
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
						return null;
					}

				};
			}
		};
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
		// 首先，查看是否已经加载
		Class<?> type = this.findLoadedClass(name);
		if (type != null) {
			return type;
		}
		try {
			// 父项加载失败，子项加载
			Environment.getLogger().debug(String.format("to find %s by %s.", name, this.getClass().getSimpleName()));
			return super.findClass(name);
		} catch (ClassNotFoundException e) {
			// 没有加载，尝试父项加载
			Environment.getLogger()
					.debug(String.format("to find %s by %s.", name, this.getParent().getClass().getSimpleName()));
			return this.getParent().loadClass(name);
		}
	}
}
