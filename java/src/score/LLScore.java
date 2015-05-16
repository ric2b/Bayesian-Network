package score;

import dataset.Dataset;
import bayessian.BayessianNetwork;
import bayessian.RandomVariable;
import bayessian.InstanceCounting;

public class LLScore implements Score {

	@Override
	public double getScore(BayessianNetwork<? extends RandomVariable> bayessian, Dataset dataset) {
		
		double score = 0;		
		for(int i: bayessian) { // for each node of the Network
			int Q = bayessian.getParentConfigurationCount(i);
						
			for(int J = 0; J < Q; J++) {
				int N = bayessian.getRange(i);
				for(int k = 0; k < N; k++) {
					int Nij = InstanceCounting.getNij(i,J, bayessian, dataset);
					
					if(InstanceCounting.getNijk(i,J,k, bayessian, dataset) != 0){ // otherwise it's 0, no need to add 
						score += InstanceCounting.getNijk(i,J,k, bayessian, dataset) * Math.log((InstanceCounting.getNijk(i,J,k, bayessian, dataset)*1.0)/Nij);
					}					
				}
			}
		}
		
		if(score == 0) {
			return Double.NEGATIVE_INFINITY;
		}
		
		return score;
	}

}
