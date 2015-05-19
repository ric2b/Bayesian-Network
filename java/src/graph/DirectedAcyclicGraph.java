package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

public class DirectedAcyclicGraph<T> implements Graph<T>, NavigableGraph<T>, Cloneable {
	
	protected Map<Node<T>, Collection<Node<T>>> edgeMap = new HashMap<>();;		// mapa que representa as arestas
	protected Map<T, Node<T>> nodeMap = new HashMap<>();								// permite obter o nó para um dado T
	protected int nodeCount = 0;																// numero de nós no grafo
	protected int edgeCount = 0;
	
	/**
	 * Creates and empty graphs
	 */
	public DirectedAcyclicGraph() {
		; // grafo vazio
	}
	
	/**
	 * Constructs a graph initialized with the nodes in the collection ts and zero edges.
	 * @param ts	collections of nodes to add to the graph 
	 */
	public DirectedAcyclicGraph(Collection<? extends T> ts) {
		//criar nós a partir da collection
		for(T t : ts) {
			this.addNode(t);
		}
	}
	
	/**
	 * Constructs a graph initialized with the nodes in the array ts and zero edges.
	 * @param ts	array of nodes to add to the graph
	 */
	public DirectedAcyclicGraph(T[] ts) {
		//criar nós a partir da collection
		for(T t : ts) {
			this.addNode(t);
		}
	}
	
	@Override
	public void addNode(T t) {
		if(t == null) {
			throw new NullPointerException();
		}
		
		//usar como indice o numero de nós total para garantir que não é repetido
		Node<T> node = new Node<T>(nodeCount, t);
		
		//colocar nó no mapa de aresta com nenhuma aresta definida
		edgeMap.put(node, new HashSet<Node<T>>()); 
		
		//colocar nó nos mapas de indice e de T
		nodeMap.put(t, node);
		
		nodeCount++;
	}
	
	@Override
	public boolean addEdge(T src, T dest) throws NullPointerException, NoSuchElementException {
		if(doesItCreateCycle(this.getNode(src),this.getNode(dest))){
			return false;
		}
		
		edgeCount++;
		return edgeMap.get(getNode(dest)).add(getNode(src));
	}
	
	@Override
	public int addEdge(List<? extends T> srcNodes, List<? extends T> destNodes) {
		
		int nodeCount = srcNodes.size() <= destNodes.size() ? srcNodes.size() : destNodes.size();
		for (int i = 0; i < nodeCount; i++) {
			//adicionar uma aresta por cada par
			addEdge(srcNodes.get(i), destNodes.get(i));
		}
		
		return 0;
	}
	
	
	@Override
	public void removeNode(T t) throws NullPointerException, NoSuchElementException {
		Node<T> node = getNode(t);
		
		edgeCount -= edgeMap.get(getNode(t)).size();
		
		//remover nó de todos os mapas
		edgeMap.remove(node);
		nodeMap.remove(node.get());
		
		nodeCount--;
	}
	
	@Override
	public boolean removeEdge(T src, T dest) throws NullPointerException, NoSuchElementException {
		//remover src do conjunto de pais do dest
		Collection<Node<T>> parents = edgeMap.get(getNode(dest));
		edgeCount--;
		
		return parents.remove(getNode(src));
	}	
	
	@Override
	public void removeAllEdges() {
		Set<Map.Entry<Node<T>, Collection<Node<T>>>> entries = edgeMap.entrySet();
		
		for(Map.Entry<Node<T>, Collection<Node<T>>> entry : entries) {
			entry.getValue().clear();
		}
		
		edgeCount = 0;
	}
	
	@Override
	public void removeAllEdges(T t) throws NullPointerException, NoSuchElementException {
		Collection<Node<T>> parents = edgeMap.get(getNode(t));
		edgeCount -= parents.size();
		parents.clear();
	}
	
	/**
	 * Inverts the direction of the edge with the source node src an destination node dest
	 * @param src	source node of the edge
	 * @param dest	destination node of the edge
	 * @return	true if the edge could be inverted
	 */
	public boolean flipEdge(T src, T dest) {
		
		if(doesItCreateCycle(this.getNode(dest),this.getNode(src))){
			return false;
		}

		return removeEdge(src, dest) && addEdge(dest, src);
	}
	
	public int nodeCount() {
		return nodeCount;
	}
	
	public int edgeCount() {
		return edgeCount;
	}
	
	public void removeAllNodes() {
		//limpar todos os mapas
		edgeMap.clear();
		nodeMap.clear();
		
		nodeCount = 0;
		edgeCount = 0;
	}
	
	public List<T> getNodes() {
		List<T> nodes = new ArrayList<T>();
		
		//preencher lista com os elementos de todos nós
		for(Node<T> node : edgeMap.keySet()) {
			nodes.add(node.get());
		}
		
		return nodes;
	}
	
	/**
	 * Tests if the node given by t of generic type T exists in the graph and returns the respective 
	 * node of type Node<T>.
	 * @return	the respective node of type Node<T> 
	 */
	protected Node<T> getNode(T t) {
		if(t == null) {
			throw new NullPointerException();
		}
		
		Node<T> node = nodeMap.get(t);
		if(node == null) {
			//t não existe no grafo
			throw new NoSuchElementException();
		}
		
		return node;
	}
	
	/**
	 * Returns a collection with all the parents of the node t. If t doesn't have parents an empty collection
	 * is returned.
	 * @param t	node
	 * @return	collection of parents of t
	 */
	public Collection<T> getParents(T t) {
		return this.getParents(getNode(t));
	}

