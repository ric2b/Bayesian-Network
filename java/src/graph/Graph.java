package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.List;

import graph.components.Node;

public abstract class Graph<T> {
	
	protected Map<Node<T>, Collection<Node<T>>> edgeMap = null;		// mapa que representa as arestas
	protected Map<T, Node<T>> nodeMap = null;						// permite obter o nó para um dado T
	protected Map<Integer, Node<T>> indexMap = null;				// permite obter o nó a partir de um dado indice 
	protected int nodeCount = 0;									// numero de nós no grafo
	
	/**
	 * Constructor sem argumentos do grafo
	 */
	public Graph() {
		this.edgeMap = new HashMap<>();
		this.nodeMap = new HashMap<>();
		this.indexMap = new HashMap<>();
	}
	
	/**
	 * Constructor do grafo que permite introduzir uma coleção de nós no grafo.
	 * Estes nós são colocados no grafo sem qualquer ligação entre eles.
	 * @param ts	coleção de T's com que se pretende iniciar o grafo 
	 */
	public Graph(Collection<? extends T> ts) {
		//criar nós a partir da collection
		for(T t : ts) {
			this.addNode(t);
		}
	}
	
	public List<T> getNodes() {
		List<T> nodes = new ArrayList<T>();
		
		//preencher lista com os elementos de todos nós
		for(Node<T> node : edgeMap.keySet()) {
			nodes.add(node.get());
		}
		
		return nodes;
	}
	
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
		indexMap.put(nodeCount, node);
		
		nodeCount++;
	}
	
	protected abstract Node<T> getNode(int index) throws NoSuchElementException;
	
	protected abstract Node<T> getNode(T t) throws NullPointerException, NoSuchElementException;
	
	protected abstract void addEdge(Node<T> node1, Node<T> node2);
	
	public void addEdge(T t1, T t2) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que adiciona a aresta a partir de  Nodes
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.addEdge(getNode(t1), getNode(t2));
	}

	public void addEdge(int index1, int index2) throws NoSuchElementException {
		//usar metodo abstracto que adiciona a aresta
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.addEdge(getNode(index1), getNode(index2));
	}
	
	protected abstract void removeNode(Node<T> node);
	
	public void removeNode(T t) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que remove um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeNode(getNode(t));
	}
	
	public void removeNode(int index) throws NoSuchElementException {
		//usar metodo abstracto que remove um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeNode(getNode(index));
	}

	protected abstract void removeEdge(Node<T> node1, Node<T> node2);
	
	public void removeEdge(T t1, T t2) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que remove uma aresta
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeEdge(getNode(t1), getNode(t2));
	}

	public void removeEdge(int index1, int index2) throws NoSuchElementException {
		//usar metodo abstracto que remove uma aresta
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeEdge(getNode(index1), getNode(index2));
	}

	protected abstract void removeAllEdges(Node<T> node);
	
	public void removeAllEdges(T t) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que remove todas as arestas de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeAllEdges(getNode(t));
	}

	public void removeAllEdges(int index) throws NoSuchElementException {
		//usar metodo abstracto que remove todas as arestas de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeAllEdges(index);
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	public void clear() {
		//limpar todos os mapas
		edgeMap.clear();
		nodeMap.clear();
		indexMap.clear();
		
		nodeCount = 0;
	}
	
}
