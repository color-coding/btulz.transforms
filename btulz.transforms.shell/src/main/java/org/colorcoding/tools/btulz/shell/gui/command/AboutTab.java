package org.colorcoding.tools.btulz.shell.gui.command;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 关于和帮助
 * 
 * @author Niuren.Zhu
 *
 */
public class AboutTab extends WorkingTab {

	private static final long serialVersionUID = 7201855758302708134L;

	/**
	 * 工作层
	 */
	public static final String PANEL_ABOUT = "about_panel";

	public AboutTab() {
		super("about");
		this.initPanel(PANEL_ABOUT);
	}

	private AboutTab that = this;

	@Override
	protected void init(JPanel panel) {
		// 执行其他层
		super.init(panel);
		// 仅执行自己相关
		if (!panel.getName().equals(PANEL_ABOUT)) {
			return;
		}
		panel.setLayout(new GridBagLayout());
		JLabel label = null;
		Font font = new java.awt.Font("Dialog", 3, 15);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		int count = 0;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		label = new JLabel("btulz.transforms.shell");
		label.setFont(font);
		panel.add(label, gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		label = new JLabel("author: niuren.zhu");
		label.setFont(font);
		panel.add(label, gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		label = new JLabel("mail: niuren.zhu@icloud.com");
		label.setFont(font);
		panel.add(label, gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		label = new JLabel("copyright 2016 color-coding studio");
		label.setFont(font);
		panel.add(label, gridBagConstraints);
		count++;
		gridBagConstraints.gridy = count;// 组件的纵坐标
		label = new JLabel("help");
		label.setFont(font);
		label.setForeground(Color.BLUE);
		label.setToolTipText("click to get help.");
		label.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				that.onButtonRunClick(null);
			}
		});
		panel.add(label, gridBagConstraints);
	}

	protected void onButtonRunClick(JButton button) {
		super.onButtonRunClick(button);
		this.setRunningCommand("-help");
	}
}
