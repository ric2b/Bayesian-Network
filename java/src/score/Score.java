package score;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public abstract class Score<T> {
	BayessianNetwork<? extends RandomVariable> BN;	
	
	Score(BayessianNetwork<? extends RandomVariable> BN) {
		this.BN = BN;
	}
	
	abstract int getScore();
}
