package components;

public class DirectedAcyclicGraphFactory extends DirectedGraphFactory {
	
	@Override
	public Edge createEdge(Node srcNode, Node destNode) {
		
		Edge newEdge = null;
		try {
			newEdge = new DirectedAcyclicEdge(srcNode, destNode);
		} catch(Exception e) {
			//aresta formou um ciclo
			//retornar null
		}
		
		return newEdge;
	}
}