	/**
	 * Returns a collection with all the parents of the node t. If t doesn't have parents an empty collection
	 * is returned.
	 * @param node
	 * @return	collection of parents of node
	 */
	protected Collection<T> getParents(Node<T> node) {
		Collection<T> parents = new ArrayList<>(nodeCount);
		//converter conjunto de Node<T> para conjunto de T
		for(Node<T> auxNode : edgeMap.get(node)) {
			parents.add(auxNode.get());
		}
		
		return parents;
	}

	/**
	 * Tests if adding the edge (source, destination) generates a cycle in the graph. It tests this locally 
	 * using a BFS algorithm.
	 * @param source		source node of the edge to add
	 * @param destination	destination node of the edge to add
	 * @return	true if a cycle is generated by the edge
	 */
	protected boolean doesItCreateCycle(Node<T> source, Node<T> destination) {
		
		Queue<Node<T>> queue = new LinkedList<Node<T>>(); // lista dos nós descobertos mas por visitar
		List<Node<T>> visitedNodes = new ArrayList<Node<T>>(); // lista dos nós já visitados
		
		//primeiro nó a ser "descoberto" é V (o destino da nova aresta)
		queue.add(source); 
		visitedNodes.add(source);
		
		Node<T> currentNode;
		Node<T> tmpNode;
		while(!queue.isEmpty()){
			
			currentNode = queue.poll(); // vai buscar à fila o próximo nó a visitar (e remove-o da fila)
			if(currentNode.equals(destination)){ 
				return true; // se o nó actual é U, foi encontrado um caminho de V para U, a aresta (U,V) vai criar um ciclo
			}
			
			visitedNodes.add(currentNode); // o nó actual é marcado como visitado
			Iterator<Node<T>> currentNodeIterator = this.parents(currentNode);
			// o iterador é usado para saber os nós vizinhos do nó actual
			
			while(currentNodeIterator.hasNext()){ // iterar por todos os nós vizinhos do nó actual
				tmpNode = currentNodeIterator.next(); 
				if(!visitedNodes.contains(tmpNode)){ 
					// cada nó vizinho é adicionado à lista de nós a visitar se ainda não foi visitado
					// isso só acontece para nós que ainda não tinham sido descobertos
					queue.add(tmpNode);
				}				
			}
		}
		
		// se não há mais nós descobertos por visitar e nenhum dos visitados era U,
		// então não há caminho de V para U e a aresta não vai criar um ciclo
		return false;
	}
	
	/**
	 * Iterator used to iterate trough all the parents of a node.
	 */
	protected class ParentsIterator implements Iterator<T> {
		
		Node<T> node = null;
		Iterator<Node<T>> nodeIterator = null;
		
		public ParentsIterator(Node<T> node) {
			this.node = node;
			nodeIterator = edgeMap.get(this.node).iterator();
		}
		
		public boolean hasNext() {
			return nodeIterator.hasNext();
		}

		public T next() {
			return nodeIterator.next().get();
		}

		public void remove() {
			throw new UnsupportedOperationException(); 
		}
	}
	
	public Iterator<T> parents(T t) throws NullPointerException, NoSuchElementException {
		return new ParentsIterator(getNode(t));
	}

	protected Iterator<Node<T>> parents(Node<T> node) {
		return edgeMap.get(node).iterator();
	}

	public Iterator<T> children(T t) throws NullPointerException, NoSuchElementException {
		//este método não é implementado aqui devido à sua ineficiencia
		throw new UnsupportedOperationException(); 
	}
	
	/**
	 * Returns a string with the format used to represent the graph.
	 */
	public String toString() {
		
		String string = "";
		
		for(Node<T> node : edgeMap.keySet()) {
			string += node + ": " + edgeMap.get(node) + '\n'; 
		}
		
		return string;
	}
	
	@Override
	public void getEdges(List<? super T> srcNodes, List<? super T> destNodes) {
		Set<Map.Entry<Node<T>, Collection<Node<T>>>> entries = edgeMap.entrySet();
		
		for(Map.Entry<Node<T>, Collection<Node<T>>> entry : entries) {
			for(Node<T> node : entry.getValue()) {
				destNodes.add(entry.getKey().get());
				srcNodes.add(node.get());
			}
		}
		
		return;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + edgeCount;
		result = prime * result + nodeCount;
		result = prime * result + ((edgeMap == null) ? 0 : edgeMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!(obj instanceof DirectedAcyclicGraph))
			return false;
		
		DirectedAcyclicGraph<?> other = (DirectedAcyclicGraph<?>) obj;
		
		if (edgeCount != other.edgeCount)
			return false;
		
		if (nodeCount != other.nodeCount)
			return false;
		
		if (edgeMap == null) {
			if (other.edgeMap != null)
				return false;
		} else {
			Set<Map.Entry<Node<T>, Collection<Node<T>>>> entries = edgeMap.entrySet();
			
			// iterar por cada entrada do edgeMap do objecto actual
			for(Map.Entry<Node<T>, Collection<Node<T>>> entry : entries) {
				// obter conjunto de pais para cada entrada
				Collection<?> parents = other.edgeMap.get(entry.getKey());
				if(parents == null)
					return false;
				
				// comparar conjunto de pais
				if(!parents.equals(entry.getValue())) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public Object clone() {
		
		DirectedAcyclicGraph<T> cloneDag = new DirectedAcyclicGraph<>(this.getNodes());
		
		Set<Map.Entry<Node<T>, Collection<Node<T>>>> entries = edgeMap.entrySet();
		for(Map.Entry<Node<T>, Collection<Node<T>>> entry : entries) {
			for(Node<T> node : entry.getValue()) {
				cloneDag.addEdge(node.t, entry.getKey().t);
			}
		}
		
		return cloneDag;
	}
	
}
