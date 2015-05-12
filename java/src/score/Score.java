package score;

import dataset.Dataset;
import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public abstract class Score {
	BayessianNetwork<? extends RandomVariable> BN = null;	
	
	Score(BayessianNetwork<? extends RandomVariable> BN) {
		this.BN = BN;
	}
	
	abstract int getScore(Dataset dataset);
}
