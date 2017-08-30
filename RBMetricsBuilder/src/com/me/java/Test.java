package com.me.java;

import java.io.IOException;

public class Test {
	
	private void processFile() throws IOException{
		String s = "model: new RB.DiffViewerPageModel({";
		System.out.println(s.length());
	}
	
	public static void main(String[] args) throws IOException{
		Test test = new Test();
		test.processFile();
		
	}

}
