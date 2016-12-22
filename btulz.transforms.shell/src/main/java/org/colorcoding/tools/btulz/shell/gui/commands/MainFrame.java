package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -8387552310875230969L;

	public MainFrame() {
		super("btulz.transforms.shell");
	}

	MainFrame that = this;

	public void initialize() {
		// 设置窗体
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		// 添加菜单
		this.initMenus();
		// 添加控件-构建区域
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		// gridBagConstraints.ipady = 15;
		// gridBagConstraints.ipadx = 30;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		JLabel label = new JLabel("Commands");
		label.setFont(new java.awt.Font("Dialog", 3, 18));
		label.setForeground(Color.BLUE);
		label.setToolTipText("double click to reload.");
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
				if (e.getClickCount() == 2) {
					// 双击，重载ui
					that.dispose();
					MainFrame frame = new MainFrame();
					frame.display();
				}
			}
		});
		this.add(label, gridBagConstraints);
		BuilderTab builderPane = new BuilderTab();
		builderPane.setName("pane_builder");
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		// gridBagConstraints.ipady = -1;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		this.add(builderPane, gridBagConstraints);
		builderPane.addBuilderTabListener(new BuilderTabListener() {
			@Override
			public void builderSelected(CommandBuilder builder) {
				that.showWorkingTab(new CommandTab(builder));
			}
		});
		// 添加控件-工作区域
		this.workingPane = new JTabbedPane();
		this.workingPane.setName("pane_working");
		this.workingPane.setAutoscrolls(true);
		// WorkingTab tab = new AboutTab();
		// this.workingPane.addTab(tab.getTitle(), tab);
		gridBagConstraints.ipadx = 500;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		this.add(this.workingPane, gridBagConstraints);
	}

	protected void initMenus() {
		MainFrame that = this;
		JMenuBar menuBar = new JMenuBar();
		// 帮助菜单项
		JMenu menu = new JMenu("Help");
		// 帮助-关于
		JMenuItem itemAbout = new JMenuItem("About");
		itemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.showWorkingTab(new AboutTab());
			}
		});
		menu.add(itemAbout);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

	public void display() {
		this.initialize();
		this.pack();
		this.setLocationRelativeTo(null);// 移到中间
		this.setVisible(true);
	}

	JTabbedPane workingPane;

	public void showWorkingTab(WorkingTab tab) {
		if (this.workingPane == null) {
			return;
		}
		this.workingPane.addTab(tab.getTitle(), tab);
		this.workingPane.setSelectedComponent(tab);
		this.pack();
		this.setLocationRelativeTo(null);// 移到中间
	}

}
