package org.colorcoding.tools.btulz;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 运行环境
 * 
 * @author Niuren.Zhu
 *
 */
public class Environment {

	/**
	 * 命名空间-工具
	 */
	public static final String NAMESPACE_BTULZ_UTIL = "http://colorcoding.org/btulz/util";
	/**
	 * 命名空间-模型
	 */
	public static final String NAMESPACE_BTULZ_MODELS = "http://colorcoding.org/btulz/models";

	/**
	 * 命名空间-变形金刚
	 */
	public static final String NAMESPACE_BTULZ_TRANSFORMERS = "http://colorcoding.org/btulz/transformers";

	/**
	 * 命名空间-执行计划
	 */
	public static final String NAMESPACE_BTULZ_ORCHESTRATION = "http://colorcoding.org/btulz/orchestration";

	private volatile static Logger logger;

	/**
	 * 获取日志员
	 * 
	 * @return
	 */
	public synchronized static Logger getLogger() {
		if (logger == null) {
			try {
				File file = new File(getStartupFolder() + File.separator + "log4j.properties");
				if (file.exists() && file.isFile()) {
					PropertyConfigurator.configure(file.getPath());
				} else {
					URI path = getResource("log4j.properties");
					if (path != null && path.getPath() != null) {
						PropertyConfigurator.configure(path.getPath());
					} else {
						InputStream stream = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream("log4j.properties");
						if (stream != null) {
							PropertyConfigurator.configure(stream);
						}
					}
				}
				logger = Logger.getLogger("btulz.transforms");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

	/**
	 * 程序启动的目录（主要的配置文件目录）
	 * 
	 * @return
	 */
	public static String getStartupFolder() {
		try {
			File file = null;
			URL url = Thread.currentThread().getContextClassLoader().getResource("");
			String path = null;
			if (url != null) {
				URI uri = url.toURI();
				if (uri != null) {
					path = uri.getPath();
				}
				if (path == null) {
					path = url.getPath();
					if (path != null)
						path = java.net.URLDecoder.decode(path, "UTF-8");
				}
			}
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
			file = new File(path);
			if (file.isFile()) {
				file = file.getParentFile();
			}
			if (file.getParentFile().isDirectory() && file.getParentFile().getName().equals("WEB-INF")) {
				// web路径
				file = file.getParentFile();
			}
			return file.getPath();
		} catch (URISyntaxException | UnsupportedEncodingException e) {
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
	 * 获取资源地址
	 * 
	 * @param type
	 *            资源名称
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

	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName
	 *            文件路径
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String getEncoding(String fileName) {
		String charset = "GBK";
		try {
			byte[] first3Bytes = new byte[3];
			boolean checked = false;
			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
			bin.mark(100);
			int read = bin.read(first3Bytes, 0, 3);
			if (read == -1) {
				bin.close();
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bin.reset();
			if (!checked) {
				@SuppressWarnings("unused")
				int loc = 0;
				while ((read = bin.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bin.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 -
															// 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bin.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bin.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bin.close();
		} catch (Exception e) {
			getLogger().error(String.format("parse [%s] encoding faild, %s", fileName, e));
		} finally {
		}
		return charset;
	}
}
