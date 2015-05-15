package bayessian;

import score.Score;
import dataset.TransitionDataset;

public class DynamicBayessianNetwork<T> extends BayessianNetwork<RandomVariable>{
	
	int varCountInTimeT = 0;		// numero de variaveis num instante de tempo
	
	public DynamicBayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score score) {
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
}
