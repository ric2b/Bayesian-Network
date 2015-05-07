package input;

import java.io.IOException;
//import java.util.Arrays;


import dataset.TimeSlice;
import bayessian.RandomVector;
import bayessian.Sample;

public class TrainFileReader extends CSVFileReader {
	
	String[] firstLine = this.contents.get(0); //primeira posicao da lista <-> primeira linha do ficheiro
	String lastElement = firstLine[firstLine.length-1]; //ultima posicao da primeira posicao da lista <-> nome e ultimo instante de tempo da ultima RVar 
		
	public TrainFileReader(String pathname) throws IOException {
		super(pathname);	
	}
	
	public int getTimeInstants() {			
		return (Integer.parseInt(lastElement.substring(lastElement.lastIndexOf("_")+1,lastElement.length())))+1;
	}
	
	public int getSubjectsCount() {
		return getLineCount() - 1; 
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
	
	public int getRangeSingleRVar(int i_Arg, int numberOfRVars) {
		int numberOfSubjects = getLineCount() - 1;	//primeira posicao da lista tem as RVars
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
	
	public TimeSlice getTimeSlice(int time) {
		
		int numberOfSubjects = getLineCount() - 1;
		
		TimeSlice timeSlice = new TimeSlice(numberOfSubjects); //chamar construtor da TimeSlice
		Sample sample = new Sample(getRVarsCount());
		
		for(int i = 0; i < numberOfSubjects; i++) {
			sample = new Sample(); //WIP
			if(timeSlice.addSample(sample) == false) {
				break;
			}
		}
	
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
