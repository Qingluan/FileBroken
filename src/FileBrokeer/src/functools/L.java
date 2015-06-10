package functools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class L {
	public static void l(Object ...contents){
		for(Object s: contents){
			System.out.println(s);
		}
	}
	
	public static void lf(Object obj){
		File f = new File("/Users/darkh/Desktop/log.error");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.append(obj.toString());
		writer.close();
	}
}
