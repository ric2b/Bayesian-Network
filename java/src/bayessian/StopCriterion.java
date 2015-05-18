package bayessian;

import graph.Graph;
import graph.operation.EdgeOperation;

public interface StopCriterion {
	
	public boolean toStop(BayessianNetwork<? extends RandomVariable> BN, double bestScore,
			EdgeOperation<? extends Graph<? extends RandomVariable>, ? extends RandomVariable> operation);
}
