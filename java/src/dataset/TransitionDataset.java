package dataset;

import java.util.ArrayList;
import java.util.List;

public class TransitionDataset extends Dataset {
	
	protected List<Sample> samplesOfTimeNextT = new ArrayList<>();

	/**
	 * Contruir TransitionDataset a partir de um conjunto de 2 ou mais timeslices
	 * @param timeSlices	array de timeslice para contruir transition dataset
	 * @throws Exception	caso o array de time slices tenha menos 2 2 time slices 
	 */
	public TransitionDataset(TimeSlice[] timeSlices) throws Exception {
		
		if(timeSlices.length < 2) {
			// temos que criar uma excepção para isto
			throw new Exception("um TransitionDataset precisa de pelo menos 2 time slices");
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
			value = this.samplesOfTimeNextT.get(sample).getValue(index);
		} else {
			value = this.samplesOfTimeT.get(sample).getValue(index);
		}
		
		return value;
	}
	
}
