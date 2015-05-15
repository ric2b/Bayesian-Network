package graph;

/*
 * Detalhes de implementação de um grafo:
 * 1) do ponto de vista do cliente não existe o conceito de Node, o que singnifica que sempre
 * 	  que um método retorna um nó para o cliente é retornado o objecto T que o nó representa
 * 	  em vez de um objecto de Node
 * 2) as arestas de um nó só representadas através de um mapa. para cada nó é mapeado uma
 * 	  coleção de nós que representam os pais do mesmo
 * 3) são utilizados os mapas nodeMap e indexMap para fazer o mapeamento entre os objectos de
 * 	  tipo T e dos indices do nós
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.List;

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
	
	protected abstract Node<T> getNode(int index) throws NoSuchElementException;
	
	protected abstract Node<T> getNode(T t) throws NullPointerException, NoSuchElementException;
	
	protected abstract boolean addEdge(Node<T> node1, Node<T> node2);
	
	/**
	 * Adiciona uma aresta entre os nós que representam t1 e t2. Caso não exista algum dos nós recebidos
	 * no grafo a aresta não é criada e é feito um throw de uma NoSuchElementException.
	 * @param t1 primeiro nó da aresta
	 * @param t2 segundo nó da aresta
	 * @throws NullPointerException		caso t1 ou t2 sejam null
	 * @throws NoSuchElementException	caso t1 ou t2 não existam no grafo
	 */
	public boolean addEdge(T t1, T t2) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que adiciona a aresta a partir de  Nodes
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		return this.addEdge(getNode(t1), getNode(t2));
	}
	
	/**
	 * Adiciona uma aresta entre os nós com índice index1 e index2. Caso algum dos indices não represente
	 * nenhum nó do grafo a aresta não é criada e é feito um throw de uma NoSuchElementException.
	 * @param index1 indice do primeiro nó da aresta
	 * @param index2 indice do segundo nó da aresta
	 * @throws NoSuchElementException	caso index1 ou index2 não sejam indices de nenhum dos nós do grafo
	 */
	public boolean addEdge(int index1, int index2) throws NoSuchElementException {
		//usar metodo abstracto que adiciona a aresta
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		return this.addEdge(getNode(index1), getNode(index2));
	}
	
	protected abstract void removeNode(Node<T> node);
	
	/**
	 * Remove um nó do grafo. Caso t não exista no grafo não é feita qualquer alteração ao mesmo.
	 * @param t nó do grafo que se prentende remover
	 * @throws NullPointerException		caso t seja null
	 * @throws NoSuchElementException	caso t não exista no grafo
	 */
	public void removeNode(T t) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que remove um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeNode(getNode(t));
	}
	
	/**
	 * Remove um nó do grafo a partir do índice. Caso não exista um nó com indice indexno grafo não é feita 
	 * qualquer alteração ao mesmo.
	 * @param index indice do nó que se pretende remover
	 * @throws NoSuchElementException	caso não exista nenhum nó com indice recebido
	 */
	public void removeNode(int index) throws NoSuchElementException {
		//usar metodo abstracto que remove um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeNode(getNode(index));
	}

	protected abstract void removeEdge(Node<T> node1, Node<T> node2);
	
	/**
	 * Remove aresta que liga os nós t1 e t2 do grafo. Caso um destes nós não exista no grafo não é feita
	 * qualquer alteração ao estado do mesmo.
	 * @param t1 primeiro nó da aresta a remover
	 * @param t2 segundo nó da aresta a remover
	 * @throws NullPointerException		caso t1 ou t2 sejam null
	 * @throws NoSuchElementException	caso t1 ou t2 não exista no grafo
	 */
	public void removeEdge(T t1, T t2) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que remove uma aresta
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeEdge(getNode(t1), getNode(t2));
	}

	/**
	 * Remove aresta que liga os nós com indice index1 e index2. Caso um destes nós não exista no grafo não 
	 * é feita qualquer alteração ao estado do mesmo.
	 * @param t1 primeiro nó da aresta a remover
	 * @param t2 segundo nó da aresta a remover
	 * @throws NullPointerException		caso t1 ou t2 sejam null
	 * @throws NoSuchElementException	caso t1 ou t2 não exista no grafo
	 */
	public void removeEdge(int index1, int index2) throws NoSuchElementException {
		//usar metodo abstracto que remove uma aresta
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeEdge(getNode(index1), getNode(index2));
	}

	protected abstract void removeAllEdges(Node<T> node);
	
	/**
	 * Remove todas as arestas existentes no grafo que tenham ligação com o nó t. Caso t não exista no grafo,
	 * não é feita qualquer alteração ao mesmo.
	 * @param t nó de quais as arestas se quer remover
	 * @throws NullPointerException		caso t seja null
	 * @throws NoSuchElementException	caso t não exista no grafo
	 */
	public void removeAllEdges(T t) throws NullPointerException, NoSuchElementException {
		//usar metodo abstracto que remove todas as arestas de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeAllEdges(getNode(t));
	}
	
	/**
	 * Remove todas as arestas existentes no grafo que tenham ligação com o nó de indice index. Caso tal nó 
	 * não exista no grafo, não é feita qualquer alteração ao mesmo.
	 * @param index indice do nó do qual se quer remover as arestas
	 * @throws NoSuchElementException	caso não exista nenhum nó com indice index
	 */
	public void removeAllEdges(int index) throws NoSuchElementException {
		//usar metodo abstracto que remove todas as arestas de um nó
		//este metodo abstracto deve ser implementado por um subclasse de acordo com as suas especificações
		this.removeAllEdges(index);
	}
	
	/**
	 * Retorna o número de nós existente no grafo.
	 * @return número de nós no grafo
	 */
	public int getNodeCount() {
		return nodeCount;
	}
	
	/**
	 * Remove todos os nós e arestas dos grafo.
	 */
	public void clear() {
		//limpar todos os mapas
		edgeMap.clear();
		nodeMap.clear();
		indexMap.clear();
		
		nodeCount = 0;
	}
	
}
