package graph.operation;

import graph.Graph;

public abstract class EdgeOperation<E extends Graph<T>, T> {
	
	protected T t1 = null;	// node1 of the edge
	protected T t2 = null;	// node2 of the edge
	
	public EdgeOperation(T t1, T t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public abstract void exec(E graph);
	
}
