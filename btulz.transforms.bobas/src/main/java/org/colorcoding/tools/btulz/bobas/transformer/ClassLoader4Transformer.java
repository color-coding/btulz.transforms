package org.colorcoding.tools.btulz.bobas.transformer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.Environment;

/**
 * 业务对象类型加载器
 *
 * 继承URLClassLoader，用于从指定JAR包和目录中加载业务对象类。
 *
 * 加载策略（父优先委派）： 1. 检查是否已加载 2. 优先由父加载器加载，保证同一包的类由同一加载器加载，避免IllegalAccessError 3.
 * 父加载器找不到时，才由自身从URL路径中加载
 *
 * @author manager
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

	/**
	 * 缓存的类名列表，避免重复扫描JAR
	 */
	private List<String> cachedClassNames;

	/**
	 * 获取可加载的类名列表
	 *
	 * @return 类全限定名的可迭代对象
	 */
	public Iterable<String> getClassNames() {
		if (this.cachedClassNames != null) {
			return this.cachedClassNames;
		}
		List<String> classNames = new ArrayList<>();
		for (URL url : getURLs()) {
			try {
				File file = new File(java.net.URLDecoder.decode(url.getPath(), "UTF-8"));
				if (!file.exists()) {
					continue;
				}
				if (file.getName().toLowerCase().endsWith(".jar")) {
					try (JarFile jarFile = new JarFile(file)) {
						Enumeration<JarEntry> jarEntries = jarFile.entries();
						while (jarEntries.hasMoreElements()) {
							JarEntry jarEntry = jarEntries.nextElement();
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
				} else if (file.isDirectory()) {
					collectClassNames(file, file, classNames);
				}
			} catch (Exception e) {
				Environment.getLogger().error(String.format("scan classes from [%s] failed: %s", url, e.getMessage()));
			}
		}
		this.cachedClassNames = classNames;
		return classNames;
	}

	/**
	 * 递归收集目录下的class文件名
	 *
	 * @param baseDir    根目录，用于计算包名前缀
	 * @param current    当前目录
	 * @param classNames 收集结果的列表
	 */
	private void collectClassNames(File baseDir, File current, List<String> classNames) {
		File[] files = current.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				collectClassNames(baseDir, file, classNames);
			} else if (file.getName().toLowerCase().endsWith(".class")) {
				String relativePath = baseDir.toPath().relativize(file.toPath()).toString();
				String name = relativePath.replace(File.separatorChar, '.').replace(".class", "");
				classNames.add(name);
			}
		}
	}

	/**
	 * 从输入流定义类
	 *
	 * @param name        类的全限定名
	 * @param inputStream 类的字节流
	 * @return 定义的Class对象
	 * @throws IOException            读取流失败
	 * @throws ClassFormatError       类格式错误
	 * @throws NoClassDefFoundError   类定义找不到
	 * @throws ClassNotFoundException 类找不到
	 */
	protected Class<?> defineClass(String name, InputStream inputStream)
			throws IOException, ClassFormatError, NoClassDefFoundError, ClassNotFoundException {
		byte[] buffer = new byte[8192];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int len; (len = inputStream.read(buffer)) != -1;) {
			baos.write(buffer, 0, len);
		}
		byte[] classBytes = baos.toByteArray();
		return this.defineClass(name, classBytes, 0, classBytes.length);
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		// 首先，查看是否已经加载
		Class<?> type = this.findLoadedClass(name);
		if (type != null) {
			return type;
		}
		// 如果父加载器已经加载了同包的类，则此类也交由父加载器加载，
		// 避免同包类分属不同ClassLoader导致IllegalAccessError
		try {
			type = this.getParent().loadClass(name);
			Environment.getLogger()
					.debug(String.format("found %s by %s.", name, this.getParent().getClass().getSimpleName()));
			return type;
		} catch (ClassNotFoundException e) {
			// 父加载器未找到，由子加载器加载
		}
		Environment.getLogger().debug(String.format("to find %s by %s.", name, this.getClass().getSimpleName()));
		return super.findClass(name);
	}

	/**
	 * 重写loadClass，实现带同包一致性保护的类加载。
	 *
	 * 加载策略： 1. 检查是否已加载 2. 父加载器优先（保证框架类由父加载器统一加载，避免同包跨加载器访问） 3.
	 * 父加载器找不到时，由自身从URL路径中加载
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// 检查是否已加载
		Class<?> type = this.findLoadedClass(name);
		if (type != null) {
			return type;
		}
		// 父加载器优先，保证同包类由同一加载器加载
		try {
			return this.getParent().loadClass(name);
		} catch (ClassNotFoundException e) {
			// 父加载器未找到，继续
		}
		// 自身查找
		return this.findClass(name);
	}

	@Override
	public void close() throws IOException {
		this.cachedClassNames = null;
		super.close();
	}
}