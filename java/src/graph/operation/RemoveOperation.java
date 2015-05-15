package graph.operation;

import graph.Graph;

public class RemoveOperation<E extends Graph<T>, T> extends EdgeOperation<E, T> {

	public RemoveOperation(T t1, T t2) {
		super(t1, t2);
	}

	@Override
	public void exec(E graph) {
		graph.removeEdge(t1, t2);
	}

}
