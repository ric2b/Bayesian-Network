package dataset;

import java.util.ArrayList;
import java.util.List;

public class TransitionDataset {
	
	private List<Sample> samplesOfTimeT = new ArrayList<>();
	private List<Sample> samplesOfTimeNextT = new ArrayList<>();
	
	/**
	 * Contruir TransitionDataset a partir de um conjunto de 2 ou mais timeslices
	 * @param timeSlice0 timeslice do instante 0
	 * @param timeSlice1 timeslice do instante 1
	 * @param timeSlices timeslices para instantes superiores a 1
	 */
	public TransitionDataset(TimeSlice timeSlice0, TimeSlice timeSlice1, TimeSlice... timeSlices) {
		
		//juntar samples da time slice 0 e timeslice 1
		for(int j = 0; j < timeSlice0.getSampleCount(); j++) {
			//testar se ambos os time slices têm amostras definidas
			if(timeSlice0.hasSample(j) && timeSlice1.hasSample(j)) {
				samplesOfTimeT.add(timeSlice0.getSample(j));
				samplesOfTimeNextT.add(timeSlice1.getSample(j));
			}
		}
		
		if(timeSlices.length > 0) {
			//foram recebidas mais do que 2 timeslices
			
			//juntar samples da timeslice 1 e timeslice 2
			for(int j = 0; j < timeSlice1.getSampleCount(); j++) {
				//testar se ambos os time slices têm amostras definidas
				if(timeSlice1.hasSample(j) && timeSlices[0].hasSample(j)) {
					samplesOfTimeT.add(timeSlice1.getSample(j));
					samplesOfTimeNextT.add(timeSlices[0].getSample(j));
				}
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
		
	}
	
	public int countValues(int varI, int value, int[] parents, int[] parentValues) {
		int count = 0;
		// o numero de amostras em cada conjunto de amostras é igual
		int sampleCount = samplesOfTimeT.size();
		
		for(int i = 0; i < sampleCount; i++) {
			boolean toCount = true;	// indica se esta linha de amostras é para contar como uma combinação
			for(int j = 0; j < parents.length; j++) {
				if(parentValues[j] != getValue(parents[j], i)) {
					// esta linha não contem a configuração dos pais correcta
					toCount = false;
					break;
				}
			}
			
			if(toCount && value == getValue(varI, i)) {
				// linha cumpriu a configuração dos pais e o valor da variavel
				count++;
			}
		}
		
		return count;
	}
	
	private int getValue(int var, int sample) {
		int value = 0;
		
		if(var >= Sample.length()) {
			value = this.samplesOfTimeNextT.get(sample).getValue(var);
		} else {
			value = this.samplesOfTimeT.get(sample).getValue(var);
		}
		
		return value;
	}
	
}
