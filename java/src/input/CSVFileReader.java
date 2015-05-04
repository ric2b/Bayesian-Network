package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVFileReader {
	
	protected ArrayList<String[]> contents = new ArrayList<>();
	protected int cursorRow = 0, cursorColumn = 0;	//cursor que indica posicao no ficheiro em memoria actual 					 			
			
	public CSVFileReader(String pathname) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(pathname));
		String line = null;
		//carregar ficheiro para a memoria
		while((line = reader.readLine()) != null) {         
	        if(line.contains("sep")) {
				continue;
			}
			//adicionar ao contents um array de strings com os valores entre virgulas da linha
			contents.add(line.split(","));
	    }
	    reader.close();
	}
	
	public String[] readLine() {
		String[] line = null;
		
		if(this.cursorRow < contents.size()) {	//testar se ja estamos no fim do ficheiro
			line = contents.get(this.cursorRow);
			
			//mover cursor para o inicio da proxima linha 
			this.cursorRow++;
			this.cursorColumn = 0;
		}
		
		return line;
	}
	
	public String read() {
		String value = null;
		
		if(this.cursorRow < contents.size()) {	//testar se ja estamos no fim do ficheiro
			value = contents.get(this.cursorRow)[this.cursorColumn];
			
			//mover cursor para proxima coluna
			this.cursorColumn++;
			if(this.cursorColumn >= contents.get(this.cursorRow).length) {
				//chegou-se o fim da linha
				//mover cursor para o inicio da proxima linha
				this.cursorColumn = 0;
				this.cursorRow++;
			}
		}
		
		return value;
	}
	
	public int getLineCount() {
		return contents.size();
	}
	
	public void reset() {
		this.cursorRow = 0; 
		this.cursorColumn = 0;
	}
	
	/*public static void main(String[] args) throws IOException {
		CSVFileReader reader = new CSVFileReader("short-test-data.csv");
		
		String[] line = null;
		while((line = reader.readLine()) != null) {
			System.out.println(Arrays.deepToString(line));
		}
		
		System.out.println();
		
		reader.reset();
		
		String value = null;
		while((value = reader.read()) != null) {
			System.out.print(value + ';');
		}
	}*/
}