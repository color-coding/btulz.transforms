package org.colorcoding.tools.btulz.shell.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.colorcoding.tools.btulz.shell.Environment;

/**
 * 命令
 * 
 * @author Niuren.Zhu
 *
 */
public class Command {

	public static String toCommand(String[] commands) {
		StringBuilder builder = new StringBuilder();
		for (String command : commands) {
			if (builder.length() > 0) {
				builder.append(" ");
			}
			builder.append(command);
		}
		return builder.toString();
	}

	public Command() {
	}

	public Command(CommandBuilder builder) {
		this.setCommandBuilder(builder);
	}

	private CommandEventSupport support;

	/**
	 * 添加事务监听
	 * 
	 * @param listener
	 */
	public void addListener(CommandListener listener) {
		if (listener == null) {
			return;
		}
		if (this.support == null) {
			this.support = new CommandEventSupport(this);
		}
		this.support.addListener(listener);
	}

	/**
	 * 移出事务监听
	 * 
	 * @param listener
	 */
	public void removeListener(CommandListener listener) {
		if (listener == null) {
			return;
		}
		if (this.support == null) {
			return;
		}
		this.support.removeListener(listener);
	}

	private CommandBuilder commandBuilder;

	public final CommandBuilder getCommandBuilder() {
		return commandBuilder;
	}

	private final void setCommandBuilder(CommandBuilder commandBuilder) {
		this.commandBuilder = commandBuilder;
	}

	private String workFolder;

	public final String getWorkFolder() {
		if (this.workFolder == null || this.workFolder.isEmpty()) {
			this.workFolder = Environment.getWorkingFolder();
		}
		return workFolder;
	}

	public final void setWorkFolder(String workFolder) {
		this.workFolder = workFolder;
	}

	/**
	 * 运行命令
	 * 
	 * @return 命令运行返回值
	 */
	public int run() {
		if (this.getCommandBuilder() == null) {
			throw new NullPointerException("not set command builder.");
		}
		return this.run(this.getCommandBuilder().toCommands());
	}

	private Thread commonThread = null;
	private Thread errorThread = null;
	private Process process = null;

	private String getCharsetName() {
		if (System.getProperty("os.name").startsWith("Windows"))
			return "GBK";
		return "utf-8";
	}

	/**
	 * 运行命令
	 * 
	 * @param command 命令字符
	 * @return 命令运行返回值
	 */
	public int run(String[] commands) {
		try {
			if (this.process != null) {
				throw new RuntimeException("Has been in running command.");
			}
			this.fireMessages(MessageType.common, String.format("[%s] is ready to run.",
					this.getCommandBuilder() != null ? this.getCommandBuilder().getName() : "unknown"));
			this.fireMessages(MessageType.common, String.format("command: %s", toCommand(commands)));
			File workFolder = new File(this.getWorkFolder());
			this.fireMessages(MessageType.common, String.format("workfolder: %s", workFolder.getPath()));
			this.process = Runtime.getRuntime().exec(commands, null, workFolder);
			this.commonThread = new Thread(new Runnable() {
				public synchronized void run() {
					try (BufferedReader read = new BufferedReader(
							new InputStreamReader(process.getInputStream(), Command.this.getCharsetName()))) {
						String line = null;
						while ((line = read.readLine()) != null) {
							Command.this.fireMessages(MessageType.common, line);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			// 开启线程2，错误输出
			this.errorThread = new Thread(new Runnable() {
				public synchronized void run() {
					try (BufferedReader read = new BufferedReader(
							new InputStreamReader(process.getErrorStream(), Command.this.getCharsetName()))) {
						String line = null;
						while ((line = read.readLine()) != null) {
							Command.this.fireMessages(MessageType.error, line);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			this.commonThread.start();
			this.errorThread.start();
			this.process.waitFor();
			return this.process.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
			return -9999;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -9999;
		} finally {
			this.commonThread = null;
			this.errorThread = null;
			if (this.process != null) {
				this.process.destroy();
			}
			this.process = null;
			this.fireMessages(MessageType.common, String.format("[%s] is done.",
					this.getCommandBuilder() != null ? this.getCommandBuilder().getName() : "unknown"));
		}
	}

	/**
	 * 终止命令
	 */
	public void destroy() {
		if (this.process != null) {
			this.process.destroy();
		}
		this.fireMessages(MessageType.common, String.format("[%s] was stop.",
				this.getCommandBuilder() != null ? this.getCommandBuilder().getName() : "unknown"));
	}

	/**
	 * 触发消息
	 * 
	 * @param type    消息类型
	 * @param message 消息
	 */
	protected void fireMessages(MessageType type, String message) {
		if (this.support == null) {
			return;
		}
		this.support.fireMessages(type, message);
	}
}
