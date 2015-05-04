package input;

import java.io.IOException;
import java.util.Arrays;

public class TrainFileReader extends CSVFileReader {
	
	String[] firstLine = this.contents.get(0); //primeira posicao da lista <-> primeira linha do ficheiro
	int numberOfRVars = getRVars();
	int alturaArray = getLineCount();
	int numberOfSubjects = getSubjects();
	int[] rangeRVars = new int[numberOfRVars];
		
	public TrainFileReader(String pathname) throws IOException {
		super(pathname);	
	}
	
	public int getSubjects() {
		return alturaArray - 1; //primeira posicao da lista tem as RVars
	}
	
	public int getRVars() {
		int i = 0, numberOfRVars = 0;	
		
		while(i < firstLine.length){
			if(firstLine[i].endsWith("_0")) {
				numberOfRVars++;
			}
			else {
				break;
			}
			i++;
		}
		
		return numberOfRVars;
	}
	
	public String[] getNameRVars() {
		int i = 0;
		String[] nameRVars = new String[numberOfRVars];
		
		while(i < numberOfRVars)  {
			nameRVars[i] = firstLine[i].substring(0, firstLine[i].lastIndexOf("_0")); //procurar ultima ocorrencia de "_0"
			rangeRVars[i] = getRangeSingleRVar(i);
			i++;
		}
		
		return nameRVars;	
	}
	
	public int getRangeSingleRVar(int i) {
		int j = 1, sample = 0, max = 0;
		
		while(i < firstLine.length) { //percorrer instantes de tempo da RVar
			while(j <= numberOfSubjects) { //percorrer amostras da RVar
				sample = Integer.parseInt((this.contents.get(j))[i]); //recolher amostra da linha j e da coluna i
				if(sample > max) {
					max = sample; 
				}
				j++;
			}
			j = 1;
			i += numberOfRVars;
		}
	
		return max+1;
	}
	
	public int[] getRangeAllRVars() {	
		return rangeRVars;
	}
	
	public int getTimeInstants() {	
		int timeInstants = 0;
		String lastElement = firstLine[firstLine.length-1];
		
		timeInstants = Integer.parseInt(lastElement.substring(lastElement.lastIndexOf("_")+1,lastElement.length()));
		
		return timeInstants+1;
	}
	
	public static void main(String[] args) throws IOException {
		TrainFileReader reader = new TrainFileReader("short-test-data.csv");
		
		System.out.println("#Subjects: " + reader.getSubjects());
		System.out.println("#RVars: " + reader.getRVars());
		System.out.println("#TimeInstants: " + reader.getTimeInstants());
		System.out.println("Name of RVars: " + Arrays.deepToString(reader.getNameRVars()));
		System.out.println("Range of RVars: " + Arrays.toString(reader.getRangeAllRVars()));
	}
}
