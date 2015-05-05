package input;

import java.io.IOException;
//import java.util.Arrays;

import dataset.TimeSlice;
import bayessian.RandomVector;

public class TrainFileReader extends CSVFileReader {
	
	String[] firstLine = this.contents.get(0); //primeira posicao da lista <-> primeira linha do ficheiro
	String lastElement = firstLine[firstLine.length-1]; //ultima posicao da primeira posicao da lista <-> nome e ultimo instante de tempo da ultima RVar 
	
	int arrayHeight = -1;
	int timeInstants = -1;
	int numberOfSubjects = -1;
	int numberOfRVars = -1;
	String[] nameRVars = null;
	int[] rangeRVars = null;
	
	RandomVector randomVector = null;
	TimeSlice timeSlice = null;

	public TrainFileReader(String pathname) throws IOException {
		super(pathname);
		arrayHeight = getLineCount();
		timeInstants = (Integer.parseInt(lastElement.substring(lastElement.lastIndexOf("_")+1,lastElement.length())))+1;
		numberOfSubjects = arrayHeight - 1;	//primeira posicao da lista tem as RVars
		numberOfRVars = firstLine.length / timeInstants;
	}
	
	public int getTimeInstants() {			
		return timeInstants;
	}
	
	public int getSubjectsCount() {
		return numberOfSubjects; 
	}
	
	public int getRVarsCount() {	
		return numberOfRVars;
	}
	
	public String[] getNameRVars() {
		
		if(nameRVars == null) {
			nameRVars = new String[numberOfRVars];
			rangeRVars = new int[numberOfRVars];
			for(int i = 0; i < numberOfRVars; i++) {
				nameRVars[i] = firstLine[i].substring(0, firstLine[i].lastIndexOf("_0")); //procurar ultima ocorrencia de "_0"
				rangeRVars[i] = getRangeSingleRVar(i);
			}
		}
		
		randomVector = new RandomVector(nameRVars, numberOfRVars); //chamar construtor do RandomVector
		
		return nameRVars;
	}
	
	public int getRangeSingleRVar(int i_Arg) {
		int sample = 0, max = 0;
		
		for(int i = i_Arg; i < firstLine.length; i += numberOfRVars) { //percorrer instantes de tempo da RVar
			for(int j = 1; j <= numberOfSubjects; j++) { //percorrer amostras da RVar
				sample = Integer.parseInt((this.contents.get(j))[i]); //recolher amostra da linha j e da coluna i
				if(sample > max) {
					max = sample; 
				}
			}
		}
	
		return max+1;
	}
	
	public int[] getRangeAllRVars() {	
		return rangeRVars;
	}
	
	public TimeSlice getTimeSlice(int time) {
		
		timeSlice = new TimeSlice(randomVector, numberOfSubjects); //chamar construtor da TimeSlice
		
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
