package components;

public class DirectedEdge extends Edge {
	
	/*
	 * Nota: considera-se o nó 1 a fonte e o nó 2 o destino da aresta direccionada
	 */
	
	DirectedEdge(Node srcNode, Node destNode) {
		super(srcNode, destNode);
	}
	
	public Node getSource() {
		return node1;
	}
	
	public Node getDest() {
		return node2;
	}
	
	public boolean isSource(Node node) {
		return node == node1;
	}
	
	public boolean isDest(Node node) {
		return node == node2;
	}
	
	public String getString() {
		return "edge[" + node1 + "->" + node2 +"]";
	}
}
