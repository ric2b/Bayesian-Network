package score;

import dataset.Dataset;
import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public class MDLScore extends LLScore {
	
	@Override
	public int getScore(BayessianNetwork<? extends RandomVariable> bayessian, Dataset dataset) {
		int score = 0;
		
		//score = super.getScore() - log(N)*getNetworkComplexity()/2.0;
		
		return score;
	}
}
