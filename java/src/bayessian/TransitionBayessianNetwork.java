package bayessian;

import score.Score;
import dataset.Dataset;
import dataset.Sample;
import dataset.TransitionDataset;

public class TransitionBayessianNetwork<T extends RandomVariable> extends BayessianNetwork<T>{
	
	public TransitionBayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score score, StopCriterion criterion) {
		super(vars, dataset, score, vars.length / 2, criterion);
	}
	
	/**
	 * Doesn't associate variables form the future to the present and from the present to the present.
	 */
	@Override
	protected boolean addAssociation(int srcIndex, int destIndex) {
		
		if((srcIndex >= varCount && destIndex < varCount) ||		// tentiva de ligar duas variaveis de t+1 para t
				(srcIndex < varCount && destIndex < varCount) ||	// ou tentativa de ligar duas vars de instante t 
				graph.getParents(vars[destIndex]).size() == BayessianNetwork.parentCount) {	// variavel ja tem 3 pais
			return false;
		}
		
		return graph.addEdge(vars[srcIndex], vars[destIndex]);
	}
	
	/**
	 * Doesn't associate variables form the future to the present and from the present to the present.
	 */
	@Override
	protected boolean flipAssociation(int srcIndex, int destIndex) {
		
		if((destIndex >= varCount && srcIndex < varCount) ||		// tentiva de ligar duas variaveis de t+1 para t
				(srcIndex < varCount && destIndex < varCount) ||		// tentativa de ligar duas vars de instante t
				graph.getParents(vars[srcIndex]).size() == BayessianNetwork.parentCount) {	// variavel ja tem 3 pais
			return false;
		}
		
		return graph.flipEdge(vars[srcIndex], vars[destIndex]);
	}
	
	/**
	 * Tests if a variable with the given position is from the future
	 * @param index	position of the random variable
	 * @return
	 */
	boolean isFutureVar(int index) {
		return index >= varCount;
	}
	
	/**
	 * Returns the number of random variables of one time instant
	 * @return  number of random variables of one time instant
	 */
	public int getVarCount () {
		return varCount;
	}
	
	/**
	 * Computes the probability of a single random variable to have the value specified
	 * @param indexOfVar	index of the random variable to compute the probability
	 * @param value			value of the random variable to compute the probability
	 * @param sample
	 * @return
	 */
	private double calculateSingleProbability(int indexOfVar, int value, Sample sample) {
		//
		//value = valor que estamos a considerar nesse momento para a RVar que se pretende obter
		double probability = 0;
		int numberOfIterations = 1;
		int numberOfRVars = varCount; //dividir por 2 porque metade do array sao variaveis do passado e metade do array sao variaveis do futuro
		int[] d = new int[numberOfRVars];
		
		for(int i = 0; i < numberOfRVars; i++) {
			if(i != indexOfVar) {
				numberOfIterations *= getRange(i);
			}
		}
		
		for(int n = 0; n < numberOfIterations; n++) {
			int j = InstanceCounting.getjOfProbability(indexOfVar, sample, getParents(indexOfVar + varCount), d, this);
			double thetaijk = estimates[indexOfVar + varCount].getEstimate(j, value);
			double resultOfMultiplication = 1.0;
			for(int l = 0; l < numberOfRVars; l++) {
				double thetaljdl = 1.0;
				if(l != indexOfVar) {
					int jlinha = InstanceCounting.getjLinhaOfProbability(indexOfVar, value, l, sample, getParents(l + varCount), d, this);
					thetaljdl = estimates[l + varCount].getEstimate(jlinha, d[l]);
				}
				resultOfMultiplication *= thetaljdl;
			}	
			probability += thetaijk * resultOfMultiplication;	
			//System.out.println("probababilidade parcial: " + (thetaijk * resultOfMultiplication));
			//System.out.println("probabilidade: " + probability);
			d[numberOfRVars-1]++;
			for(int m = numberOfRVars-1; m > 0; m--) {
				if(m == indexOfVar){
					continue;
				}
				if(d[m] == getRange(m)) {
					
					if((m - 1) == indexOfVar && (m - 2) >= 0) {
						d[m - 2]++;
					} else {
						d[m - 1]++;
					}				
					d[m] = 0;
				}
				else {
					break;
				}
			}
		}
		
		//System.out.println("probabilidades :" +  probability);
		
		return probability;
	}
	
	/**
	 * Computes the future value of the random variable with the given position
	 * @param indexOfVar	position
	 * @param sample		sample
	 * @return	future value of the random variable
	 */
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
	
	/**
	 * Returns an array with all the future values of the given random variable
	 * @param indexOfVar	random variable to compute the furture values
	 * @param dataset
	 * @return array with all the future values of the given random variable
	 */
	public int[] getFutureValues(int indexOfVar, Dataset dataset) {
		//indexOfVar = index of X[t+1] do qual queremos saber o valor mais provavel
		
		if(indexOfVar <= varCount || indexOfVar > vars.length) {
			throw new IllegalArgumentException("o numero da variavel a calcular n�o est� correcto");
		}
		
		int[] futureValues = new int[dataset.size()];
		
		int i = 0;
		for(Sample item : dataset) {
			//subtrair numero de RVars para mapear o index de X[t+1] para X[t]
			futureValues[i] = calculateFutureValue(indexOfVar-varCount-1, item); 
			i++;
		}
		
		return futureValues;
	}
	
	public String toString() {
		String string = "=== Inter-slice connectivity\n";
		
		for(int i = varCount; i < vars.length; i++) {
			string += vars[i].toString() + " : ";
			for(RandomVariable var: graph.getParents(vars[i])) {
				if(var.getTimeInstant() == 0)
					string += var + " ";
			}
			string += '\n';
		}
		
		string += "=== Intra-slice connectivity\n";
		for(int i = varCount; i < vars.length; i++) {
			string += vars[i].toString() + " : ";
			for(RandomVariable var: graph.getParents(vars[i])) {
				if(var.getTimeInstant() == 1)
					string += var + " ";
			}
			
			if(i != vars.length - 1) {
				string += '\n';
			}	
		}
		
		return string;
	}
	
}