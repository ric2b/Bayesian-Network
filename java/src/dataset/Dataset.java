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
	 * Creates an empty dataset
	 */
	public Dataset() {
		;// nada para fazer mas este método deve existir
	}
	
	/**
	 * Constructs a dataset initialized with the samples of the given array. 
	 * @param samples	array of samples to add to the dataset
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
	 * Constructs a dataset initialized with the samples of the given collection. 
	 * @param samples	collection of samples to add to the dataset
	 */
	public Dataset(Collection<? extends Sample> samples) {
		samplesOfTimeT.addAll(samples);
	}
	
	/**
	 * Constructs a dataset initialized with the samples of the given time slice. 
	 * @param samples	time slice of samples to add to the dataset
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
	 * Returns the sample at the given position.
	 * @param indexOfSample	position
	 * @return	sample at the given position
	 */
	public Sample getSample(int indexOfSample) {
		return samplesOfTimeT.get(indexOfSample);
	}
	
	/**
	 * Returns the count of number of samples that have the value @value at the column @indexI and the values
	 * @values at the columns indexes@
	 * @param indexI
	 * @param value
	 * @param indexes
	 * @param values
	 * @return	count
	 */
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
	
	/**
	 * Returns all the Nijks for a given Bayessian Network.
	 * @param bayessian to compute
	 * @return Nijks of the bayessian network
	 */
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
			for(int i: bayessian) {

				int[] parents = bayessian.getParents(i);
				
				int J = 0;
				if(parents.length == 0) {
					J = 0; // configuração vazia
				} else {
					int[] parentValues = new int[parents.length];
					
					// obter valores dos pais para esta amostra
					for(int p = 0; p < parents.length; p++) {
						parentValues[p] = getValue(parents[p], sample);
					}
					
					// obter indice J dos valores dos pais
					J = InstanceCounting.mapjToJ(bayessian.getParentRanges(i), parentValues);
				}
				
				// ler valor da variavel
				int k = getValue(i, sample);
				
				Nijks[i][J][k]++;
			}
		}
		
		return Nijks;
	}

	/**
	 * Returns the value of the dataset at the column @index and in the given sample.
	 * @param index		column of the dataset
	 * @param sample	sample to get the value from
	 * @return	value of the sample
	 */
	protected int getValue(int index, int sample) {
		return this.samplesOfTimeT.get(sample).getValue(index);
	}
	
	/**
	 * Returns the number of samples in dataset 
	 * @return number of samples in dataset
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
