package org.colorcoding.tools.btulz.shell.gui.commands;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class WorkingTab extends JPanel {

	private static final long serialVersionUID = 7047384462753675622L;

	public WorkingTab() {
		this.init();
	}

	public WorkingTab(String title) {
		this();
		this.setTitle(title);
	}

	private String title;

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	private JTextArea textArea;

	protected void init() {

	}
}
