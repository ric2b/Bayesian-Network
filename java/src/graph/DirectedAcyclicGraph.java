package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import graph.components.Node;
import graph.components.NodeFactory;

public class DirectedAcyclicGraph<T> extends Graph<T> implements NavigableGraph<T> {
	
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
	
	public Collection<T> getParents(T t) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que obter pais de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		return this.getParents(getNode(t));
	}
	
	public Collection<T> getParents(int index) throws NoSuchElementException {
		//usar metodo abstracto que obter pais de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		return this.getParents(getNode(index));
	}
	
	protected Collection<T> getParents(Node<T> node) {
		Collection<T> parents = new ArrayList<>(nodeCount);
		//converter conjunto de Node<T> para conjunto de T
		for(Node<T> auxNode : edgeMap.get(node)) {
			parents.add(auxNode.get());
		}
		
		return parents;
	}
	
	protected boolean isCycle(Node<T> node) {
		//método precisa de ser implementado
		return false;
	}
	
	protected class ParentsIterator implements Iterator<T> {
		
		Node<T> node = null;
		Iterator<Node<T>> nodeIterator = null;
		
		public ParentsIterator(Node<T> node) {
			this.node = node;
			nodeIterator = edgeMap.get(this.node).iterator();
		}
		
		@Override
		public boolean hasNext() {
			return nodeIterator.hasNext();
		}

		@Override
		public T next() {
			return nodeIterator.next().get();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(); 
		}
	}
	
	@Override
	public Iterator<T> parents(T t) throws NullPointerException, NoSuchElementException {
		return new ParentsIterator(getNode(t));
	}

	@Override
	public Iterator<T> parents(int index) throws NoSuchElementException {
		return new ParentsIterator(getNode(index));
	}

	@Override
	public Iterator<T> children(T t) throws NullPointerException, NoSuchElementException {
		//este método não é implementado aqui devido à sua ineficiencia
		throw new UnsupportedOperationException(); 
	}

	@Override
	public Iterator<T> children(int index) throws NoSuchElementException {
		//este método não é implementado aqui devido à sua ineficiencia
		throw new UnsupportedOperationException(); 
	}
}
