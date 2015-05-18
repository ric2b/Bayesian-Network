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
				
				// calcular Nijks
				int[] Nijks = new int[N];
				int Nij = 0;
				for(int k = 0; k < N; k++) {
					Nij += Nijks[k] = InstanceCounting.getNijk(i,J,k, bayessian, dataset);						
				}
				
				// calcular score
				if(Nij != 0) {
					for(int k = 0; k < N; k++) {
						if(Nijks[k] != 0)
							score += Nijks[k] * Math.log((Nijks[k]*1.0)/Nij) / Math.log(2);
					}
				}
			}
		}
		
		return score;
	}

}
