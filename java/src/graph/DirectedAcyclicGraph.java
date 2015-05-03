package graph;

import java.util.Collection;
import java.util.NoSuchElementException;

import graph.components.Node;
import graph.components.NodeFactory;

public class DirectedAcyclicGraph<T> extends Graph<T> {
	
	/*
	 * Nota:	no edgeMap são colocados os pais de cada nó para representar arestas direccionadas
	 */
	
	public DirectedAcyclicGraph(NodeFactory<? extends T> factory) {
		super(factory);
	}
	
	public DirectedAcyclicGraph(NodeFactory<? extends T> factory, Collection<? extends T> ts) {
		super(factory, ts);
	}
	
	@Override
	public void addEdge(T srcT, T desT) throws NullPointerException, NoSuchElementException {
		//criar aresta
		this.addEdge(getNode(srcT), getNode(desT));
	}

	@Override
	public void addEdge(int srcIndex, int destIndex) throws NoSuchElementException {
		//criar aresta
		this.addEdge(getNode(srcIndex), getNode(destIndex));
	}
	
	protected void addEdge(Node<T> srcNode, Node<T> destNode) {
		//adicionar @srcNode ao conjunto de nós pais do @destNode
		edgeMap.get(destNode).add(srcNode);
	}

	@Override
	public void removeNode(T t) throws NullPointerException, NoSuchElementException {
		//remover nó
		this.removeNode(getNode(t));
	}
	
	@Override
	public void removeNode(int index) throws NoSuchElementException {
		//remover nó
		this.removeNode(getNode(index));
	}
	
	protected void removeNode(Node<T> node) {
		//remover nó de todos os mapas
		edgeMap.remove(node);
		nodeMap.remove(node.get());
		indexMap.remove(node.getIndex());
		
		nodeCount--;
	}
	
	@Override
	public void removeEdge(T srcT, T desT) throws NullPointerException, NoSuchElementException {
		//remover aresta
		this.removeEdge(getNode(srcT), getNode(desT));
	}

	@Override
	public void removeEdge(int srcIndex, int destIndex) throws NoSuchElementException {
		//remover aresta
		this.removeEdge(getNode(srcIndex), getNode(destIndex));
	}
	
	protected void removeEdge(Node<T> srcNode, Node<T> destNode) {
		//remover @srcNode do conjunto de pais do @destNode
		edgeMap.get(destNode).remove(srcNode);
	}

	@Override
	public void removeAllEdges(T t) throws NullPointerException, NoSuchElementException {
		//remover todas as arestas do nó
		this.removeAllEdges(getNode(t));
	}

	@Override
	public void removeAllEdges(int index) throws NoSuchElementException {
		//remover todas as arestas do nó
		this.removeAllEdges(index);
	}
	
	protected void removeAllEdges(Node<T> node) {
		edgeMap.get(node).clear();
	}
	
	protected Node<T> getNode(T t) throws NullPointerException, NoSuchElementException {
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
	
	protected Node<T> getNode(int index) throws NoSuchElementException {
		Node<T> node = indexMap.get(index);
		if(node == null) {
			//não existe nenhum nó com índice @index
			throw new NoSuchElementException();
		}
		
		return node;
	}
}
