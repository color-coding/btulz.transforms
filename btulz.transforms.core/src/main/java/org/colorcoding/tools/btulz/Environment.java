package org.colorcoding.tools.btulz;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	 * 新行标记
	 */
	public static final String NEW_LINE = System.getProperty("line.separator", "\r\n");
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
				// jar:file:/... 的 URI 在 getPath() 后仍可能保留 file: 前缀。
				// 这里必须在进入 File 之前转换，否则 file: 会被当作目录名。
				if (path.regionMatches(true, 0, "file:", 0, 5)) {
					path = new File(new URI(path)).getPath();
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

	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName 文件路径
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String getEncoding(String fileName) {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(fileName));
			if (bytes.length >= 2 && bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xFE) {
				return "UTF-16LE";
			}
			if (bytes.length >= 2 && bytes[0] == (byte) 0xFE && bytes[1] == (byte) 0xFF) {
				return "UTF-16BE";
			}
			if (bytes.length >= 3 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB
					&& bytes[2] == (byte) 0xBF) {
				return "UTF-8";
			}
			try {
				StandardCharsets.UTF_8.newDecoder()
						.onMalformedInput(CodingErrorAction.REPORT)
						.onUnmappableCharacter(CodingErrorAction.REPORT)
						.decode(ByteBuffer.wrap(bytes));
				return "UTF-8";
			} catch (CharacterCodingException e) {
				// 兼容现有的 GBK 模板，例如 Windows .bat 模板。
				return "GBK";
			}
		} catch (Exception e) {
			getLogger().error(String.format("parse [%s] encoding failed, %s", fileName, e));
		}
		return "UTF-8";
	}

	/** 判断模板/输出文件是否为 Windows 批处理文件。 */
	public static boolean isWindowsBatchFile(String fileName) {
		if (fileName == null) {
			return false;
		}
		String name = fileName.toLowerCase();
		return name.endsWith(".bat") || name.endsWith(".bat.txt");
	}
}
