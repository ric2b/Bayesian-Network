package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import graph.components.Node;

public class DirectedAcyclicGraph<T> extends Graph<T> implements NavigableGraph<T> {
	
	/*
	 * Nota: no edgeMap são colocados os pais de cada nó para representar arestas direccionadas
	 */
	
	/**
	 * Constructor sem argumentos
	 */
	public DirectedAcyclicGraph() {
		super();
	}
	
	public DirectedAcyclicGraph(Collection<? extends T> ts) {
		super(ts);
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
	
	protected boolean doesItCreateCycle(Node<T> source, Node<T> destination) {
		// testa se adicionar uma aresta (U,V) (de U para V) cria um ciclo
		// assumindo que o grafo � aciclico antes da nova aresta, isto s� acontece se j� existe um caminho de V para U,
		// uma vez que nesse caso � possivel ir de V para U e depois usar a nova aresta para ir de U para V (ciclo)
		// O teste � feito com base numa BFS, uma vez que esta DAG pode ter altura infinita mas largura limitada a 3^andares 
		
		Queue<Node<T>> queue = new LinkedList<Node<T>>(); // lista dos n�s descobertos mas por visitar
		List<Node<T>> visitedNodes = new ArrayList<Node<T>>(); // lista dos n�s j� visitados
		
		//primeiro n� a ser "descoberto" � V (o destino da nova aresta)
		queue.add(destination); 
		visitedNodes.add(destination);
		
		Node<T> currentNode;
		Node<T> tmpNode;
		while(!queue.isEmpty()){
			
			currentNode = queue.poll(); // vai buscar � fila o pr�ximo n� a visitar (e remove-o da fila)
			if(currentNode.equals(source)){ 
				return true; // se o n� actual � U, foi encontrado um caminho de V para U, a aresta (U,V) vai criar um ciclo
			}
			
			visitedNodes.add(currentNode); // o n� actual � marcado como visitado
			Iterator<Node<T>> currentNodeIterator = this.parents(currentNode); 
			// o iterador � usado para saber os n�s vizinhos do n� actual
			
			while(currentNodeIterator.hasNext()){ // iterar por todos os n�s vizinhos do n� actual
				tmpNode = currentNodeIterator.next(); 
				if(!visitedNodes.contains(tmpNode)){ 
					// cada n� vizinho � adicionado � lista de n�s a visitar se ainda n�o foi visitado
					// isso s� acontece para n�s que ainda n�o tinham sido descobertos
					queue.add(tmpNode);
				}				
			}
		}
		
		// se n�o h� mais n�s descobertos por visitar e nenhum dos visitados era U,
		// ent�o n�o h� caminho de V para U e a aresta n�o vai criar um ciclo
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

	protected Iterator<Node<T>> parents(Node<T> node) {
		return edgeMap.get(node).iterator();
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
