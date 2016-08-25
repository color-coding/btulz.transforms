package org.colorcoding.tools.btulz;

import java.io.File;
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
	/**
	 * 命名空间-参数
	 */
	public static final String NAMESPACE_BTULZ_PARAMETERS = "http://colorcoding.org/btulz/parameters";

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
					// 此处存在中文路径识别异常风险
					path = url.getPath();
					if (path != null)
						path = path.replace("%20", " ");// 处理空格
				}
			}
			// file:/E:/WorkTemp/ibas/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ibcp.systemcenter.service/WEB-INF/classes/
			// 取到的值如上
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
		} catch (URISyntaxException e) {
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
}
