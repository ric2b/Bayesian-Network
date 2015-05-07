package dataset;

import bayessian.RandomVector;
import bayessian.Sample;

public class TimeSlice {
	
	Sample[] timeSlice = null;
	RandomVector vars = null;
	
	public TimeSlice(RandomVector vars, int numberOfSamples) {
		timeSlice = new Sample[numberOfSamples]; //criar array de Sample, ou seja, um array de array de inteiros
		// [[0,1,2],[1,3,4]] <-> [[samples of subject1],[samples of subject2]]
	}
	
	public void setSample(int indexOfSample, Sample sample) {
		timeSlice[indexOfSample] = sample;
	}
	
	public Sample getSample(int indexOfSample) {
		return timeSlice[indexOfSample];
	}	
}
