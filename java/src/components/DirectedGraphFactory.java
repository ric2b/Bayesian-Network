package components;

public class DirectedGraphFactory extends GraphFactory {
	
	@Override
	public Edge createEdge(Node srcNode, Node destNode) {
		return new DirectedEdge(srcNode, destNode);
	}
}
