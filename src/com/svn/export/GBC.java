package com.svn.export;

import java.awt.GridBagConstraints;

public class GBC extends GridBagConstraints {
	public GBC(){}
	public GBC(int x ,int y ){
		this.gridx = x;
		this.gridy = y;
		this.gridwidth=1;
		this.gridheight=1;
	}
	public GBC(int x ,int y ,int width ,int height){
		this.gridx = x;
		this.gridy = y;
		this.gridwidth=width;
		this.gridheight=height;		
	}
	public GBC setFill(int fill){
		this.fill = fill;
		return this;
	}
	
	public GBC setIpad(int x ,int y ){
		this.ipadx = x;
		this.ipady = y;
		return this;
	}
	
	public GBC setSpan(int x ,int y ){
		this.gridwidth=x;
		this.gridheight=y;
		return this;
	}
}
