package graph.components;

public interface NodeFactory<T> {
	
	public Node<T> createNode(int index, T t);
}
