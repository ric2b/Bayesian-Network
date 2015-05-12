package dataset;


public class TimeSlice {
	
	Sample[] timeSlice = null;
	int k = -1;
	
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
}
