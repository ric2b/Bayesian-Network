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
	protected void addEdge(Node<T> srcNode, Node<T> destNode) {
		//adicionar @srcNode ao conjunto de nós pais do @destNode
		edgeMap.get(destNode).add(srcNode);
	}
	
	@Override
	protected void removeNode(Node<T> node) {
		//remover nó de todos os mapas
		edgeMap.remove(node);
		nodeMap.remove(node.get());
		indexMap.remove(node.getIndex());
		
		nodeCount--;
	}
	
	@Override
	protected void removeEdge(Node<T> srcNode, Node<T> destNode) {
		//remover @srcNode do conjunto de pais do @destNode
		edgeMap.get(destNode).remove(srcNode);
	}
	
	@Override
	protected void removeAllEdges(Node<T> node) {
		edgeMap.get(node).clear();
	}
	
	@Override
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
	
	@Override
	protected Node<T> getNode(int index) throws NoSuchElementException {
		Node<T> node = indexMap.get(index);
		if(node == null) {
			//não existe nenhum nó com índice @index
			throw new NoSuchElementException();
		}
		
		return node;
	}
}
