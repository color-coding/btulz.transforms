package org.colorcoding.tools.btulz.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包含释放资源的命令基类
 * 
 * @author Niuren.Zhu
 *
 * @param <C>
 */
public abstract class Command4Release<C> extends Command<C> {

	/**
	 * 释放资源命令
	 */
	public static final String ARGUMENT_NAME_RELEASE = "-Release";
	/**
	 * 返回值，5，错误，输入输出异常
	 */
	public static final int RETURN_VALUE_IO_EXCEPTION = 5;

	protected File getJarFile() throws UnsupportedEncodingException {
		String jarFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		File file = new File(java.net.URLDecoder.decode(jarFilePath, "UTF-8"));
		return file;
	}

	/**
	 * 将资源释放到文件夹
	 * 
	 * @param resource
	 *            资源名称
	 * @throws IOException
	 */
	protected void writeResources(String resource, File outFolder) throws IOException {
		if (resource == null) {
			resource = "";
		}
		File file = this.getJarFile();
		if (file != null && file.getName().toLowerCase().endsWith(".jar")) {
			JarFile jarFile = new JarFile(file);
			try {
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				if (jarEntries != null) {
					while (jarEntries.hasMoreElements()) {
						JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
						if (jarEntry.isDirectory()) {
							// 文件夹不做处理
							continue;
						}
						String name = jarEntry.getName().toLowerCase();
						if (name.startsWith(resource.toLowerCase())) {
							File writeFile = new File(outFolder.getPath() + File.separator + jarEntry.getName());
							if (!writeFile.getParentFile().exists()) {
								writeFile.getParentFile().mkdirs();
							}
							if (writeFile.createNewFile()) {
								this.print("release resources [%s].", jarEntry.getName());
								InputStream inputStream = jarFile.getInputStream(jarEntry);
								FileOutputStream fos = new FileOutputStream(writeFile);
								int len = -1;
								byte[] b = new byte[1024];
								while ((len = inputStream.read(b)) != -1) {
									if (fos == null) {
										file.getParentFile().mkdirs();
										file.createNewFile();
									}
									fos.write(b, 0, len);
								}
								fos.flush();
								fos.close();
								inputStream.close();
							}
						}
					}
				}
			} finally {
				if (jarFile != null) {
					jarFile.close();
				}
			}
		}
	}

	@Override
	protected final int run(Argument[] arguments) {
		ArrayList<Argument> others = new ArrayList<>();
		for (Argument argument : arguments) {
			if (argument.getName().equalsIgnoreCase("-Release")) {
				// 释放资源命令
				try {
					if (argument.isInputed()) {
						this.writeResources(this.getResourceSign(argument), this.getReleaseFolder(argument));
					}
				} catch (Exception e) {
					this.print(e);
					return RETURN_VALUE_IO_EXCEPTION;
				}
				continue;// 已处理，则不再传入子命令
			}
			others.add(argument);
		}
		return this.go(others.toArray(new Argument[] {}));
	}

	/**
	 * 为帮助添加调用代码的示例
	 */
	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append(" ");
		stringBuilder.append("-Release"); // 释放模板到当前目录
		super.moreHelps(stringBuilder);
	}

	/**
	 * 创建自身参数
	 */
	protected Argument[] createArguments() {
		return new Argument[] { new Argument("-Release", "释放资源模板") };
	}

	/**
	 * 运行
	 * 
	 * @param arguments
	 * @return
	 */
	protected abstract int go(Argument[] arguments);

	/**
	 * 获取释放的资源标记
	 * 
	 * @param releaseArgument
	 *            释放资源的命令
	 * @return
	 */
	protected abstract String getResourceSign(Argument releaseArgument);

	/**
	 * 获取释放资源的位置
	 * 
	 * @param releaseArgument
	 *            释放资源的命令
	 * @return
	 */
	protected abstract File getReleaseFolder(Argument releaseArgument);
}
