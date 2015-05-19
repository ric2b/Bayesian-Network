package input;

import java.io.IOException;

import dataset.Sample;
import dataset.TimeSlice;
import bayessian.RandomVariable;
import bayessian.StaticRandomVariable;

public class DataFileReader {
	
	CSVFileReader fileReader = null; 
	String[] firstLine = null; 
	
	/**
	 * Opens a train files given by the obsolute or relative pathname
	 * @param pathname		pathname of the files
	 * @throws IOException	when a I/O failure occurs
	 */
	public DataFileReader(String pathname) throws IOException {
		this.fileReader = new CSVFileReader(pathname);
		this.firstLine = fileReader.getRow(0);
	}
	
	/**
	 * Returns the number of time instants of the train file.
	 * @return number of time instants of the train file
	 */
	public int timeInstantCount() {
		String lastElement = firstLine[firstLine.length - 1]; // obter ultima posicao da primeira linha do ficheiro
		int indexOfUnderScore = lastElement.lastIndexOf("_");	// obter indice do ultimo underscore (_)
		
		if(indexOfUnderScore == -1) {
			// contem apenas um instante de tempo - test file
			return 1;
		}
				
		return 1 + Integer.parseInt(lastElement.substring(indexOfUnderScore + 1, lastElement.length()));
	}
	
	/**
	 * Returns the number of subjects of the train file.
	 * @return number of time subjects of the train file
	 */
	public int subjectCount() {
		return fileReader.getRowCount() - 1; 
	}
	
	/**
	 * Returns the number of random variables of the train file.
	 * @return number of time random variables of the train file
	 */ 
	public int randomVarCount() {
		// numero de elementos da primeira linha
		return firstLine.length;
	}
	
	/**
	 * Returns an array of all the random variables in the train file. 
	 * @return array of all the random variables in the train file
	 */
	public RandomVariable[] getRVars() {		
		
		int numberOfRVars = randomVarCount();
		RandomVariable[] vars = new RandomVariable[numberOfRVars];
		String varName = null;		// nome da variavel
		int timeInstant = -1;		// instante de tempo da variavel
		int instantCount = timeInstantCount();
		int varInOneInstant = numberOfRVars / instantCount;
		
		int[] ranges = getRanges();
		
		for(int i = 0; i < numberOfRVars; i++) {
			// obter indice do underscore
			int indexOfUnderScore = firstLine[i].lastIndexOf("_");
			
			if(indexOfUnderScore == -1) {
				// dados de um test file que só têm 1 instante de tempo
				varName = firstLine[i];
				timeInstant = 0;
			} else {
				// nome da variavel é dado apenas pela parte à esquerda do underscore
				varName = firstLine[i].substring(0, indexOfUnderScore);
				// instante de tempo da variavel é dado pela o numero à direita do underscore
				timeInstant = Integer.parseInt(firstLine[i].substring(indexOfUnderScore + 1, firstLine[i].length()));
			}
			
			int rangeIndex = i % varInOneInstant;
			vars[i] = new StaticRandomVariable(varName, ranges[rangeIndex], timeInstant);
		}

		return vars;
	}
	
	/**
	 * Returns an array of all the ranges of each random variables in the train file. 
	 * @return array with ranges of each random variables in the train file.
	 */
	private int[] getRanges() {
		
		int numberOfSubjects = subjectCount();
		int numberOfRVars = randomVarCount() / timeInstantCount();	// numero de variveis aleatorias num instante de tempo
		int[] ranges = new int[numberOfRVars];
		
		//percorrer todas as random vars
		for (int varIndex = 0; varIndex < numberOfRVars; varIndex++) {
			
			int max = 0;	// valor maximo actual da variavel
			//percorrer instantes de tempo da RVar
			for(int i = varIndex; i < firstLine.length; i += numberOfRVars) { 	
				for(int j = 1; j <= numberOfSubjects; j++) { 					//percorrer amostras da RVar
					
					try {
						int sample = Integer.parseInt(fileReader.getPosition(j, i)); 	//recolher amostra da linha j e da coluna i
						if(sample > max) {
							max = sample; 
						}
					}
					catch (ArrayIndexOutOfBoundsException except) {
						// este subject não tem mais amostras
					}
				}
			}
			
			ranges[varIndex] = max + 1;
			
		}
		
		return ranges;
	}
	
	/**
	 * Returns the time slice of the data file of time @time 
	 * @return time slice of the data file of time @time
	 */
	public TimeSlice getTimeSlice(int time) {
		int numberOfSubjects = subjectCount();
		int numberOfRVars = randomVarCount() / timeInstantCount();	// numero de variaveis num instante de tempo
		int k = 0;
		
		Sample.setLength(numberOfRVars);
		TimeSlice timeSlice = new TimeSlice(numberOfSubjects); //chamar construtor da TimeSlice	
		
		try{
			for(int j = 1; j <= numberOfSubjects; j++) { //percorrer todos os subjects
				k = 0;
				Sample sample = new Sample();										//cada subject corresponde a uma Sample
				for(int i = numberOfRVars*time; i < numberOfRVars*time+numberOfRVars; i++) { 	//percorrer RVars desse instante de tempo, para um dado subject (j)
					try {
						sample.setValue(k, Integer.parseInt(fileReader.getPosition(j, i)));		//recolher amostra da linha j e da coluna i	
						k++;
					}
					catch (ArrayIndexOutOfBoundsException except) {
						sample = null; // se para um dado subject nao houver uma amostra de tempo entao sample toma o valor null
					}
				}
				if(timeSlice.addSample(sample) == false) {
					break;
				}
			}
		} catch(Exception e) {
			// nunca deve ocorrer
			e.printStackTrace();
		}
	
		return timeSlice;	
	}
	
}