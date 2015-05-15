package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class DirectedAcyclicGraph<T> extends Graph<T> implements NavigableGraph<T> {
	
	/*
	 * Nota: no edgeMap são colocados os pais de cada nó para representar arestas direccionadas
	 */

	public DirectedAcyclicGraph() {
		super();
	}
	
	public DirectedAcyclicGraph(Collection<? extends T> ts) {
		super(ts);
	}
	
	/**
	 * Devolve um Node<T> tendo em conta o seu indice no grafo.
	 * @param index	indice do nó que se pretende obter
	 * @return	Node<T> correpondente ao index
	 * @throws NoSuchElementException	caso o indice indicado não corresponda a nenhum nó existente no grafo
	 */
	protected Node<T> getNode(int index) throws NoSuchElementException {
		Node<T> node = indexMap.get(index);
		if(node == null) {
			//não existe nenhum nó com índice @index
			throw new NoSuchElementException();
		}
		
		return node;
	}
	
	/**
	 * Devolve um Node<T> tendo em conta o objecto do tipo T que este armazena. É feito um mapeamento entre o
	 * objecto t recebido tendo em conta os seu métodos de equals() e hashcode(). Desta forma, caso exista no
	 * grafo um nó que armazene um objecto T igual a t este é retornado.
	 * @param t indice do nó que se pretende obter
	 * @return	Node<T> correpondente ao index
	 * @throws NoSuchElementException	caso o indice indicado não corresponda a nenhum nó existente no grafo
	 */
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
	
	/**
	 * Adiciona uma aresta com origem no nó srcNode e com destino no nó destNode. Não é feita qualquer 
	 * verificação sobre a existência dos nós recebidos no grafo uma vez que este método só pode ser 
	 * chamado dentro da classe do grafo ou classes que herdam o mesmo. Deixa-se assim ao cuidado de quem
	 * implementa um grafo ou uma classe que herda o mesmo a responsabilidade de garantir que ambos os nós
	 * existem no grafo.
	 * Este método é chamado por todos os outros métodos que adicionam arestas ao grafo com diferentes tipos
	 * de parametros.
	 * @param srcNode	nó de origem da aresta
	 * @param destNode	nó de destino da aresta
	 */
	protected boolean addEdge(Node<T> srcNode, Node<T> destNode) {
		//adicionar @srcNode ao conjunto de nós pais do @destNode
		//adicionar teste de ciclo
		return edgeMap.get(destNode).add(srcNode);
	}
	
	/**
	 * Remove node do grafo. Não é feita qualquer verificação sobre a existência do nó recebido no grafo 
	 * uma vez que este método só pode ser chamado dentro da classe do grafo ou classes que herdam o mesmo. 
	 * Deixa-se assim ao cuidado de quem implementa um grafo ou uma classe que herda o mesmo a responsabilidade 
	 * de garantir que node existe no grafo.
	 * Este método é chamado por todos os outros métodos que removem um nó do grafo com diferentes tipos
	 * de parametros.
	 * @param node nó do grafo que se prentende remover
	 */
	@Override
	protected void removeNode(Node<T> node) {
		//remover nó de todos os mapas
		edgeMap.remove(node);
		nodeMap.remove(node.get());
		indexMap.remove(node.getIndex());
		
		nodeCount--;
	}
	
	/**
	 * Remove aresta com origem no nó srcNode e com destino no nó destNode. Não é feita qualquer 
	 * verificação sobre a existência dos nós recebidos no grafo uma vez que este método só pode ser 
	 * chamado dentro da classe do grafo ou classes que herdam o mesmo. Deixa-se assim ao cuidado de quem
	 * implementa um grafo ou uma classe que herda o mesmo a responsabilidade de garantir que ambos os nós
	 * existem no grafo.
	 * Este método é chamado por todos os outros métodos que removem arestas ao grafo com diferentes tipos
	 * de parametros.
	 * @param srcNode	nó de origem da aresta a remover
	 * @param destNode	nó de destino da aresta a remover
	 */
	@Override
	protected void removeEdge(Node<T> srcNode, Node<T> destNode) {
		//remover @srcNode do conjunto de pais do @destNode
		edgeMap.get(destNode).remove(srcNode);
	}
	
	/**
	 * Remove todas as arestas existentes no grafo que tenham ligação com node. Não é feita qualquer 
	 * verificação sobre a existência dos nós recebidos no grafo uma vez que este método só pode ser 
	 * chamado dentro da classe do grafo ou classes que herdam o mesmo. Deixa-se assim ao cuidado de quem
	 * implementa um grafo ou uma classe que herda o mesmo a responsabilidade de garantir que ambos os nós
	 * existem no grafo.
	 * Este método é chamado por todos os outros métodos que removem todas as arestas do grafo com diferentes tipos
	 * @param node nó de quais as arestas se quer remover
	 */
	@Override
	protected void removeAllEdges(Node<T> node) {
		edgeMap.get(node).clear();
	}
	
	public void flipEdge(T srcNode, T destNode) {
		this.flipEdge(getNode(srcNode), getNode(destNode));
	}
	
	protected void flipEdge(Node<T> srcNode, Node<T> destNode) {
		//adicionar teste de ciclo
		// remover aresta actual
		removeEdge(srcNode, destNode);
		// adicionar aresta inversa
		addEdge(destNode, srcNode);
	}
	
	/**
	 * Devolve uma coleção de todos os nós pais do nó t. Caso este não tenha pais é retornada uma coleção vazia.
	 * @param	t nó de que se quer obter os pais
	 * @return	coleção de pais do nó t
	 * @throws NullPointerException		caso t seja null
	 * @throws NoSuchElementException	caso t não exista no grafo
	 */
	public Collection<T> getParents(T t) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que obter pais de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		return this.getParents(getNode(t));
	}
	
	/**
	 * Devolve uma coleção de todos os nós pais do nó de indice index. Caso este não tenha pais é retornada uma 
	 * coleção vazia.
	 * @param	index indice do nó de que se quer obter os pais
	 * @return	coleção de pais do nó de indice index
	 * @throws NoSuchElementException	caso não exista um nó com indice index
	 */
	public Collection<T> getParents(int index) throws NoSuchElementException {
		//usar metodo abstracto que obter pais de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		return this.getParents(getNode(index));
	}
	
	/**
	 * Devolve uma coleção de todos os nós pais do nó t. Caso este não tenha pais é retornada uma coleção vazia.
	 * Não é feita qualquer verificação sobre a existência do nó recebido no grafo uma vez que este método 
	 * só pode ser chamado dentro da classe do grafo ou classes que herdam o mesmo. Deixa-se assim ao cuidado 
	 * de quem implementa um grafo ou uma classe que herda o mesmo a responsabilidade de garantir que ambos os 
	 * nós existem no grafo.
	 * Este método é chamado por todos os outros métodos com o mesmo nome mas diferentes parametros.
	 * @param	node nó de que se quer obter os pais
	 * @return	coleção de pais de node
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
	 * Testa se adicionar uma aresta (U,V) (de U para V) cria um ciclo assumindo que o grafo é aciclico antes 
	 * da nova aresta, isto só acontece se já existe um caminho de V para U, uma vez que nesse caso é possivel 
	 * ir de V para U e depois usar a nova aresta para ir de U para V (ciclo). O teste é feito com base numa BFS, 
	 * uma vez que esta DAG pode ter altura infinita mas largura limitada a 3 andares.
	 * @param source		nó de oridem da aresta
	 * @param destination	nó de destino da aresta
	 * @return	true caso se forme um iclo e false caso contrário
	 */
	protected boolean doesItCreateCycle(Node<T> source, Node<T> destination) {
		
		Queue<Node<T>> queue = new LinkedList<Node<T>>(); // lista dos nós descobertos mas por visitar
		List<Node<T>> visitedNodes = new ArrayList<Node<T>>(); // lista dos nós já visitados
		
		//primeiro nó a ser "descoberto" é V (o destino da nova aresta)
		queue.add(destination); 
		visitedNodes.add(destination);
		
		Node<T> currentNode;
		Node<T> tmpNode;
		while(!queue.isEmpty()){
			
			currentNode = queue.poll(); // vai buscar à fila o próximo nó a visitar (e remove-o da fila)
			if(currentNode.equals(source)){ 
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
	 * Innerclass que implementa um iterador que permite iterar por todos o nós pais de um certo nó. Este
	 * iterador é para ser utilizado apenas pelos clientes deste grafo uma vez que é um iterador de T's. Caso
	 * se pretenda utilizar um iterador para iterar pelos pais de um certo nó dentro desta classe deve ser utilizado
	 * um iterador Iterator<Node<T>> que é retornado pelo método parents(Node<T> node).
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
	
	/**
	 * Devolve um iterador de nós do tipo T que permite iterar pelos nós pais do nó t. Este método deve ser 
	 * usado pelo cliente.
	 * @param t nó do qual se pretende obter os pais
	 * @return iterador dos pais do nó
	 */
	public Iterator<T> parents(T t) throws NullPointerException, NoSuchElementException {
		return new ParentsIterator(getNode(t));
	}
	
	/**
	 * Devolve um iterador de nós do tipo T que permite iterar pelos nós pais do nó de indice index. Este método 
	 * deve ser usado pelo cliente.
	 * @param index indice do nó do qual se pretende obter os pais
	 * @return iterador dos pais do nó
	 */
	public Iterator<T> parents(int index) throws NoSuchElementException {
		return new ParentsIterator(getNode(index));
	}
	
	/**
	 * Devolve um iterador de nós do tipo Node<T> que permite iterar pelos nós pais do nó de indice index. 
	 * Este método é que deve ser utilizado dentro dos métodos da classe.
	 * @param node nó do qual se pretende obter os pais
	 * @return iterador dos pais do nó
	 */
	protected Iterator<Node<T>> parents(Node<T> node) {
		return edgeMap.get(node).iterator();
	}

	public Iterator<T> children(T t) throws NullPointerException, NoSuchElementException {
		//este método não é implementado aqui devido à sua ineficiencia
		throw new UnsupportedOperationException(); 
	}

	public Iterator<T> children(int index) throws NoSuchElementException {
		//este método não é implementado aqui devido à sua ineficiencia
		throw new UnsupportedOperationException(); 
	}
}
