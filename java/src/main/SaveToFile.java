package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveToFile {
	PrintWriter out = null;	
	
	public SaveToFile(String filename){
		try {
			out = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		out.close();
	}	
	
	public static void main(String[] args) {
		SaveToFile writer = new SaveToFile("hello.txt");
		writer.println("howdy neighbour!", true);
		writer.close();
	}

}
