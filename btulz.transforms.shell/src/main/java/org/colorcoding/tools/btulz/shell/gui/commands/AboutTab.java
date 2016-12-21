package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

public class AboutTab extends WorkingTab {

	private static final long serialVersionUID = 7201855758302708134L;

	public AboutTab() {
		super("about");
	}

	@Override
	protected void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		int count = 0;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		this.add(new JLabel("btulz.transforms.shell"), gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		this.add(new JLabel("author: niuren.zhu"), gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		this.add(new JLabel("mail: niuren.zhu@icloud.com"), gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		this.add(new JLabel("copyright 2016 color-coding studio"), gridBagConstraints);
	}
}
