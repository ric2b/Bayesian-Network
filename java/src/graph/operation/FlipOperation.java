package graph.operation;

import graph.DirectedAcyclicGraph;

public class FlipOperation<E extends DirectedAcyclicGraph<T>, T> implements EdgeOperation<E, T> {

	@Override
	public void exec(E graph, T srcNode, T destNode) {
		graph.flipEdge(srcNode, destNode);
	}

}
