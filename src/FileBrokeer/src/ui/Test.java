package ui;

import functools.FileHandler;
import functools.L;

public class Test {
	public static void main(String[] args) {
		try {
			FileHandler handler = new FileHandler("/Users/darkh/Desktop/RecordBook");
			handler.getAllFiles();
			
			handler.display();
			L.l(handler.count());
//			FileHandler.Broken("/Users/darkh/Desktop/test/gradle.properties", 20);
			handler.BrokensAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
