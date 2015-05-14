package graph.operation;

import graph.DirectedAcyclicGraph;

public class FlipOperation<E extends DirectedAcyclicGraph<T>, T> extends EdgeOperation<E, T> {

	public FlipOperation(T srcNode, T destNode) {
		super(srcNode, destNode);
	}

	@Override
	public void exec(E graph) {
		graph.flipEdge(t1, t2);
	}

}
