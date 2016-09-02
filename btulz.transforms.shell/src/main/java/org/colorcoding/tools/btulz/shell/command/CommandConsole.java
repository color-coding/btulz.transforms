package org.colorcoding.tools.btulz.shell.command;

/**
 * 命令行控制台
 * 
 * @author Niuren.Zhu
 *
 */
public class CommandConsole {

	public static final String COMMOND_HELP = "--help";

	public static void main(String[] args) {

		for (String arg : args) {
			if (arg.equals(COMMOND_HELP)) {
				System.out.println("please,exploe http://colorcoding.org/btulz/transforms");
			}
		}
	}
}
