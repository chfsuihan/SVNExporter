package com.svn;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;

import com.svn.export.FileExporterUtils;
import com.svn.export.GBC;

public class SVNExportor {
	private static Frame f= new Frame("根据SVN日志导出发布包");
	
	public static void main(String[] args) {
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		f.setLayout(new GridBagLayout());
		f.setBounds(100, 100, 700, 400);
		
		final TextField logPath= new TextField(40);
		f.add(logPath, new GBC(0,0).setFill(GridBagConstraints.BOTH));
		Button addLogPathBtn = new Button("选择日志路径");
		f.add(addLogPathBtn, new GBC(1,0).setFill(GridBagConstraints.BOTH));
		
		final TextField rootPath= new TextField(40);
		f.add(rootPath, new GBC(0,1).setFill(GridBagConstraints.BOTH));
		Button addRootPathBtn = new Button("选择WebRoot路径");
		f.add(addRootPathBtn, new GBC(1,1).setFill(GridBagConstraints.BOTH));
		
		final TextField exportPath= new TextField(40);
		f.add(exportPath, new GBC(0,2).setFill(GridBagConstraints.BOTH));
		Button addExportPathBtn = new Button("选择导出路径");
		f.add(addExportPathBtn, new GBC(1,2).setFill(GridBagConstraints.BOTH));
		
		Button exportBtn = new Button("导出");
		f.add(exportBtn, new GBC(1,3).setFill(GridBagConstraints.BOTH));
		
		Label l = new Label("导出日志：");
		f.add(l, new GBC(0,4,1,1).setFill(GridBagConstraints.BOTH));
		Button cleanLogBtn = new Button("清空日志");
		f.add(cleanLogBtn, new GBC(1,4,1,1).setFill(GridBagConstraints.BOTH));
		
		final TextArea logArea = new TextArea();
		f.add(logArea, new GBC(0,5,2,1).setFill(GridBagConstraints.BOTH).setSpan(1, 1));
		
		addListennerToButton(addLogPathBtn,logPath,JFileChooser.FILES_ONLY);
		addListennerToButton(addRootPathBtn,rootPath,JFileChooser.DIRECTORIES_ONLY);
		addListennerToButton(addExportPathBtn,exportPath,JFileChooser.DIRECTORIES_ONLY);
		
		exportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String logPathStr = logPath.getText();
				String rootPathStr = rootPath.getText();
				String exportPathStr = exportPath.getText();
				if(StringUtils.isBlank(logPathStr)){
					JOptionPane.showConfirmDialog(null,  "请选择日志路径!","提示", JOptionPane.OK_OPTION);
					return ;
				}
				if(StringUtils.isBlank(rootPathStr)){
					JOptionPane.showConfirmDialog(null,  "请选择项目WebRoot路径!","提示", JOptionPane.OK_OPTION);
					return ;
				}
				if(StringUtils.isBlank(exportPathStr)){
					JOptionPane.showConfirmDialog(null,  "请选择输出路径!","提示", JOptionPane.OK_OPTION);
					return ;
				}
				FileExporterUtils.export(logPathStr, rootPathStr, exportPathStr,logArea);
				String log = logArea.getText();
				log = log +"\n"+ "----------------------------分隔线-------------------------------------------------";
				logArea.setText(log);
			}
		});
		
		cleanLogBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logArea.setText("");
			}
		});
		f.setVisible(true);
		
	}

	private static void addListennerToButton(final Button btn,
			final TextField tf,final int type) {
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(type);
				int ret = chooser.showDialog(new Label(), "选择");
				if(ret==JFileChooser.APPROVE_OPTION){    //判断窗口是否点的是打开或保存
					File f = chooser.getSelectedFile();
					tf.setText(f.getPath());
				}
			}
		});
	}
}
