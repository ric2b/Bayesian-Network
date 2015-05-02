package components;

public class GraphFactory {
	
	public Edge createEdge(Node node1, Node node2) {
		return new Edge(node1, node2);
	}
}
