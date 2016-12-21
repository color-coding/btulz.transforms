package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.BorderLayout;

import javax.swing.JLabel;

public class AboutTab extends WorkingTab {

	private static final long serialVersionUID = 7201855758302708134L;

	public AboutTab() {
		this.init();
	}

	protected void init() {
		this.add(new JLabel("copyright 2016 color-coding studio"), BorderLayout.CENTER);
	}
}
