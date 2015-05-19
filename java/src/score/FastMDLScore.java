package score;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;
import dataset.Dataset;

public class FastMDLScore extends FastLLScore {
	
	int getNetworkComplexity(BayessianNetwork<? extends RandomVariable> bayessian) {
		int complexity = 0;
		
		for(int i: bayessian) { 
			complexity += (bayessian.getRange(i) - 1) * bayessian.getParentConfigurationCount(i);
		}
		return complexity;
	}	
	
	@Override
	public double getScore(BayessianNetwork<? extends RandomVariable> bayessian, Dataset dataset) {
		double score = 0;
		score = super.getScore(bayessian, dataset) -  Math.log(dataset.size())*getNetworkComplexity(bayessian)/(Math.log(2)*2.0);
		return score;
	}
}
