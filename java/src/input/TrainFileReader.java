package input;

import java.io.IOException;

import dataset.TimeSlice;
import bayessian.RandomVector;
import bayessian.Sample;

public class TrainFileReader {
	
	CSVFileReader fileReader = null; 
	String[] firstLine = null; 
		
	public TrainFileReader(String pathname) throws IOException {
		this.fileReader = new CSVFileReader(pathname);
		this.firstLine = fileReader.getRow(0);
	}
	
	public int getTimeInstants() {	
	String lastElement = firstLine[firstLine.length - 1]; //obter ultima posicao da primeira linha do ficheiro
		return (Integer.parseInt(lastElement.substring(lastElement.lastIndexOf("_")+1, lastElement.length())))+1;
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
		int numberOfSubjects = getSubjectsCount();
		
		for(int i = varIndex; i < firstLine.length; i += numberOfRVars) { 	//percorrer instantes de tempo da RVar
			for(int j = 1; j <= numberOfSubjects; j++) { 					//percorrer amostras da RVar
				sample = Integer.parseInt(fileReader.getPosition(j, i)); 	//recolher amostra da linha j e da coluna i
				if(sample > max) {
					max = sample; 
				}
			}
		}
	
		return max+1;
	}
	
	public TimeSlice getTimeSlice(int time) {
		int numberOfSubjects = getSubjectsCount();
		int numberOfRVars = getRVarsCount();
		int k = 0;
		
		TimeSlice timeSlice = new TimeSlice(numberOfSubjects); //chamar construtor da TimeSlice	
		
		for(int j = 1; j <= numberOfSubjects; j++) { //percorrer todos os subjects
			k = 0;
			Sample sample = new Sample(numberOfRVars);										//cada subject corresponde a uma Sample
			for(int i = numberOfRVars*time; i < numberOfRVars*time+numberOfRVars; i++) { 	//percorrer RVars desse instante de tempo, para um dado subject (j)
				sample.setValue(k, Integer.parseInt(fileReader.getPosition(j, i)));			//recolher amostra da linha j e da coluna i			
				k++;
			}
			if(timeSlice.addSample(sample) == false) {
				break;
			}
		}
	
		return timeSlice;	
	}
		
	/*public static void main(String[] args) throws IOException {
		TrainFileReader reader = new TrainFileReader("short-test-data.csv");
		
		System.out.println("#Subjects: " + reader.getSubjectsCount());
		System.out.println("#RVars: " + reader.getRVarsCount());
		System.out.println("#TimeInstants: " + reader.getTimeInstants());

		for(int k = 0; k < reader.getSubjectsCount(); k++) {
			for(int i = 0; i < reader.getRVarsCount(); i++) {
				System.out.println(reader.getTimeSlice(0).getSample(k).getValue(i));
			}
		}
	}*/
}