package input;

import java.io.IOException;

import dataset.Sample;
import dataset.TimeSlice;
import bayessian.RandomVector;

public class DataFileReader {
	
	CSVFileReader fileReader = null; 
	String[] firstLine = null; 
		
	public DataFileReader(String pathname) throws IOException {
		this.fileReader = new CSVFileReader(pathname);
		this.firstLine = fileReader.getRow(0);
	}
	
	public int timeInstantCount() {
		String lastElement = firstLine[firstLine.length - 1]; // obter ultima posicao da primeira linha do ficheiro
		int indexOfUnderScore = lastElement.lastIndexOf("_");	// obter indice do ultimo underscore (_)
		
		if(indexOfUnderScore == -1) {
			// contem apenas um instante de tempo - test file
			return 1;
		}
				
		return 1 + Integer.parseInt(lastElement.substring(indexOfUnderScore + 1, lastElement.length()));
	}
	
	public int subjectCount() {
		return fileReader.getRowCount() - 1; 
	}
	
	public int randomVarCount() {	
		return firstLine.length / timeInstantCount();
	}
	
	public RandomVector getRVars() {		
		
		String[] nameRVars = null;
		int[] rangeRVars = null;
		int numberOfRVars = randomVarCount();
		
		if(nameRVars == null) {
			nameRVars = new String[numberOfRVars];
			rangeRVars = new int[numberOfRVars];
			for(int i = 0; i < numberOfRVars; i++) {
				nameRVars[i] = firstLine[i].substring(0, firstLine[i].lastIndexOf("_0")); //procurar ultima ocorrencia de "_0"
				rangeRVars[i] = getRangeSingleRVar(i, numberOfRVars);
			}
		}
		
		RandomVector randomVector = new RandomVector(nameRVars, rangeRVars); //chamar construtor do RandomVector
		
		return randomVector;
	}
	
	private int getRangeSingleRVar(int varIndex, int numberOfRVars) {
		int sample = 0, max = 0;
		int numberOfSubjects = subjectCount();
		
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
		int numberOfSubjects = subjectCount();
		int numberOfRVars = randomVarCount();
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
		System.out.println("#RVars: " + reader.randomVarCount());
		System.out.println("#TimeInstants: " + reader.getTimeInstants());

		for(int k = 0; k < reader.getSubjectsCount(); k++) {
			for(int i = 0; i < reader.randomVarCount(); i++) {
				System.out.println(reader.getTimeSlice(0).getSample(k).getValue(i));
			}
		}
	}*/
}