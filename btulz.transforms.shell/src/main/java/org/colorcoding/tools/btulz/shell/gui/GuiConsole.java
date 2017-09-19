package org.colorcoding.tools.btulz.shell.gui;

import org.colorcoding.tools.btulz.shell.gui.commands.MainFrame;

/**
 * 用户图形界面控制台
 * 
 * @author Niuren.Zhu
 *
 */
public class GuiConsole {

	private volatile static GuiConsole instance;

	public static GuiConsole create() {
		if (instance == null) {
			synchronized (GuiConsole.class) {
				if (instance == null) {
					instance = new GuiConsole();
				}
			}
		}
		return instance;
	}

	public void run(String[] args) {
		this.showUI();
	}

	protected void showUI() {
		MainFrame frame = new MainFrame();
		frame.display();
	}
}
