package score;

import dataset.Dataset;
import bayessian.BayessianNetwork;
import bayessian.RandomVariable;
import bayessian.InstanceCounting;

public class LLScore extends Score {
	LLScore(BayessianNetwork<? extends RandomVariable> BN) {
		super(BN);
	}
	
	@Override
	int getScore(Dataset dataset) {
		int score = 0;
				
		for(int i: BN) { // for each node of the Network
			int Q = BN.getParentConfigurationCount(i);
						
			for(int J = 0; J < Q; J++) {
				int N = BN.getRange(i);
				for(int k = 0; k < N; k++) {
					int Nij = InstanceCounting.getNij(i,J, BN, dataset);
					
					if(InstanceCounting.getNijk(i,J,k, BN, dataset) != 0){ // otherwise it's 0, no need to add 
						score += InstanceCounting.getNijk(i,J,k, BN, dataset) * Math.log((InstanceCounting.getNijk(i,J,k, BN, dataset)*1.0)/Nij);
					}					
				}
			}
		}
		
		return score;
	}

}
