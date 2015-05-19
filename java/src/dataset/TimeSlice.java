package dataset;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TimeSlice implements Iterable<Sample> {
	
	private Sample[] timeSlice = null;
	private int k = -1;
	
	/**
	 * Constructs a time slice with @numberOfSamples maximum number of samples.
	 * @param numberOfSamples	maximum number of samples of the time slice
	 */
	public TimeSlice(int numberOfSamples) {
		k = 0; 
		timeSlice = new Sample[numberOfSamples]; //criar array de Sample, ou seja, um array de array de inteiros
		// [[0,1,2],[1,3,4]] <-> [[samples of subject1],[samples of subject2]]
	}
	
	/**
	 * Adds a sample to the time slice. If sample is null it is added an empry sample.
	 * @param sample sample to be added
	 * @return tru if the smaple was added 
	 */
	public boolean addSample(Sample sample) {
		if(k == timeSlice.length) {
			return false;
		}
		timeSlice[k] = sample;
		k++;
		return true;
	}
	
	/**
	 * Returns the sample at the given position.
	 * @param indexOfSample position of the sample
	 * @return	sample at the given position
	 */
	public Sample getSample(int indexOfSample) {
		return timeSlice[indexOfSample];
	}	
	
	/**
	 * Returns the number of samples in the time slice.
	 * @return number of samples in the time slice
	 */
	public int getSampleCount() {
		return k+1;
	}
	
	/**
	 * Tests if there is a sample at the given position or is an empty sample. 
	 * @param indexOfSample	position
	 * @return	true if there is a smaple at the given position
	 */
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
