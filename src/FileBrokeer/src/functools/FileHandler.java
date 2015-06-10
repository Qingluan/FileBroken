package functools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

public class FileHandler {
	File rootfile;
	long fileLen ;
	boolean isdir;
	private static int DEEP ;
	private static int NOW_DEEP;
	private LinkedList<File> fileTree;
	private ArrayList<File> dirTree;
	private ExecutorService mulThreads ;
	
	public FileHandler(String path) throws Exception {
		// TODO Auto-generated constructor stub
		mulThreads = Executors.newFixedThreadPool(50);
		rootfile = new File(path);
		if (!rootfile.exists() ){
			throw new FileNotFoundException(path);
		}
		
		isdir = rootfile.isDirectory();
		
	}
	
	public void  setThreadNumber(int number) {
		mulThreads = Executors.newFixedThreadPool(number);
	}
	
	public FileHandler(String path,int coreNum) throws Exception {
		// TODO Auto-generated constructor stub
		mulThreads = Executors.newFixedThreadPool(coreNum);
		rootfile = new File(path);
		if (!rootfile.exists() ){
			throw new FileNotFoundException(path);
		}
		
		isdir = rootfile.isDirectory();
		
	}
	/**
	 *  this will broken all files and dirs
	 */
	public boolean BrokensAll(final JProgressBar progess,final JLabel counter ,JButton btnBroken) throws Exception{
		long time_stamp = System.currentTimeMillis();
		final long all = fileTree.size();
//		int ThreadCount = 0;
		
		Runnable counterRunnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while ( !fileTree.isEmpty()){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					L.lf(all+","+fileTree.size()+"\n");
					
					progess.setValue((int)( ((all-fileTree.size())/(float)(all)) *100) );
					counter.setText(String.valueOf(all -fileTree.size()));
					L.l(fileTree.size());
					
					
				}
//				progess.setValue((int) ((all-fileTree.size);
			}
		};
		mulThreads.execute(counterRunnable);
		
		while(! this.fileTree.isEmpty()){
//			final File file = fileTree.pop();
			final File file = fileTree.pop();
			Thread thread = new Thread( new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {
						if(!file.exists()){
							try {
								this.finalize();
							} catch (Throwable e) {
								// TODO Auto-generated catch block
//								e.printStackTrace();
								L.lf(e.toString());
							}
						}
						if(file.canWrite()){
							FileHandler.Broken(file, 20);
						}else if(file.exists()){
							L.lf(file.getAbsolutePath()+"   strange can just deleted"+"\n");
							file.delete();
						}else{
							L.lf(file.getAbsolutePath()+"   more strange can just deleted"+"\n");
							file.delete();
						}

					} catch (FileNotFoundException  e) {
						// TODO Auto-generated catch block
						L.l(file.getAbsolutePath() + " is dealed");
						L.lf(file.getAbsolutePath()+"deaded\n");
					} catch ( Exception e) {
						// TODO: handle exception
						file.delete();
						e.printStackTrace();
					}
				}
			});
			
			
			mulThreads.execute(thread);
