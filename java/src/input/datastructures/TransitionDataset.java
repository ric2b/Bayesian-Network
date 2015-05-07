package input.datastructures;

import java.util.ArrayList;
import java.util.List;

public class TransitionDataset {
	
	private List<int[]> samplesOfTimeT = new ArrayList<>();
	private List<int[]> samplesOfTimeNextT = new ArrayList<>();
	
	public TransitionDataset(TimeSlice... timeSlices) {
		
		for(int i = 0; i < timeSlices.length - 1; i++) {
			for(int j = 0; j < timeSlices[i].size(); j++) {
				//testar se ambos os time slices têm amostras definidas
				if(timeSlices[i].hasSample(j) && timeSlices[i+1].hasSample(j)) {
					samplesOfTimeT.add(timeSlices[i].getSample(j));
					samplesOfTimeNextT.add(timeSlices[i+1].getSample(j));
				}
			}
		}
	}
}
