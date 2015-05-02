package graphs;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

import components.DirectedEdge;
import components.DirectedGraphFactory;
import components.Edge;
import components.Node;

public class DirectedGraph extends Graph {
	
	public DirectedGraph() {
		nodesToEdges = new Hashtable<Node, Edge>();
		factory = new DirectedGraphFactory();
	}
	
	public List<Node> getParents(Node node) {
		//obter arestas do nó
		DirectedEdge edge = (DirectedEdge) nodesToEdges.get(node);
		
		//para cada aresta do nó obter nós que sejam a fonte da aresta e não sejam o próprio nó
		List<Node> parents = new ArrayList<Node>();
		if(node != edge.getSource()) {
			parents.add(node);
		}
		
		return parents;
	}
}
