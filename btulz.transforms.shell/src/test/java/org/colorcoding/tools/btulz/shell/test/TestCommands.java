package org.colorcoding.tools.btulz.shell.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.colorcoding.tools.btulz.shell.Environment;

import junit.framework.TestCase;

public class TestCommands extends TestCase {

	String charsetName = "utf-8";
	Process process = null;

	public void testCallCommand() throws Exception {
		String userFolder = System.getProperty("user.dir");
		String workFolder = Environment.getWorkingFolder();
		String[] commands = new String[] { // 命令组
				// "cmd.exe /c", // 调用提示符
				"java", // 调用java
				"-jar", // 调用jar包
				new File(userFolder).getParent() + File.separator + "release" + File.separator
						+ "btulz.transforms.core-0.1.0.jar", // jar位置，需要提前执行compile_and_package.sh/bat
				"code", // 命令
				String.format("-TemplateFolder=%s", "eclipse/ibas_classic"), // 使用的模板
				String.format("-OutputFolder=%s", workFolder + File.separator + "temp"), // 输出目录
				String.format("-GroupId=%s", "org.colorcoding"), // 组标记
				String.format("-ArtifactId=%s", "ibas"), // 项目标记
				String.format("-ProjectVersion=%s", "0.0.1"), // 项目版本
				String.format("-ProjectUrl=%s", "http://colorcoding.org"), // 项目地址
				String.format("-Domains=%s",
						String.format(
								"%1$s%2$sbtulz.transforms.core%2$ssrc%2$stest%2$sjava%2$sorg%2$scolorcoding%2$stools%2$sbtulz%2$stest%2$stransformers%2$s",
								new File(userFolder).getParent(), File.separator)), // 模型文件
				String.format("-Parameters=%s",
						"[{\"name\":\"Company\",\"value\":\"CC\"},{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]"), // 其他参数
				"-Release" };// 命令数组
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : commands) {
			stringBuilder.append(string);
			stringBuilder.append(" ");
		}
		String command = stringBuilder.toString();// 命令

		if (System.getProperty("os.name").startsWith("Windows")) {
			// windows
			charsetName = "GBK";
			// command = "ping -t 192.168.3.1";
			// command = "java -version";
			// command = String.format("java -jar
			// %sbtulz.transforms.core-0.1.0.jar code",
			// new File(userFolder).getParent() + File.separator + "release" +
			// File.separator);
		} else if (System.getProperty("os.name").startsWith("Linux")) {
			// linux
			// command = "ping 192.168.3.1";
			// // command = "java -version";
			// command = String.format("java -jar
			// %sbtulz.transforms.core-0.1.0.jar code",
			// new File(userFolder).getParent() + File.separator + "release" +
			// File.separator);
		} else
			throw new Exception("unkwon os " + System.getProperty("os.name"));

		try {
			File workFile = new File(workFolder + File.separator + "temp");
			if (!workFile.exists()) {
				workFile.mkdirs();
			}
			System.out.println("run: " + command);
			process = Runtime.getRuntime().exec(command, null, workFile);
			// 开启线程1，正常输出
			new Thread(new Runnable() {
				public void run() {
					try {
						InputStream inputStream = process.getInputStream();
						BufferedReader read = new BufferedReader(new InputStreamReader(inputStream, charsetName));
						String line = null;
						while ((line = read.readLine()) != null) {
							System.out.println(line);
						}
						inputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			// 开启线程2，错误输出
			new Thread(new Runnable() {
				public void run() {
					try {
						InputStream inputStream = process.getErrorStream();
						BufferedReader read = new BufferedReader(new InputStreamReader(inputStream, charsetName));
						String line = null;
						while ((line = read.readLine()) != null) {
							System.err.println(line);
						}
						inputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			process.waitFor();
			// Thread.sleep(100000);
		} finally {
			if (process != null) {
				// 最终销毁
				process.destroy();
			}
		}
	}
}
