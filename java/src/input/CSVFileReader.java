package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVFileReader {
	
	private ArrayList<String[]> contents = new ArrayList<>();
	private int cursorRow = 0, cursorColumn = 0;	//cursor que indica posicao no ficheiro em memoria actual 					 			
	private int columnCount = 0;					//numero de colunas de dados
	private int size = 0;							//numero de posicoes com dados do ficheiro
	
	public CSVFileReader(String pathname) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(pathname));
		String line = null;
		//carregar ficheiro para a memoria
		while((line = reader.readLine()) != null) {         
			if(line.contains("sep")) {
				continue;
			}
			//adicionar ao contents um array de strings com os valores entre virgulas da linha
			String[] strings = line.replace(" ", "").replace("\t", "").split(",");
			contents.add(strings);
			
			//o numero de colunas e dado pela linha com maior numero de colunas
			if(strings.length > columnCount) {
				columnCount = strings.length;
			}
			
			//adicionar novas posicoes ao tamanho do ficheiro
			size += strings.length;
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
	
	public void reset() {
		this.cursorRow = 0; 
		this.cursorColumn = 0;
	}
	
	public String[] getRow(int row) {
		return contents.get(row);
	}
	
	public String getPosition(int row, int column) {
		return contents.get(row)[column];
	}
	
	public int getRowCount() {
		return contents.size();
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	public int size() {
		return this.size;
	}
	
}