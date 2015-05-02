package components;

public class Edge {
	
	protected Node node1 = null;
	protected Node node2 = null;
	
	Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Node getNode1() {
		return node1;
	}
	
	public Node getNode2() {
		return node2;
	}
	
	public Node getOpposite(Node node) {
		Node oppositeNode = null;
		if(node == node1)
			oppositeNode = node2;
		else if(node == node2)
			oppositeNode = node1;
		return oppositeNode;
	}
	
	public String toString() {
		return "edge[" + node1 + "-" + node2 +"]";
	}
}
