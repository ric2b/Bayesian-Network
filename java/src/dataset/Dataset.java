package dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import bayessian.BayessianNetwork;
import bayessian.InstanceCounting;
import bayessian.RandomVariable;

public class Dataset implements Iterable<Sample> {
	
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
	
	public double getScore(BayessianNetwork<? extends RandomVariable> bayessian, Dataset dataset) {
		
		double score = 0;		
		for(int i: bayessian) { // for each node of the Network
			int Q = bayessian.getParentConfigurationCount(i);
			
			for(int J = 0; J < Q; J++) {
				int N = bayessian.getRange(i);
				
				// calcular Nijks
				int[] Nijks = InstanceCounting.getNijks(i,J, bayessian, dataset);
				int Nij = 0;
				for(int k = 0; k < N; k++) {
					Nij += Nijks[k];				
				}
				
				// calcular score
				if(Nij != 0) {
					for(int k = 0; k < N; k++) {
						if(Nijks[k] != 0)
							score += Nijks[k] * Math.log((Nijks[k]*1.0)/Nij) / Math.log(2);
					}
				}
			}
		}
		
		return score;
	}
	
	public static int[] getNijks(int i, int J, BayessianNetwork<? extends RandomVariable> BN, Dataset dataset) {
		return dataset.getAllCounts(i, BN.getRange(i), BN.getParents(i), InstanceCounting.mapJToj(BN.getParentRanges(i),J));
	}
	
	public int[][][] getAllCounts(BayessianNetwork<? extends RandomVariable> bayessian) {
		// o numero de amostras em cada conjunto de amostras é igual
		int sampleCount = samplesOfTimeT.size();
		
		int[][][] Nijks = new int[bayessian.size()][][];
		
		for(int i: bayessian) {
			int Q = bayessian.getParentConfigurationCount(i);
			Nijks[i] = new int[Q][];
			for(int J = 0; J < Q; J++) {
				int range = bayessian.getRange(i);
				Nijks[i][J] = new int[range];
			}
		}
		
		for(int sample = 0; sample < sampleCount; sample++) {
			
			boolean toCount = true;
			for(int i: bayessian) { // for each node of the Network
				int Q = bayessian.getParentConfigurationCount(i);
				
				for(int J = 0; J < Q; J++) {

					int range = bayessian.getRange(i);
					int[] indexes = bayessian.getParents(i);
					int[] values = InstanceCounting.mapJToj(bayessian.getParentRanges(i),J);
					
					for(int k = 0; k < range; k++) {
						toCount = true;
						for(int j = 0; j < indexes.length; j++) {
							if(values[j] != getValue(indexes[j], sample)) {
								// esta linha não contem a configuração dos pais correcta
								toCount = false;
								break;
							}
						}
						
						if(toCount && k == getValue(i, sample)) {
							// linha cumpriu a configuração dos pais e o valor da variavel
							Nijks[i][J][k]++;
						}
					}
				}
			}
			
		}
		
		return Nijks;
	}
	
	public int[] getAllCounts(int indexI, int range, int[] indexes, int[] values) {
		// o numero de amostras em cada conjunto de amostras é igual
		int sampleCount = samplesOfTimeT.size();
		
		int[] Nijk = new int[range];
		
		for(int i = 0; i < sampleCount; i++) {
			boolean toCount = true;	// indica se esta linha de amostras é para contar como uma combinação
			
			for(int k = 0; k < range; k++) {
				for(int j = 0; j < indexes.length; j++) {
					if(values[j] != getValue(indexes[j], i)) {
						// esta linha não contem a configuração dos pais correcta
						toCount = false;
						break;
					}
				}
				
				if(toCount && k == getValue(indexI, i)) {
					// linha cumpriu a configuração dos pais e o valor da variavel
					Nijk[k]++;
				}
			}
		}
		
		return Nijk;
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
	
	protected class SampleIterator implements Iterator<Sample> {
		
		private int position = 0;
			
		@Override
		public boolean hasNext() {
			return position < Dataset.this.samplesOfTimeT.size();
		}

		@Override
		public Sample next() {
			
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			
			return Dataset.this.samplesOfTimeT.get(position++);
		}

		@Override
		public void remove() {
			Dataset.this.samplesOfTimeT.remove(position - 1);
		}
		
	}

	@Override
	public Iterator<Sample> iterator() {
		return new SampleIterator();
	}

}
