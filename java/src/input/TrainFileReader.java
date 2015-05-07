package input;

import java.io.IOException;
//import java.util.Arrays;

import dataset.TimeSlice;
import bayessian.RandomVector;

public class TrainFileReader {
	
	CSVFileReader fileReader = null; 
	String[] firstLine = null; 								//primeira posicao da lista <-> primeira linha do ficheiro 
		
	public TrainFileReader(String pathname) throws IOException {
		this.fileReader = new CSVFileReader(pathname);
		this.firstLine = fileReader.getRow(0);
	}
	
	public int getTimeInstants() {	
		String[] lastRow = fileReader.getRow(fileReader.getRowCount() - 1);	//obter ultima linha do ficheiro
		String lastElement = lastRow[lastRow.length - 1];					//obter ultima posição da ultima linha do ficheiro
		return (Integer.parseInt(lastElement.substring(lastElement.lastIndexOf("_")+1,lastElement.length())))+1;
	}
	
	public int getSubjectsCount() {
		return fileReader.getRowCount() - 1; 
	}
	
	public int getRVarsCount() {	
		return firstLine.length / getTimeInstants();
	}
	
	public RandomVector getRVars() {
		
		String[] nameRVars = null;
		int[] rangeRVars = null;
		RandomVector randomVector = null;
		int numberOfRVars = getRVarsCount();
		
		if(nameRVars == null) {
			nameRVars = new String[numberOfRVars];
			rangeRVars = new int[numberOfRVars];
			for(int i = 0; i < numberOfRVars; i++) {
				nameRVars[i] = firstLine[i].substring(0, firstLine[i].lastIndexOf("_0")); //procurar ultima ocorrencia de "_0"
				rangeRVars[i] = getRangeSingleRVar(i, numberOfRVars);
			}
		}
		
		randomVector = new RandomVector(nameRVars, rangeRVars); //chamar construtor do RandomVector
		
		return randomVector;
	}
	
	private int getRangeSingleRVar(int varIndex, int numberOfRVars) {
		int sample = 0, max = 0;
		
		for(int i = varIndex; i < firstLine.length; i += numberOfRVars) { 	//percorrer instantes de tempo da RVar
			for(int j = 1; j <= fileReader.getRowCount(); j++) { 			//percorrer amostras da RVar
				sample = Integer.parseInt(fileReader.getPosition(i, j)); 	//recolher amostra da linha j e da coluna i
				if(sample > max) {
					max = sample; 
				}
			}
		}
	
		return max+1;
	}
	
	public TimeSlice getTimeSlice(int time) {
		
		RandomVector randomVector = getRVars();
		int numberOfSubjects = fileReader.getRowCount() - 1;
		
		TimeSlice timeSlice = null;
		
		timeSlice = new TimeSlice(randomVector, numberOfSubjects); //chamar construtor da TimeSlice
		
		//construir timeslice
		
		return timeSlice;	
	}
		
	/*public static void main(String[] args) throws IOException {
		TrainFileReader reader = new TrainFileReader("short-test-data.csv");
		
		System.out.println("#Subjects: " + reader.numberOfSubjects);
		System.out.println("#RVars: " + reader.numberOfRVars);
		System.out.println("#TimeInstants: " + reader.timeInstants);
		System.out.println("Name of RVars: " + Arrays.deepToString(reader.getNameRVars()));
		System.out.println("Range of RVars: " + Arrays.toString(reader.getRangeAllRVars()));
	}*/
}
