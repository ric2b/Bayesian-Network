package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveToFile {
	PrintWriter out = null;	
	boolean opened = false;
	
	public SaveToFile(String filename, boolean writeToFile){
		if(writeToFile){
			try {
				out = new PrintWriter(filename);
			} catch (FileNotFoundException e) {
				e.printStackTrace();				
			}
			opened = true;
		}
	}
	
	public void println(String arg, boolean writeToFile){
		System.out.println(arg);
		if(writeToFile)
			out.println(arg);
	}
	
	public void print(String arg, boolean writeToFile){
		System.out.print(arg);
		if(writeToFile)
			out.print(arg);
	}
	
	public void close(){
		if(opened)
			out.close();
	}	
	
	public static void main(String[] args) {
		SaveToFile writer = new SaveToFile("hello.txt", true);
		writer.println("howdy neighbour!", true);
		writer.close();
	}

}
