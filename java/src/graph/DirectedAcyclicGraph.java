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

public class DirectedAcyclicGraph<T> implements Graph<T>, NavigableGraph<T> {
	
	protected Map<Node<T>, Collection<Node<T>>> edgeMap = new HashMap<>();;		// mapa que representa as arestas
	protected Map<T, Node<T>> nodeMap = new HashMap<>();								// permite obter o nó para um dado T
	protected Map<Integer, Node<T>> indexMap = new HashMap<>();						// permite obter o nó a partir de um dado indice 
	protected int nodeCount = 0;																// numero de nós no grafo
	
	/**
	 * Cria um grafo vazio
	 */
	public DirectedAcyclicGraph() {
		; // grafo vazio
	}
	
	/**
	 * Constructor do grafo que permite introduzir uma coleção de nós no grafo.
	 * Estes nós são colocados no grafo sem qualquer ligação entre eles.
	 * @param ts	coleção de T's com que se pretende iniciar o grafo 
	 */
	public DirectedAcyclicGraph(Collection<? extends T> ts) {
		//criar nós a partir da collection
		for(T t : ts) {
			this.addNode(t);
		}
	}
	
	/**
	 * Adiciona um novo nó ao grafo. Este novo nó é adicionado sem qualquer aresta, ou seja,
	 * sem qualquer ligação e nenhum dos outros nós que já existam no grafo.
	 * @param t	objecto T que se pretende adicionar ao grafo
	 */
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
	
	/**
	 * Adiciona uma aresta entre os nós que representam src e dest. Caso não exista algum dos nós recebidos
	 * no grafo a aresta não é criada e é feito um throw de uma NoSuchElementException.
	 * @param src primeiro nó da aresta
	 * @param dest segundo nó da aresta
	 * @throws NullPointerException		caso src ou dest sejam null
	 * @throws NoSuchElementException	caso src ou dest não existam no grafo
	 */
	public void addEdge(T src, T dest) throws NullPointerException, NoSuchElementException {
		edgeMap.get(getNode(dest)).add(getNode(src));
	}
	
	/**
	 * Remove um nó do grafo. Caso t não exista no grafo não é feita qualquer alteração ao mesmo.
	 * @param t nó do grafo que se prentende remover
	 * @throws NullPointerException		caso t seja null
	 * @throws NoSuchElementException	caso t não exista no grafo
	 */
	public void removeNode(T t) throws NullPointerException, NoSuchElementException {
		Node<T> node = getNode(t);
		
		//remover nó de todos os mapas
		edgeMap.remove(node);
		nodeMap.remove(node.get());
		indexMap.remove(node.getIndex());
		
		nodeCount--;
	}
	
	/**
	 * Remove aresta que liga os nós src e dest do grafo. Caso um destes nós não exista no grafo não é feita
	 * qualquer alteração ao estado do mesmo.
	 * @param src primeiro nó da aresta a remover
	 * @param dest segundo nó da aresta a remover
	 * @throws NullPointerException		caso src ou dest sejam null
	 * @throws NoSuchElementException	caso src ou dest não exista no grafo
	 */
	public void removeEdge(T src, T dest) throws NullPointerException, NoSuchElementException {
		//remover src do conjunto de pais do dest
		edgeMap.get(getNode(dest)).remove(getNode(src));
	}	
	
	/**
	 * Remove todas as arestas existentes no grafo que tenham ligação com o nó t. Caso t não exista no grafo,
	 * não é feita qualquer alteração ao mesmo.
	 * @param t nó de quais as arestas se quer remover
	 * @throws NullPointerException		caso t seja null
	 * @throws NoSuchElementException	caso t não exista no grafo
	 */
	public void removeAllEdges(T t) throws NullPointerException, NoSuchElementException {
		edgeMap.get(getNode(t)).clear();
	}
	
	/**
	 * Retorna o número de nós existente no grafo.
	 * @return número de nós no grafo
	 */
	public int nodeCount() {
		return nodeCount;
	}
	
	/**
	 * Remove todos os nós do grafo.
	 */
	public void removeAllNodes() {
		//limpar todos os mapas
		edgeMap.clear();
		nodeMap.clear();
		indexMap.clear();
		
		nodeCount = 0;
	}
	
	/**
	 * Retorna uma lista com todos os nós existentes actualmente no grafo.
	 * Caso não existam nós no grafo é retornada uma lista vazia em vez de null, o que
	 * faz com que o cliente não tenha de testar o retorno do método para o usar. 
	 * @return lista de todos os nós do grafo
	 */
	public List<T> getNodes() {
		List<T> nodes = new ArrayList<T>();
		
		//preencher lista com os elementos de todos nós
		for(Node<T> node : edgeMap.keySet()) {
			nodes.add(node.get());
		}
		
		return nodes;
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
	
	public void flipEdge(T src, T dest) {
		//adicionar teste de ciclo
		// remover aresta actual
		removeEdge(src, dest);
		// adicionar aresta inversa
		addEdge(dest, src);
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

}
