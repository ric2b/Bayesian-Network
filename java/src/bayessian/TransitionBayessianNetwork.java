package bayessian;

import score.Score;
import dataset.TransitionDataset;

public class TransitionBayessianNetwork<T> extends BayessianNetwork<RandomVariable>{
	
	int varCountInTimeT = 0; // numero de variaveis num instante de tempo
	
	public TransitionBayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score score) {
		super(vars, dataset, score);
		this.varCountInTimeT = vars.length / 2;
	}
	
	@Override
	protected boolean addAssociation(int srcIndex, int destIndex) {
		
		if(srcIndex >= varCountInTimeT && destIndex < varCountInTimeT) {
			// tentiva de ligar duas variaveis de t+1 para t
			return false;
		}
		
		return graph.addEdge(vars[srcIndex], vars[destIndex]);
	}
	
	public int calculateProbability(int indexOfVar, int value) {
		//index = index of X[t+1] do qual queremos saber o valor mais provavel
		//value = valor que estamos a considerar nesse momento para a RVar que se pretende obter
		int probability = 0;
		int numberOfIterations = 0;
		int numberOfRvars = vars.length;
		int[] d = new int[numberOfRvars - 1];
		
		for(int i = 0; i < numberOfRvars; i++) {
			if(i != indexOfVar) {
				numberOfIterations *= getRange(i);
			}
		}
		
		for(int i = 0; i < numberOfIterations-1; i++) {
			//calculos
			d[0]++;
			for(int j = 0; j < numberOfRvars-1; j++) {
				if(j != indexOfVar) {
					if(d[j] == getRange(j)) {
						d[j+1]++;
						d[j] = 0;
					}
					else {
						break;
					}
				}
				else {
					continue;
				}
			}
		}
		return probability;
	}
}
