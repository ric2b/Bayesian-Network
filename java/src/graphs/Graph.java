package graphs;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import components.Edge;
import components.GraphFactory;
import components.Node;

public class Graph {
	
	/*
	 * Nota:	Para já está a ser usada uma hastable mas isto não funciona porque ela só permite ter um valor por cada
	 * 		chave. No futuro podemos implementar uma estrutura de dados para usar aqui.
	 */
	protected Map<Node, Edge> nodesToEdges = null;		//mapeamento entre os nos e as arestas
	protected GraphFactory factory = null;
	
	public Graph() {
		nodesToEdges = new Hashtable<Node, Edge>();
		factory = new GraphFactory();
	}
	
	public Graph(Collection<Node> nodes) {
		//criar hashtable com tamanho correspondente ao numero de nos
		nodesToEdges = new Hashtable<Node, Edge>(nodes.size());
		
		//preencher tabela com todos os nós sem arestas
		for(Node node : nodes) {
			nodesToEdges.put(node, null);
		}
	}
	
	public void addNode(Node node) {
		nodesToEdges.put(node, null);
	}
	
	public void addEdge(Node node1, Node node2) {
		//colocar a mesma aresta nos dois nós
		Edge edge = factory.createEdge(node1, node2);
		nodesToEdges.put(node1, edge);
		nodesToEdges.put(node2, edge);
	}
	
	public Set<Node> getNodes() {
		return nodesToEdges.keySet();
	}
	
	public Set<Edge> getEdges() {
		return (Set<Edge>) nodesToEdges.values();
	}
	
	public void removeNode(Node node) {
		//remover todas as arestas que ligam ao nó
		removeEdge(nodesToEdges.get(node));
		
		//remover nó do mapa
		nodesToEdges.remove(node);
	}
	
	protected void removeEdge(Edge edge) {
		//remover aresta dos dois nós
		nodesToEdges.put(edge.getNode1(), null);
		nodesToEdges.put(edge.getNode2(), null);
	}
	
	public void removeEdge(Node node1, Node node2) {
		nodesToEdges.put(node1, null);
		nodesToEdges.put(node2, null);
	}
	
	public int getNodeCount() {
		return nodesToEdges.size();
	}
	
	public int getEdgeCount() {
		/*
		 * Nota:	isto é ineficiente mas para já é a melhor forma de garantir compatibilidade com qualquer mapa.
		 * 		quando tivermos definido quala estrutura de dados usada para representar os nós e as arestas podemos
		 * 		melhorar esta parte.
		 */
		return nodesToEdges.values().size();
	}
	
	public String toString() {
		return nodesToEdges.toString();
	}
}
