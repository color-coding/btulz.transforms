package org.colorcoding.tools.btulz.shell.gui;

import javax.swing.SwingUtilities;

import org.colorcoding.tools.btulz.shell.gui.command.MainFrame;

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
		SwingUtilities.invokeLater(() -> showUI());
	}

	protected void showUI() {
		MainFrame frame = new MainFrame();
		frame.display();
	}
}
