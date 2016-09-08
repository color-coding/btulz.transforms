package org.colorcoding.tools.btulz.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.Environment;
import org.colorcoding.tools.btulz.transformers.CodeTransformer;

/**
 * 代码创建命令
 * 
 * @author Niuren.Zhu
 *
 */
@Prompt(Command4Code.COMMAND_PROMPT)
public class Command4Code extends Command<Command4Code> {

	public final static String COMMAND_PROMPT = "code";

	public Command4Code() {
		this.setName(COMMAND_PROMPT);
		this.setDescription("根据模型创建代码。");
	}

	@Override
	protected Argument[] createArguments() {
		ArrayList<Argument> arguments = new ArrayList<>();
		arguments.add(new Argument("-TemplateFolder", "使用的模板")); // 使用的模板
		arguments.add(new Argument("-OutputFolder", "代码输出的目录")); // 输出目录
		arguments.add(new Argument("-GroupId", "组命名空间"));// 组标记
		arguments.add(new Argument("-ArtifactId", "项目命名空间"));// 项目标记
		// arguments.add(new Argument("-ProjectId", "项目标记"));// 项目标记
		arguments.add(new Argument("-ProjectVersion", "版本"));// 项目版本
		arguments.add(new Argument("-ProjectUrl", "项目的地址"));// 项目地址
		arguments.add(new Argument("-Domains", "使用的模型目录或文件")); // 模型文件
		arguments.add(new Argument("-Parameters", "其他参数数据，json格式字符串")); // 其他参数数据
		return arguments.toArray(new Argument[] {});
	}

	@Override
	protected void moreHelps(StringBuilder stringBuilder) {
		stringBuilder.append("示例：");
		stringBuilder.append(NEW_LINE);
		stringBuilder.append("  ");
		stringBuilder.append(Command4Code.COMMAND_PROMPT); // 命令
		stringBuilder.append(" ");
		stringBuilder.append("-TemplateFolder=eclipse/ibas_classic"); // 使用的模板
		stringBuilder.append(" ");
		stringBuilder.append("-OutputFolder=D:\\temp"); // 输出目录
		stringBuilder.append(" ");
		stringBuilder.append("-GroupId=org.colorcoding");// 组标记
		stringBuilder.append(" ");
		stringBuilder.append("-ArtifactId=ibas");// 项目标记
		// stringBuilder.append(" ");
		// stringBuilder.append("-ProjectId=d42441ec-841e-4585-83f6-eff2a6b0ef45");//
		// 项目ID
		stringBuilder.append(" ");
		stringBuilder.append("-ProjectVersion=0.0.1");// 项目版本
		stringBuilder.append(" ");
		stringBuilder.append("-ProjectUrl=http://colorcoding.org");// 项目地址
		stringBuilder.append(" ");
		stringBuilder.append("-Domains=D:\\initialization"); // 模型文件
		stringBuilder.append(" ");
		stringBuilder.append(
				"-Parameters=[{\"name\":\"ibasVersion\",\"value\":\"0.1.1\"},{\"name\":\"jerseyVersion\",\"value\":\"2.22.1\"}]"); // 其他参数
		super.moreHelps(stringBuilder);
	}

	@Override
	protected int run(Argument[] arguments) {
		try {
			CodeTransformer codeTransformer = new CodeTransformer();
			for (Argument argument : arguments) {
				if (argument.getName().equalsIgnoreCase("-TemplateFolder")) {
					boolean done = codeTransformer.setTemplateFolder(argument.getValue());
					if (!done) {
						// 模板文件夹设置不成功，可能是文件不存在
						// 尝试保存jar包资源到启动文件夹
						this.writeCodeResources();
						codeTransformer.setTemplateFolder(argument.getValue());
					}
				} else if (argument.getName().equalsIgnoreCase("-OutputFolder")) {
					codeTransformer.setOutputFolder(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-GroupId")) {
					codeTransformer.setGroupId(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ArtifactId")) {
					codeTransformer.setArtifactId(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ProjectId")) {
					codeTransformer.setProjectVersion(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ProjectVersion")) {
					codeTransformer.setProjectVersion(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-ProjectUrl")) {
					codeTransformer.setProjectUrl(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Domains")) {
					codeTransformer.addDomains(argument.getValue());
				} else if (argument.getName().equalsIgnoreCase("-Parameters")) {
					codeTransformer.addParameters(this.createParameters(argument.getValue()));
				} else {
					this.print("invalid argument [%s].", argument.getName());
					return -1;
				}
			}
			codeTransformer.transform();
			return 1;
		} catch (Exception e) {
			this.print(e.toString());
			return -1;
		}
	}

	private void writeCodeResources() throws IOException {
		String jarFilePath = Command4Code.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		File file = new File(java.net.URLDecoder.decode(jarFilePath, "UTF-8"));
		if (file.getName().toLowerCase().endsWith(".jar")) {
			JarFile jarFile = new JarFile(file);
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			if (jarEntries != null) {
				while (jarEntries.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
					String name = jarEntry.getName().toLowerCase();
					if (name.startsWith("code/")) {
						File writeFile = new File(Environment.getWorkingFolder() + File.separator + jarEntry.getName());
						Environment.getLogger().debug((String.format("release resources [%s]. ", jarEntry.getName())));
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
			jarFile.close();
		}
	}
}
