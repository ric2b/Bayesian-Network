package graph;

/*
 * Ter em conta que Node é só uma classe que existe dentro do grafo utilizada para representar os
 * diferentes nos do mesmo. Desta forma tem apenas visibilidade de package de forma que um cliente 
 * deste grafo nao possa criar objectos da classe Node nem precise de conhecer a sua implementação. 
 */

class Node<T> {
	
	protected int index = -1;
	protected T t = null;
	
	public Node(int index, T t) {
		this.index = index;
		this.t = t;
	}
	
	public int getIndex() {
		return index;
	}
	
	public T get() {
		return t;
	}
	
	public void set(T t) {
		this.t = t;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + index;
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		
//		/*
//		 * Nota: o operador 'instanceof' retorna false caso o primeiro operando seja null
//		 */
//		
//		if(!(obj instanceof Node)) {
//			return false;
//		}
//		
//		//considerar apenas o indice
//		Node<?> other = (Node<?>) obj;
//		if (index != other.index)
//			return false;
//		
//					
//		return true;
//	}
	
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