//			ThreadCount += 1;
			
			
			
		}
		mulThreads.shutdown();
		
		while(!fileTree.isEmpty()){
			Thread.sleep(100);
		}
		
		if (this.count()==0){
			btnBroken.setText("delete Dirs");
		}
		
		
		
		
		L.l(new Time(System.currentTimeMillis()-time_stamp));
		try{
			while (! dirTree.isEmpty()){
				for(File f:dirTree){
					try{
						f = f.getAbsoluteFile();
						if (!f.delete()){
							L.l(f.getAbsolutePath() + " not delete");
						}
					}catch ( SecurityException e){
						L.l(e.toString());
					}
				}
				getAllDirs();
			}
		}catch (NullPointerException e){
			L.l("ok delete ok ");
			return true;
		}
		
		
		
		return false;
	}
	
	
	
	
	public void getAllFiles(int deep){
		FileHandler.NOW_DEEP = 0;
		FileHandler.DEEP = deep;
		fileTree = new LinkedList<File>();
		walk(this.fileTree, rootfile);
		
		FileHandler.DEEP = 0;
		FileHandler.NOW_DEEP = 0;
		
		this.getAllDirs(deep);
		
	}

	public void getAllFiles(){
		FileHandler.DEEP = -1;
		fileTree = new LinkedList<File>();
		walk(fileTree, rootfile);
		
		this.getAllDirs();
		
	}
	
	public void getAllDirs(){
		FileHandler.DEEP = -1;
		dirTree = new ArrayList<File>();
		walkDirs(this.dirTree, rootfile);
	}
	
	public void getAllDirs(int deep){
		FileHandler.NOW_DEEP = 0;
		FileHandler.DEEP = deep;
		dirTree = new ArrayList<File>();
		walkDirs(this.dirTree, rootfile);
		
		FileHandler.DEEP = 0;
		FileHandler.NOW_DEEP = 0;
		
	}
	
	public void display(){
		for (File f : this.fileTree){
			L.l(f.getAbsolutePath());
		}
		L.l("dir : ");
		for (File f : this.dirTree){
			L.l(f.getAbsolutePath());
		}
	}
	
	
	
	public static byte[] fill(int j ,long num){
		
		ByteArrayOutputStream bys =  new ByteArrayOutputStream();
		for(long i =0;i< num;i++){
			bys.write(j);
		}
		return bys.toByteArray();
		
		
	}
	
	public  static boolean Broken(String path,int times) throws Exception{
		File file = new File(path);
		if (!file.exists()){
			throw new Exception("File not found");
		}
		long fileLen = file.length();
		byte[] rubbish_odd = fill(0x55, fileLen); // odd turn we will fill 0x55
		byte[] rubbish_even = fill(0xaa, fileLen); //even turn we will fill 0xaa
		int count = 0;
		try {
			for(count =0 ;count < times ;count++){
				FileOutputStream out = new FileOutputStream(file);
				if (count %2 == 0){
					out.write(rubbish_odd);
				}else{
					out.write(rubbish_even);
				}
				
				out.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file.delete();
	}
	
	
	
	public synchronized static boolean Broken(File brokenFile,int times) throws Exception{
		@SuppressWarnings("unused")
		String path = brokenFile.getAbsolutePath();
		if (!brokenFile.exists()){
			throw new Exception("File not found");
		}
		long fileLen = brokenFile.length();
		byte[] rubbish_odd = fill(0x55, fileLen); // odd turn we will fill 0x55
		byte[] rubbish_even = fill(0xaa, fileLen); //even turn we will fill 0xaa
		int count = 0;
		try {
			for(count =0 ;count < times ;count++){
				
				FileOutputStream out = new FileOutputStream(brokenFile);
				if (count %2 == 0){
					out.write(rubbish_odd);
				}else{
					out.write(rubbish_even);
				}
				
				out.close();
		
			}
		
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			L.lf("coutnt :"+count+"\n");
		}
		
		return brokenFile.delete();
	}

	
	public boolean delete(File f){
		return f.delete();
	}
	/**
	 * this is a recurily function 
	 *  
	 * @param filesList 
	 * @param f
	 * @return gotfiles 
	 */
	private LinkedList<File> walk(LinkedList<File> filesList,File f){
		if (DEEP != -1){
			if (FileHandler.NOW_DEEP >= FileHandler.DEEP){
				if (f.isFile()){
					filesList.add(f);
					return filesList;
				}else{
					return filesList;
				}
			}
			FileHandler.NOW_DEEP += 1;
		}
		if (f.isFile()){
			filesList.add(f);
			return filesList;
		}else{
			for(File file : f.listFiles()){
				walk(filesList, file);
			}
		}
		return filesList;	
	}
	
	private ArrayList<File> walkDirs(ArrayList<File> filesList,File f){
		if (DEEP != -1){
			if (FileHandler.NOW_DEEP >= FileHandler.DEEP){
				if (f.isDirectory()){
					filesList.add(f);
					return filesList;
				}else{
					return filesList;
				}
			}
			FileHandler.NOW_DEEP += 1;
		}
		if (f.isFile()){
			return filesList;
		}else{
			filesList.add(f);
			for(File file : f.listFiles()){
				walkDirs(filesList, file);
			}
		}
		return filesList;	
	}
	
	public boolean deleteDir(File f){
		return f.delete();
	}

	public long count(){
		return fileTree.size();
	}
	public void display(JTextArea table) {
		// TODO Auto-generated method stub
		for (File f : this.fileTree){
			L.l(f.getAbsolutePath());
//			table.add()
			table.insert(f.getAbsolutePath()+" ---- ok\n  ",0);
			
		}
		L.l("dir : ");
		for (File f : this.dirTree){
			L.l(f.getAbsolutePath());
			table.insert(f.getAbsolutePath()+" ---- ok\n  ",0);
		}
	}
}
