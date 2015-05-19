package bayessian;

import graph.Graph;
import graph.operation.EdgeOperation;

public class MaximumCriterion implements StopCriterion {
	
	/**
	 * Returns true when the maximum local stopping criterions is met
	 */
	@Override
	public boolean toStop(BayessianNetwork<? extends RandomVariable> BN, double bestScore,
			EdgeOperation<? extends Graph<? extends RandomVariable>, ? extends RandomVariable> operation) {
		return operation == null;
	}

}
