package dataset;

import java.util.ArrayList;
import java.util.List;

public class TransitionDataset extends Dataset {
	
	protected List<Sample> samplesOfTimeNextT = new ArrayList<>();

	/**
	 * Constructs the transition dataset based in an array 2 or more time slices 
	 * @param timeSlices	array of timeslices  
	 */
	public TransitionDataset(TimeSlice[] timeSlices) {
		
		if(timeSlices.length < 2) {
			// temos que criar uma excepção para isto
			throw new IllegalArgumentException("um TransitionDataset precisa de pelo menos 2 time slices");
		}
		
		//juntar restantes timeslices
		for(int i = 0; i < timeSlices.length - 1; i++) {
			for(int j = 0; j < timeSlices[i].getSampleCount(); j++) {
				//testar se ambos os time slices têm amostras definidas
				if(timeSlices[i].hasSample(j) && timeSlices[i+1].hasSample(j)) {
					samplesOfTimeT.add(timeSlices[i].getSample(j));
					samplesOfTimeNextT.add(timeSlices[i+1].getSample(j));
				}
			}
		}
	}
	
	@Override
	protected int getValue(int index, int sample) {
		int value = 0;
		
		if(index >= Sample.length()) {
			value = this.samplesOfTimeNextT.get(sample).getValue(index - Sample.length());
		} else {
			value = this.samplesOfTimeT.get(sample).getValue(index);
		}
		
		return value;
	}
	
	public String toString() {
		String string = "";
		for (int i = 0; i < samplesOfTimeT.size(); i++) {
			string += samplesOfTimeT.get(i).toString() + samplesOfTimeNextT.get(i).toString() + '\n';
		}
		return string;
	}
	
}
