package score;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;
import bayessian.InstanceCounting;

public class LLScore<T> extends Score<T> {
	LLScore(BayessianNetwork<? extends RandomVariable> BN) {
		super(BN);
	}
	
	int getScore() {
		int score = 0;
				
		for(int i: BN) { // for each node of the Network
			int[] parentRanges = BN.getParentRanges(i); // get the ranges of the parents
			
			int Q = 1;
			for(int ri: parentRanges)
				Q *= ri; // get the number of different possible configurations for the parents
			
			for(int J = 0; J < Q; J++) {
				int[] parentValues = InstanceCounting.mapJToj(parentRanges, J);
				int N = BN.getRange(i);
				for(int k; k < N; k++) {
					int Nij = BN.getNij(i,J);
					score += BN.getNijk(i,J,k) * log (BN.getNijk(i,J,k)/Nij);
				}
			}
		}
		return score;
	}
}
