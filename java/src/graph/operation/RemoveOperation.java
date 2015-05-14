package graph.operation;

import graph.Graph;

public class RemoveOperation<E extends Graph<T>, T> implements EdgeOperation<E, T> {

	@Override
	public void exec(E graph, T t1, T t2) {
		graph.removeEdge(t1, t2);
	}

}
