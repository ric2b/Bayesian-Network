package graph.operation;

import graph.Graph;

public abstract class EdgeOperation<E extends Graph<T>, T> {
	
	protected T t1 = null;	// node1 of the edge
	protected T t2 = null;	// node2 of the edge
	
	/**
	 * Defines the two nodes of the graph where the edge opertion is performed on
	 * @param t1	node 1
	 * @param t2	node 2
	 */
	public EdgeOperation(T t1, T t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	/**
	 * Executes the edge operation specified.
	 * @param graph	graph where the operation is performed
	 */
	public abstract void exec(E graph);
	
}
