package org.colorcoding.tools.btulz.shell.gui.commands;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.colorcoding.tools.btulz.shell.commands.CommandBuilder;
import org.colorcoding.tools.btulz.shell.commands.CommandManager;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -8387552310875230969L;

	public MainFrame() {
		super("btulz.transforms");
	}

	MainFrame that = this;

	public void initialize() {
		// 设置窗体
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, 960, 600);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		// 添加菜单
		this.initMenus();
		// 添加控件-构建区域
		JPanel builderPane = new JPanel();
		builderPane.setName("pane_builder");
		for (CommandBuilder commandBuilder : CommandManager.create().getCommands()) {
			BuilderTab builderTab = new BuilderTab(commandBuilder);
			builderTab.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					that.onBuilderTabClick((BuilderTab) e.getSource());
				}
			});
			builderPane.add(builderTab);
		}
		gridBagConstraints.gridx = 0;// 组件的横坐标
		gridBagConstraints.gridy = 0;// 组件的纵坐标
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;// 组件所占列数，也是组件的宽度
		gridBagConstraints.gridheight = 0;// 组件所占行数，也是组件的高度
		gridBagConstraints.weightx = 1;// 行的权重，通过这个属性来决定如何分配列的剩余空间
		gridBagConstraints.weighty = 0;// 列的权重，通过这个属性来决定如何分配列的剩余空间
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.ipadx = 3;// 组件间的横向间距
		gridBagConstraints.ipady = 2;// 组件间的纵向间距
		this.add(builderPane, gridBagConstraints);
		builderPane.setVisible(true);
		// 添加控件-工作区域
		this.workingPane = new JTabbedPane();
		this.workingPane.setName("pane_working");
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.gridheight = 0;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 0;
		this.workingPane.addTab("about", new AboutTab());
		this.add(this.workingPane, gridBagConstraints);
		this.workingPane.setVisible(true);
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
		this.setLocationRelativeTo(null);// 移到中间
		this.setVisible(true);
	}

	JTabbedPane workingPane;

	public void showWorkingTab(WorkingTab tab) {
		if (this.workingPane == null) {
			return;
		}
		this.workingPane.add(tab.getName(), tab);
		// this.workingPane.setSelectedIndex(this.workingPane.getTabCount() -
		// 1);
	}

	public void onBuilderTabClick(BuilderTab tab) {
		CommandBuilder commandBuilder = tab.getBuilder();
		WorkingTab workingTab = new CommandTab(commandBuilder);
		this.showWorkingTab(workingTab);
	}
}
