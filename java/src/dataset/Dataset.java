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
	
	/**
	 * @return	numero de amostras no dataset
	 */
	public int size() {
		return samplesOfTimeT.size();
	}
	
}
