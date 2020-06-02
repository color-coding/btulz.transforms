package org.colorcoding.tools.btulz.shell.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.colorcoding.tools.btulz.shell.Environment;
import org.colorcoding.tools.btulz.shell.Serializer;

/**
 * 命令管理员
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandManager {

	private volatile static CommandManager instance;

	public static CommandManager create() {
		if (instance == null) {
			synchronized (CommandManager.class) {
				if (instance == null) {
					instance = new CommandManager();
					instance.initialize();
				}
			}
		}
		return instance;
	}

	private Map<String, CommandBuilder> commandMaps;

	private Map<String, CommandBuilder> getCommandMaps() {
		if (this.commandMaps == null) {
			this.commandMaps = new HashMap<>();
		}
		return commandMaps;
	}

	public List<CommandBuilder> getCommands() {
		List<CommandBuilder> commandBuilders = new ArrayList<>();
		commandBuilders.addAll(this.getCommandMaps().values());
		java.util.Collections.sort(commandBuilders);
		return commandBuilders;
	}

	public void addCommands(CommandBuilder command) {
		this.getCommandMaps().put(command.getName(), command);
	}

	public CommandBuilder addCommands(InputStream inputStream, String name) {
		if (inputStream == null) {
			return null;
		}
		try {
			Object object = Serializer.fromXmlString(inputStream, CommandBuilder.class);
			if (object instanceof CommandBuilder) {
				CommandBuilder commandBuilder = (CommandBuilder) object;
				if (name != null && !name.isEmpty()) {
					commandBuilder.setName(name);
				}
				this.addCommands(commandBuilder);
				return commandBuilder;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void initialize() {
		this.getCommandMaps().clear();
		this.loadResources();
	}

	/**
	 * 加载资源命名构造器
	 * 
	 * @return
	 */
	protected void loadResources() {
		// 加载jar包命令
		try {
			// 加载自身资源
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("commands");
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url.getProtocol().equals("file")) {
					// 加载工作目录命令
					this.loadResources(url.getFile());
				}
			}
			// 加载工作目录资源
			File workFolder = new File(Environment.getWorkingFolder());
			if (workFolder != null) {
				for (File file : workFolder.listFiles()) {
					if (file.getName().endsWith(".jar")) {
						JarFile jarFile = new JarFile(file);
						this.loadResources(jarFile);
						jarFile.close();
					}
				}
			}
			// 加载工作目录命令
			this.loadResources(Environment.getCommandsFolder());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void loadResources(JarFile jarFile) throws IOException {
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		if (jarEntries != null) {
			while (jarEntries.hasMoreElements()) {
				JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
				if (jarEntry.isDirectory()) {
					continue;
				}
				if (!jarEntry.getName().startsWith("commands")) {
					continue;
				}
				if (!jarEntry.getName().endsWith(".xml")) {
					continue;
				}
				InputStream inputStream = jarFile.getInputStream(jarEntry);
				if (inputStream != null) {
					this.addCommands(inputStream, this.getCommandName(jarEntry.getName()));
					inputStream.close();
				}
			}
		}
	}

	public void loadResources(String fileFolder) {
		if (fileFolder == null || fileFolder.isEmpty())
			return;
		File file = new File(fileFolder);
		if (file.exists()) {
			if (file.isFile()) {
				try {
					this.addCommands(new FileInputStream(file), this.getCommandName(file.getName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				File[] files = file.listFiles();
				if (files != null) {
					for (File fileItem : files) {
						if (!fileItem.getName().endsWith(".xml")) {
							continue;
						}
						this.loadResources(fileItem.getPath());
					}
				}
			}
		}
	}

	private String getCommandName(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		if (name.indexOf(File.separator) > 0) {
			name = name.substring(name.lastIndexOf(File.separator) + 1);
		}
		if (name.indexOf("/") > 0) {
			name = name.substring(name.lastIndexOf("/") + 1);
		}
		if (name.indexOf(".") > 0) {
			name = name.substring(0, name.lastIndexOf("."));
		}
		return name;
	}
}
