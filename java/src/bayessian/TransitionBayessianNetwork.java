package bayessian;

import score.Score;
import dataset.Dataset;
import dataset.Sample;
import dataset.TransitionDataset;

public class TransitionBayessianNetwork<T extends RandomVariable> extends BayessianNetwork<T>{
	
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
	
	boolean isFutureVar(int index) {
		return index >= varCountInTimeT;
	}
	
	private double calculateSingleProbability(int indexOfVar, int value, Sample sample) {
		//index = index of X[t+1] do qual queremos saber o valor mais provavel
		//value = valor que estamos a considerar nesse momento para a RVar que se pretende obter
		double probability = 0;
		double resultOfMultiplication = 0;
		int numberOfIterations = 0;
		int numberOfRvars = varCountInTimeT; //dividir por 2 porque metade do array sao variaveis do passado e metade do array sao variaveis do futuro
		int[] d = new int[numberOfRvars];
		
		for(int i = 0; i < numberOfRvars; i++) {
			if(i != indexOfVar) {
				numberOfIterations *= getRange(i);
			}
		}
		
		for(int n = 0; n < numberOfIterations-1; n++) {
			int j = InstanceCounting.getjOfProbability(indexOfVar, sample, getParents(indexOfVar), d, this);
			double thetaijk = estimates[indexOfVar].getEstimate(j, value);
			for(int l = 0; l < numberOfRvars; l++) {
				double thetaljdl = 1.0;
				if(l != indexOfVar) {
					int jlinha = InstanceCounting.getjLinhaOfProbability(indexOfVar, value, l, d);
					thetaljdl = estimates[l].getEstimate(jlinha, d[l]);
				}
				resultOfMultiplication *= thetaljdl;
			}
			probability += thetaijk * resultOfMultiplication;		
			d[0]++;
			for(int m = 0; m < numberOfRvars; m++) {
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
	
	private int calculateFutureValue(int indexOfVar, Sample sample) {
		
		double maxProbability = 0;
		double probability = 0;
		int futureValue = 0;
		
		for(int m = 0; m < getRange(indexOfVar); m++) {
			probability = calculateSingleProbability(indexOfVar, m, sample);
			if(probability > maxProbability) {
				maxProbability = probability;
				futureValue = m;
			}
		}
		return futureValue;
	}
	
	public int[] getFutureValues(int indexOfVar, Dataset dataset) {
		
		int[] futureValues = new int[dataset.size()];
		
		int i = 0;
		for(Sample item : dataset) {
			futureValues[i] = calculateFutureValue(indexOfVar, item);
			i++;
		}
		
		return futureValues;
	}
}