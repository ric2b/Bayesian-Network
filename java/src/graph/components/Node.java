package graph.components;

public class Node<T> {
	
	protected int index = -1;
	protected T t = null;
	
	protected Node(int index, T t) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		/*
		 * Nota: o operador 'instanceof' retorna false caso o primeiro operando seja null
		 */
		
		if(!(obj instanceof Node)) {
			return false;
		}
		
		//considerar apenas o indice
		Node<?> other = (Node<?>) obj;
		if (index != other.index)
			return false;
					
		return true;
	}
}
