package org.colorcoding.tools.btulz.shell;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 运行环境
 * 
 * @author Niuren.Zhu
 *
 */
public class Environment {

	/**
	 * 命名空间-壳
	 */
	public static final String NAMESPACE_BTULZ_SHELL = "http://colorcoding.org/btulz/shell";

	/**
	 * 命名空间-壳-命令
	 */
	public static final String NAMESPACE_BTULZ_SHELL_COMMANDS = NAMESPACE_BTULZ_SHELL + "/commands";

	/**
	 * 程序启动的目录（主要的配置文件目录）
	 * 
	 * @return
	 */
	public static String getStartupFolder() {
		try {
			String path = java.net.URLDecoder
					.decode(Environment.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			if (path != null) {
				if (path.split(":").length > 2) {
					path = path.substring(path.indexOf(":") + 1, path.length());
				}
				if (path.indexOf("!") > 0) {
					path = path.substring(0, path.indexOf("!"));
				}
			}
			if (path == null) {
				path = System.getProperty("user.dir");
			}
			File file = new File(path);
			if (file.isFile()) {
				file = file.getParentFile();
			}
			if (file.getParentFile().isDirectory() && file.getParentFile().getName().equals("WEB-INF")) {
				// web路径
				file = file.getParentFile();
			}
			return file.getPath();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取工作目录
	 * 
	 * @return
	 */
	public static String getWorkingFolder() {
		return getStartupFolder();
	}

	/**
	 * 获取历史目录
	 * 
	 * @return
	 */
	public static String getHistoryFolder() {
		return getWorkingFolder() + File.separator + "history";
	}

	/**
	 * 获取命令目录
	 * 
	 * @return
	 */
	public static String getCommandsFolder() {
		return getWorkingFolder() + File.separator + "commands";
	}

	/**
	 * 获取资源地址
	 * 
	 * @param type 资源名称
	 * @return 统一格式（此对象避免路径的中文问题）
	 * @throws URISyntaxException
	 */
	public static URI getResource(String name) throws URISyntaxException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(name);
		if (url == null) {
			return null;
		}
		return url.toURI();
	}

}
