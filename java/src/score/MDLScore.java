package score;

import dataset.Dataset;
import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public class MDLScore extends LLScore {
	MDLScore(BayessianNetwork<? extends RandomVariable> BN) {
		super(BN);
	}
	
	int getNetworkComplexity() { // PROVAVELMENTE ISTO DEVE SER UM M�TODO DA BN
		
		int complexity = 0;
		
		for(int i: BN) { 
			complexity += (BN.getRange(i) - 1) * BN.getParentConfigurationCount(i);
		}
		
		return complexity;
	}
	
	@Override
	public int getScore(Dataset dataset) {
		int score = 0;
		
		//score = super.getScore() - log(N)*getNetworkComplexity()/2.0;
		
		return score;
	}
}
