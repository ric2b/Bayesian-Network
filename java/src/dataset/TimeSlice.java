package dataset;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TimeSlice implements Iterable<Sample> {
	
	private Sample[] timeSlice = null;
	private int k = -1;
	
	public TimeSlice(int numberOfSamples) {
		k = 0; 
		timeSlice = new Sample[numberOfSamples]; //criar array de Sample, ou seja, um array de array de inteiros
		// [[0,1,2],[1,3,4]] <-> [[samples of subject1],[samples of subject2]]
	}
		
	public boolean addSample(Sample sample) {
		if(k == timeSlice.length) {
			return false;
		}
		setSample(k, sample);  // adicionar sample na primeira posicao da TimeSlice que estiver vazia (posicao indicada por k)
		k++;
		return true;
	}
	
	public void setSample(int indexOfSample, Sample sample) {
		timeSlice[indexOfSample] = sample;
	}
	
	public Sample getSample(int indexOfSample) {
		return timeSlice[indexOfSample];
	}	
	
	public int getSampleCount() {
		return k+1;
	}
	
	public boolean hasSample(int indexOfSample) {
		return indexOfSample < timeSlice.length && timeSlice[indexOfSample] != null;
	}

	private class Itr implements Iterator<Sample> {
		
		private int curPosition = -1;	// posição actual do iterador
		private int nextPosition = -1;	// próxima posição calculada do iterador
		
		@Override
		public boolean hasNext() {
			return findNextPosition();
		}

		@Override
		public Sample next() {
			
			if(!findNextPosition()) {
				// não foi encontrada próxima posição
				throw new NoSuchElementException();	
			}
			
			// assumir próxima posição
			curPosition = nextPosition;
			
			// devolver posição actual
			return TimeSlice.this.timeSlice[curPosition];
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		private boolean findNextPosition() {
			
			if(this.nextPosition > this.curPosition) {
				// já existe uma próxima posição determinada
				return true;
			}
			
			this.nextPosition = -1;
			// procurar próxima posição não vazia a partir da posição actual
			for(int i = this.curPosition + 1; i < TimeSlice.this.k; i++) {
				if(TimeSlice.this.hasSample(i)) {
					this.nextPosition = i;	// guardar posição para a próxima chamada de next
					break;
				}
			}
			
			return this.nextPosition > -1;
		}
		
	}
	
	/**
	 * Devolve um iterador por todas as amostras que existem no timeslice. Amostras vazias não são 
	 * consideradas.
	 * @return	iterador
	 */
	@Override
	public Iterator<Sample> iterator() {
		return new Itr();
	}
	
	public String toString() {
		String string = "";
		for(Sample sample : this.timeSlice) {
			if(sample == null)
				string += "[null]\n";
			else
				string += sample.toString() + '\n';
		}
		
		return string;
	}

}
