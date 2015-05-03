package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.List;

import graph.components.Node;
import graph.components.NodeFactory;

public abstract class Graph<T> {
	
	protected Map<Node<T>, Collection<Node<T>>> edgeMap = null;		// mapa que representa as arestas
	protected Map<T, Node<T>> nodeMap = null;								// permite obter o nó para um dado T
	protected Map<Integer, Node<T>> indexMap = null;					// permite obter o nó a partir de um dado indice 
	protected NodeFactory<T> factory = null;								// fabrica utilizada para criar os nós dependendo do tipo de grafo
	protected int nodeCount = 0;													// numero de nós no grafo
	
	@SuppressWarnings("unchecked")	//utilizado para remover os warnings do eclipse devido ao cast da factory
	public Graph(NodeFactory<? extends T> factory) {
		this.factory = (NodeFactory<T>) factory;
		this.edgeMap = new HashMap<>();
		this.nodeMap = new HashMap<>();
		this.indexMap = new HashMap<>();
	}
	
	public Graph(NodeFactory<? extends T> factory, Collection<? extends T> ts) {
		//chamar constructor anterior
		this(factory);
		 
		//criar nós a partir da collection
		for(T t : ts) {
			this.addNode(t);
		}
	}
	
	public void addNode(T t) {
		if(t == null) {
			throw new NullPointerException();
		}
		
		//usar como indice o numero de nós total para garantir que não é repetido
		Node<T> node = ((NodeFactory<T>) factory).createNode(nodeCount, ((T) t));
		
		//colocar nó no mapa de aresta com nenhuma aresta definida
		edgeMap.put(node, new HashSet<Node<T>>()); 
		
		//colocar nó nos mapas de indice e de T
		nodeMap.put(t, node);
		indexMap.put(nodeCount, node);
		
		nodeCount++;
	}
	
	public abstract void addEdge(T t1, T t2) throws NullPointerException, NoSuchElementException;
	
	public abstract void addEdge(int index1, int index2) throws NoSuchElementException;
	
	public List<T> getNodes() {
		List<T> nodes = new ArrayList<T>();
		
		//preencher lista com os elementos de todos nós
		for(Node<T> node : edgeMap.keySet()) {
			nodes.add(node.get());
		}
		
		return nodes;
	}
	
	public abstract void removeNode(T t) throws NullPointerException, NoSuchElementException;
	
	public abstract void removeNode(int index) throws NoSuchElementException;
	
	public abstract void removeEdge(T t1, T t2) throws NullPointerException, NoSuchElementException;
	
	public abstract void removeEdge(int index1, int index2) throws NoSuchElementException;
	
	public abstract void removeAllEdges(T t) throws NullPointerException, NoSuchElementException;
	
	public abstract void removeAllEdges(int index) throws NoSuchElementException;
	
	public int getNodeCount() {
		return nodeCount;
	}
	
}
