package graph.operation;

import graph.Graph;

public interface EdgeOperation<E extends Graph<T>, T> {
	
	public void exec(E graph, T t1, T t2);
	
}
