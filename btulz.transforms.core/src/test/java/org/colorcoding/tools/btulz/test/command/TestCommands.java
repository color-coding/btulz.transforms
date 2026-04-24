package org.colorcoding.tools.btulz.test.command;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Console;
import org.colorcoding.tools.btulz.command.Command4Code;
import org.colorcoding.tools.btulz.test.Environment;

import junit.framework.TestCase;

/**
 * 命令行测试
 *
 * 覆盖： - Console空参数调用 - Command4Code：代码生成命令（代表所有命令行入口）
 *
 * 注意：Command4Ds/Command4Sql/Command4DsJar/Command4Excel等
 * 均为对应Transformer的命令行包装，与Transformer测试重复
 */
public class TestCommands extends TestCase {

	/** 测试用控制台，继承Console以访问protected方法，避免触发System.exit */
	static class TestConsole extends Console {
		/** 运行命令，返回退出码（不触发System.exit） */
		public static int execute(String[] args) {
			return getCommandsManager().run(args);
		}
	}

	/** 控制台空参数调用 */
	public void testConsole() {
		TestConsole.execute(new String[] {});
	}

	/** 代码生成命令（代表所有命令行入口） */
	public void testCommandCode() {
		ArrayList<String> args = new ArrayList<>();
		args.add(String.format(Command4Code.COMMAND_PROMPT)); // 命令
		args.add(String.format("-TemplateFolder=%s", "eclipse/ibas_classic")); // 使用的模板
		args.add(String.format("-OutputFolder=%s", Environment.getOutputFolder())); // 输出目录
		args.add(String.format("-GroupId=%s", "org.colorcoding"));// 组标记
		args.add(String.format("-ArtifactId=%s", "ibas"));// 项目标记
		args.add(String.format("-ProjectVersion=%s", "0.0.1"));// 项目版本
		args.add(String.format("-ProjectUrl=%s", "http://colorcoding.org"));// 项目地址
		args.add(String.format("-Domains=%s",
				Environment.getCodeFolder() + "/btulz.transforms/btulz.transforms.shell/target/test-classes"
						.replace("/", File.separator))); // 模型文件
		args.add(String.format("-Parameters=%s",
				"[{\"name\":\"Company\",\"value\":\"CC\"},{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]")); // 其他参数
		System.out.println("显示帮助信息：");
		TestConsole.execute(new String[] { Command4Code.COMMAND_PROMPT, Command4Code.ARGUMENT_NAME_HELP });
		System.out.println("开始运行：");
		TestConsole.execute(args.toArray(new String[] {}));
	}
}
