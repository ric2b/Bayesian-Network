package graph;

import java.util.Iterator;

public interface NavigableGraph<T> {
	
	/**
	 * Returns an iterator for all the parents of the node
	 * @param node
	 * @return iterador of the parents of the node
	 */
	public Iterator<T> parents(T t);
	
	/**
	 * Returns an iterator for all the children of the node
	 * @param node
	 * @return iterador of the children of the node
	 */
	public Iterator<T> children(T t);

}