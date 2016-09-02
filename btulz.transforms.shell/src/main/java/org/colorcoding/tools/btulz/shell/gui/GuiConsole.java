package org.colorcoding.tools.btulz.shell.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;

/**
 * 用户图形界面控制台
 * 
 * @author Niuren.Zhu
 *
 */
public class GuiConsole {

	public static void main(String[] args) {
		show();
	}

	public static void show() {
		JFrame frame = new JFrame("btulz.transforms");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(300, 100, 800, 600);
		frame.setLayout(new FlowLayout()); // 采用流式布局
		frame.setVisible(true);
	}
}
