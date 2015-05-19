package bayessian;

import graph.Graph;
import graph.operation.EdgeOperation;

public interface StopCriterion {
	
	/**
	 * Returns true when the specified stopping criterion is met.
	 * @param BN		bayessian network to test the criterion on
	 * @param bestScore current best score of the bayessian network
	 * @param operation operation executed on the graph with best best score
	 * @return true when the specified stopping criterion is met.
	 */
	public boolean toStop(BayessianNetwork<? extends RandomVariable> BN, double bestScore,
			EdgeOperation<? extends Graph<? extends RandomVariable>, ? extends RandomVariable> operation);
}
