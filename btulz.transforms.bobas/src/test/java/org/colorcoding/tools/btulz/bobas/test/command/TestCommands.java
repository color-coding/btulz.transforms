package org.colorcoding.tools.btulz.bobas.test.command;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.colorcoding.ibas.bobas.MyConfiguration;
import org.colorcoding.tools.btulz.bobas.Console;
import org.colorcoding.tools.btulz.bobas.command.Command4Ds;
import org.colorcoding.tools.btulz.bobas.command.Command4Init;
import org.colorcoding.tools.btulz.bobas.command.Command4Routing;

import junit.framework.TestCase;

/**
 * bobas命令行测试
 *
 * 覆盖：
 * - Command4Init：初始化命令（加载配置、数据、类路径）
 * - Command4Ds：数据结构命令（从JAR包或XML加载）
 * - Command4Routing：路由命令（查询模块并配置路由）
 *
 * 注意：依赖ibas.initialfantasy项目和ibas-framework
 */
public class TestCommands extends TestCase {

	/** 测试用控制台，继承Console以访问protected方法，避免触发System.exit */
	static class TestConsole extends Console {
		/** 运行命令，返回退出码（不触发System.exit） */
		public static int execute(String[] args) {
			return getCommandsManager().run(args);
		}
	}

	public void testInit() throws MalformedURLException {
		ArrayList<String> args = new ArrayList<>();
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String ibas = folder.getPath() + File.separator + "ibas-framework" + File.separator + "release" + File.separator;
		String config = ifFolder + File.separator + "ibas.initialfantasy" + File.separator + "app.xml";
		String data = ifFolder + File.separator + "release" + File.separator + "ibas.initialfantasy-0.2.0.jar";
		String classes = ifFolder + File.separator + "release" + File.separator + "ibas.initialfantasy-0.2.0.jar";
		File[] files = new File(ibas).listFiles();
		if (files != null) {
			for (File item : files) {
				if (item.getName().endsWith(".jar")) {
					classes = classes + ";" + item.toURI().toURL().getPath();
				}
			}
		}
		args.add(Command4Init.COMMAND_PROMPT);
		args.add("-data=" + data);
		args.add("-config=" + config);
		args.add("-classes=" + classes);
		args.add("-ignore");
		args.add("-force");
		TestConsole.execute(args.toArray(new String[] {}));
	}

	public void testDs() throws MalformedURLException {
		ArrayList<String> args = new ArrayList<>();
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String config = ifFolder + File.separator + "ibas.initialfantasy" + File.separator + "app.xml";
		String data = ifFolder + File.separator + "release" + File.separator + "ibas.initialfantasy-0.2.0.jar";
		args.add(Command4Ds.COMMAND_PROMPT);
		args.add("-data=" + data);
		args.add("-config=" + config);
		args.add("-ignore");
		TestConsole.execute(args.toArray(new String[] {}));
	}

	public void testRouting() throws MalformedURLException {
		ArrayList<String> args = new ArrayList<>();
		File folder = new File(MyConfiguration.getStartupFolder());
		folder = folder.getParentFile().getParentFile().getParentFile().getParentFile();
		String ifFolder = folder.getPath() + File.separator + "ibas.initialfantasy";
		String config = ifFolder + File.separator + "ibas.initialfantasy" + File.separator + "app.xml";
		args.add(Command4Routing.COMMAND_PROMPT);
		args.add("-config=" + config);
		args.add("-query=SELECT * FROM ${Company}_SYS_MODULE");
		args.add("-dataUrl=.../${ModuleName}/services/rest/data");
		args.add("-viewUrl=.../${ModuleName}/");
		args.add("-ignore");
		TestConsole.execute(args.toArray(new String[] {}));
	}
}
