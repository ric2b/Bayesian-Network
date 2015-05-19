package score;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;
import dataset.Dataset;

public interface Score {
	
	/**
	 * Computes the score of the given bayessian network based on the given dataset.
	 * The score function depends on the implementation.
	 * @param bayessian	bayessian network to compute the score
	 * @param dataset	dataset used to compute the score 
	 * @return	value of the score of teh bayessian network
	 */
	public double getScore(BayessianNetwork<? extends RandomVariable> bayessian, Dataset dataset);
}
