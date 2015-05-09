package score;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public class MDLScore<T> extends LLScore<T> {
	MDLScore(BayessianNetwork<? extends RandomVariable> BN) {
		super(BN);
	}
	
	int getNetworkComplexity() { // PROVAVELMENTE ISTO DEVE SER UM MÉTODO DA BN
		
		int complexity = 0;
		
		for(int i: BN) { 
			complexity += (BN.getRange(i) - 1) * BN.getParentConfigurationCount(i);
		}
		
		return complexity;
	}
	
	int getScore() {
		int score = 0;
		
		//score = super.getScore() - log(N)*getNetworkComplexity()/2.0;
		
		return score;
	}
}
