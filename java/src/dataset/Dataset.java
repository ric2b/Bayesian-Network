package dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Dataset {
	
	protected List<Sample> samplesOfTimeT = new ArrayList<>();	// amostras de um único instante temporal t
	
	/**
	 * O constructor sem argumentos cria um dataset vazio
	 */
	public Dataset() {
		;// nada para fazer mas este método deve existir
	}
	
	/**
	 * O costructor recebe um array de samples e adiciona todas à lista de amostras do dataset 
	 * @param samples	array de amostras a adicionar ao dataset
	 */
	public Dataset(Sample[] samples) {
		// adicionar todas as amostras à lista de amostras
		for(Sample sample : samples) {
			if(sample == null) {
				throw new NullPointerException("dataset não aceita Samples a null");
			}
			
			samplesOfTimeT.add(sample);
		}
	}
	
	/**
	 * O costructor recebe uma coleção de samples e adiciona todas à lista de amostras do dataset
	 * @param samples coleção de amostras a adicionar ao dataset
	 */
	public Dataset(Collection<? extends Sample> samples) {
		samplesOfTimeT.addAll(samples);
	}
	
	/**
	 * O constructor recebe um time slice e adiciona todas as amostras que este contém. Amostras
	 * vazias não são consideradas.
	 * @param timeSlice
	 */
	public Dataset(TimeSlice timeSlice) {
		for(Sample sample : timeSlice) {
			if(sample == null) {
				throw new NullPointerException("dataset não aceita Samples a null");
			}
			
			samplesOfTimeT.add(sample);
		}
	}
	
	/**
	 * Devolve a amostra com indice dado por indexOfSample
	 * @param indexOfSample	indice da amostra que se pretende ler
	 * @return	amostra com o indice dado
	 */
	public Sample getSample(int indexOfSample) {
		return samplesOfTimeT.get(indexOfSample);
	}
	
	public int countValues(int indexI, int value, int[] indexes, int[] values) {
		int count = 0;
		// o numero de amostras em cada conjunto de amostras é igual
		int sampleCount = samplesOfTimeT.size();
		
		for(int i = 0; i < sampleCount; i++) {
			boolean toCount = true;	// indica se esta linha de amostras é para contar como uma combinação
			for(int j = 0; j < indexes.length; j++) {
				if(values[j] != getValue(indexes[j], i)) {
					// esta linha não contem a configuração dos pais correcta
					toCount = false;
					break;
				}
			}
			
			if(toCount && value == getValue(indexI, i)) {
				// linha cumpriu a configuração dos pais e o valor da variavel
				count++;
			}
		}
		
		return count;
	}
	
	protected int getValue(int index, int sample) {
		return this.samplesOfTimeT.get(sample).getValue(index);
	}
	
	/**
	 * @return	numero de amostras no dataset
	 */
	public int size() {
		return samplesOfTimeT.size();
	}
	
	public String toString() {
		String string = "";
		for(Sample sample : samplesOfTimeT) {
			string += sample.toString() + '\n';
		}
		return string;
	}
	
}
