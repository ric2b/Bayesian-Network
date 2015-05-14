package graph.operation;

import graph.Graph;

public class AddOperation<E extends Graph<T>, T> implements EdgeOperation<E, T> {

	@Override
	public void exec(E graph, T t1, T t2) {
		graph.addEdge(t1, t2);
	}

}
