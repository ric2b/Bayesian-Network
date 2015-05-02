package components;

public class DirectedAcyclicEdge extends DirectedEdge {
	
	DirectedAcyclicEdge(Node srcNode, Node destNode) throws Exception {
		super(srcNode, destNode);
		
		if(srcNode == destNode) {
			throw new Exception("aresta forma um ciclo");
		}
	}
}
