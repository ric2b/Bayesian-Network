package input;

import java.io.IOException;

import dataset.Sample;
import dataset.TimeSlice;
import bayessian.RandomVariable;
import bayessian.StaticRandomVariable;

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
	
	/**
	 * Devolve numero de linhas dos dados do ficheiro de entrada
	 * @return
	 */
	public int subjectCount() {
		return fileReader.getRowCount() - 1; 
	}
	
	/**
	 * Devolve o numero de variáveis aleatorias no ficheiro de entrada. Uma variavel aleatoria é identificada pelo seu
	 * nome e instante de tempo.
	 * @return
	 */
	public int randomVarCount() {
		// numero de elementos da primeira linha
		return firstLine.length;
	}
	
	/**
	 * Obtem um array com todas as variaveis aleatorias do ficheiro. Duas variaveis aleatorias com o mesmo nome em dois 
	 * instantes de tempo diferentes sao consideradas diferentes
	 * @return
	 */
	public RandomVariable[] getRVars() {		
		
		int numberOfRVars = randomVarCount();
		RandomVariable[] vars = new RandomVariable[numberOfRVars];
		String varName = null;		// nome da variavel
		int timeInstant = -1;		// instante de tempo da variavel
		
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
			
			vars[i] = new StaticRandomVariable(varName, ranges[i / numberOfRVars], timeInstant);
		}

		return vars;
	}
	
	/**
	 * Permite obter todos os ranges de todas as variaveis aletorais de forma sequencial. Preferivel a fazê-lo uma a uma.
	 * @return range das variaveis aletorias pela mesma odem que se encontram no ficheiro
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
					int sample = Integer.parseInt(fileReader.getPosition(j, i)); 	//recolher amostra da linha j e da coluna i
					if(sample > max) {
						max = sample; 
					}
				}
			}
			
			ranges[varIndex] = max + 1;
			
		}
		
		return ranges;
	}
	
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
					sample.setValue(k, Integer.parseInt(fileReader.getPosition(j, i)));			//recolher amostra da linha j e da coluna i			
					k++;
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
		
	public static void main(String[] args) throws IOException {
		DataFileReader reader = new DataFileReader("train-data.csv");
		
		System.out.println("#Subjects: " + reader.subjectCount());
		System.out.println("#RVars: " + reader.randomVarCount());
		System.out.println("#TimeInstants: " + reader.timeInstantCount());

		for(int i = 0; i < reader.randomVarCount(); i++) {
			System.out.println("TimeSlice " + i);
			System.out.println(reader.getTimeSlice(i));
		}
	}
	
}