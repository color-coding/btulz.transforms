package org.colorcoding.tools.btulz.shell.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.colorcoding.tools.btulz.shell.Environment;

import junit.framework.TestCase;

public class testCommonds extends TestCase {

	public void testCallCommond() throws IOException, InterruptedException {
		String workFolder = Environment.getWorkingFolder();
		String[] commands = new String[] { // 命令组
				// "cmd.exe /c", // 调用提示符
				// "java", // 调用java
				// "-jar", // 调用jar包
				"btulz.transforms.core-0.1.0.jar", // 调用jar包
				"code", // 命令
				String.format("-TemplateFolder=%s", "eclipse/ibas_classic"), // 使用的模板
				String.format("-OutputFolder=%s", workFolder + File.separator + "temp"), // 输出目录
				String.format("-GroupId=%s", "org.colorcoding"), // 组标记
				String.format("-ArtifactId=%s", "ibas"), // 项目标记
				String.format("-ProjectVersion=%s", "0.0.1"), // 项目版本
				String.format("-ProjectUrl=%s", "http://colorcoding.org"), // 项目地址
				String.format("-Domains=%s", workFolder), // 模型文件
				String.format("-Parameters=%s",
						"[{\"name\":\"Company\",\"value\":\"CC\"},{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]") // 其他参数
		};// 命令数组
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : commands) {
			stringBuilder.append(string);
			stringBuilder.append(" ");
		}
		String commond = stringBuilder.toString();// 命令
		Process process = null;
		// process = Runtime.getRuntime().exec("cmd /c ping -t 192.168.3.1");
		process = Runtime.getRuntime()
				.exec("cmd.exe java " + Environment.getWorkingFolder() + File.separator + commond);
		// process = Runtime.getRuntime().exec(commond, null, null);
		InputStream inputStream = process.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
		String line = null;
		while ((line = read.readLine()) != null) {
			System.out.println(line);
		}
		inputStream.close();
		// process.waitFor();
		// Thread.sleep(100000);
	}
}
