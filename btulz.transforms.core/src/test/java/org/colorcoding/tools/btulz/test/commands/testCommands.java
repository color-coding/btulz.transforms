package org.colorcoding.tools.btulz.test.commands;

import java.io.File;
import java.util.ArrayList;

import org.colorcoding.tools.btulz.Console;
import org.colorcoding.tools.btulz.commands.Command;
import org.colorcoding.tools.btulz.commands.Command4Code;

import junit.framework.TestCase;

public class testCommands extends TestCase {

	public void testConsole() {
		Console.main(new String[] {});
	}

	public void testCommandCode() {
		ArrayList<String> args = new ArrayList<>();
		args.add(String.format(Command4Code.COMMAND_PROMPT)); // 命令
		args.add(String.format("-TemplateFolder=%s", "eclipse/ibas_classic")); // 使用的模板
		args.add(String.format("-OutputFolder=%s", System.getenv("ibasWorkspace") + File.separator + "temp")); // 输出目录
		args.add(String.format("-GroupId=%s", "org.colorcoding"));// 组标记
		args.add(String.format("-ArtifactId=%s", "ibas"));// 项目标记
		// args.add(String.format("-ProjectId=%s",
		// UUID.randomUUID().toString()));// 项目版本
		args.add(String.format("-ProjectVersion=%s", "0.0.1"));// 项目版本
		args.add(String.format("-ProjectUrl=%s", "http://colorcoding.org"));// 项目地址
		args.add(String.format("-Domains=%s", System.getenv("ibasWorkspace") + File.separator + "initialization")); // 模型文件
		args.add(String.format("-Parameters=%s",
				"[{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]")); // 其他参数
		System.out.println("显示帮助信息：");
		Console.main(new String[] { Command4Code.COMMAND_PROMPT, Command.ARGUMENT_NAME_HELP });
		System.out.println("开始运行：");
		Console.main(args.toArray(new String[] {}));
	}
}
