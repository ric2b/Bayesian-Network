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
	
	private double calculateSingleProbability(int indexOfVar, int value) {
		//index = index of X[t+1] do qual queremos saber o valor mais provavel
		//value = valor que estamos a considerar nesse momento para a RVar que se pretende obter
		double probability = 0;
		int numberOfIterations = 0;
		int numberOfRvars = varCountInTimeT; //dividir por 2 porque metade do array sao variaveis do passado e metade do array sao variaveis do futuro
		int[] d = new int[numberOfRvars - 1];
		
		for(int i = 0; i < numberOfRvars; i++) {
			if(i != indexOfVar) {
				numberOfIterations *= getRange(i);
			}
		}
		
		for(int n = 0; n < numberOfIterations-1; n++) {
			//calculos
			//int j = 
			estimates[indexOfVar].getEstimate(j, value);
			d[0]++;
			for(int m = 0; m < numberOfRvars-1; m++) {
				if(m == indexOfVar){
					continue;
				}
				if(d[m] == getRange(m)) {
					d[m+1]++;
					d[m] = 0;
				}
				else {
					break;
				}
			}
		}
		return probability;
	}
	
	public int calculateFutureValue(int indexOfVar) {
		
		double maxProbability = 0;
		double probability = 0;
		int futureValue = 0;
		
		for(int m = 0; m < getRange(indexOfVar); m++) {
			probability = calculateSingleProbability(indexOfVar, m);
			if(probability > maxProbability) {
				maxProbability = probability;
				futureValue = m;
			}
		}
		return futureValue;
	}
}