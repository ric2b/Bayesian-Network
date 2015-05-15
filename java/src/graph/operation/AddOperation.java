package graph.operation;

import graph.Graph;

public class AddOperation<E extends Graph<T>, T> extends EdgeOperation<E, T> {

	public AddOperation(T t1, T t2) {
		super(t1, t2);
	}

	@Override
	public void exec(E graph) {
		graph.addEdge(t1, t2);
	}

}
