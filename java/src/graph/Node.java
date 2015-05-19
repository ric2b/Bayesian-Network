package graph;

/*
 * Ter em conta que Node é só uma classe que existe dentro do grafo utilizada para representar os
 * diferentes nos do mesmo. Desta forma tem apenas visibilidade de package de forma que um cliente 
 * deste grafo nao possa criar objectos da classe Node nem precise de conhecer a sua implementação. 
 */

class Node<T> {
	
	protected int index = -1;
	protected T t = null;
	
	/**
	 * Consctructs a node with index and associates the generic object t to the node
	 * @param index	index of the node
	 * @param t		element to be associated with the node
	 */
	public Node(int index, T t) {
		this.index = index;
		this.t = t;
	}
	
	/**
	 * Returns the index of the node
	 * @return the index of the node
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns the element associated with the node
	 * @return element associated with the node
	 */
	public T get() {
		return t;
	}
	
	/**
	 * Sets the element associated with the node
	 */
	public void set(T t) {
		this.t = t;
	}

	@Override
	public String toString() {
		return index + ":" + t.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node<?> other = (Node<?>) obj;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}
}
