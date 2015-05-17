package bayessian;

import score.Score;
import dataset.Dataset;
import dataset.Sample;
import dataset.TransitionDataset;

public class TransitionBayessianNetwork<T extends RandomVariable> extends BayessianNetwork<T>{
	
	public TransitionBayessianNetwork(RandomVariable[] vars, Dataset dataset) {
		super(vars, dataset);
	}
	
	public TransitionBayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score score) {
		super(vars, dataset, score, vars.length / 2);
	}
	
	@Override
	protected boolean addAssociation(int srcIndex, int destIndex) {
		
		if((srcIndex >= varCount && destIndex < varCount) ||		// tentiva de ligar duas variaveis de t+1 para t
				(srcIndex < varCount && destIndex < varCount) ||	// ou tentativa de ligar duas vars de instante t 
				graph.getParents(vars[destIndex]).size() == BayessianNetwork.parentCount) {	// variavel ja tem 3 pais
			return false;
		}
		
		return graph.addEdge(vars[srcIndex], vars[destIndex]);
	}
	
	@Override
	protected boolean flipAssociation(int srcIndex, int destIndex) {
		
		if((destIndex >= varCount && srcIndex < varCount) ||		// tentiva de ligar duas variaveis de t+1 para t
				(srcIndex < varCount && destIndex < varCount) ||		// tentativa de ligar duas vars de instante t
				graph.getParents(vars[srcIndex]).size() == BayessianNetwork.parentCount) {	// variavel ja tem 3 pais
			return false;
		}
		
		return graph.flipEdge(vars[srcIndex], vars[destIndex]);
	}
	
	boolean isFutureVar(int index) {
		return index >= varCount;
	}
	
	private double calculateSingleProbability(int indexOfVar, int value, Sample sample) {
		//
		//value = valor que estamos a considerar nesse momento para a RVar que se pretende obter
		double probability = 0;
		double resultOfMultiplication = 1.0;
		int numberOfIterations = 1;
		int numberOfRVars = varCount; //dividir por 2 porque metade do array sao variaveis do passado e metade do array sao variaveis do futuro
		int[] d = new int[numberOfRVars];
		
		for(int i = 0; i < numberOfRVars; i++) {
			if(i != indexOfVar) {
				numberOfIterations *= getRange(i);
			}
		}
		
		for(int n = 0; n < numberOfIterations; n++) {
			int j = InstanceCounting.getjOfProbability(indexOfVar, sample, getParents(indexOfVar), d, this);
			if(j != 0) {
				int a = 1;
			}
			double thetaijk = estimates[indexOfVar].getEstimate(j, value);
			for(int l = 0; l < numberOfRVars; l++) {
				double thetaljdl = 1.0;
				if(l != indexOfVar) {
					int jlinha = InstanceCounting.getjLinhaOfProbability(indexOfVar, value, l, sample, getParents(l+varCount), d, this);
					if(jlinha != 0) {
						int a = 1;
					}
					thetaljdl = estimates[l].getEstimate(jlinha, d[l]);
				}
				resultOfMultiplication *= thetaljdl;
			}
			probability += thetaijk * resultOfMultiplication;		
			d[numberOfRVars-1]++;
			for(int m = numberOfRVars-2; m >= 0; m--) {
				if(m == indexOfVar){
					continue;
				}
				if(d[m+1] == getRange(m+1)) {
					d[m]++;
					d[m+1] = 0;
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
	
//	=== Inter-slice connectivity
//	name of node 1 at time-slice t + 1 : name of the parents of node 1 at time-slice t
//	name of node 2 at time-slice t + 1 : name of the parents of node 2 at time-slice t
//	. . . . . .
//	name of node n at time-slice t + 1 : name of the parents of node n at time-slice t
//	=== Intra-slice connectivity
//	name of node 1 at time-slice t + 1 : name of the parents of node 1 at time-slice t + 1
//	name of node 2 at time-slice t + 1 : name of the parents of node 2 at time-slice t + 1
//	. . . . . .
//	name of node n at time-slice t + 1 : name of the parents of node n at time-slice t + 1
	
	public String toString() {
		String string = "";
		
		string += "=== Inter-slice connectivity\n";
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
			string += '\n';
		}
		
		return string;
	}
	
}