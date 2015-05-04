package input;

import java.io.IOException;
import java.util.Arrays;

public class TrainFileReader extends CSVFileReader {
	
	String[] firstLine = this.contents.get(1); //segunda posicao da lista <-> primeira linha do ficheiro
	int numberOfRVars = getRVars();
	int alturaArray = getLineCount();
		
	public TrainFileReader(String pathname) throws IOException {
		super(pathname);	
	}
	
	public int getSubjects() {
		return alturaArray - 2; //primeira posicao da lista � o sep e a segunda as RVars
	}
	
	public int getRVars() {
		int i = 0, numberOfRVars = 0;	
		
		while(i < firstLine.length){
			if(firstLine[i].endsWith("_0")){
				numberOfRVars++;
			}
			else{
				break;
			}
			i++;
		}
		
		return numberOfRVars;
	}
	
	public String[] getNameRVars() {
		int i = 0;
		String[] nameRVars = new String[numberOfRVars];
		
		while(i < numberOfRVars){
			nameRVars[i] = firstLine[i].substring(0, firstLine[i].indexOf("_0"));
			i++;
		}
		
		return nameRVars;		
	}
	
	public int getRange(String nameRVar) {
		int range = 0;

		return range;
	}
	
	public int getTimeInstants() {	
		int timeInstants = 0;
		String lastElement = firstLine[firstLine.length-1];
		
		timeInstants = Integer.parseInt(lastElement.substring(lastElement.indexOf("_")+1,lastElement.length()));
		
		return timeInstants+1;
	}
	
	public static void main(String[] args) throws IOException {
		TrainFileReader reader = new TrainFileReader("train-data.csv");
		
		System.out.println("#Subjects: " + reader.getSubjects());
		System.out.println("#RVars: " + reader.getRVars());
		System.out.println("#TimeInstants: " + reader.getTimeInstants());
		System.out.println("Name of RVars: " + Arrays.deepToString(reader.getNameRVars()));
	}
}
