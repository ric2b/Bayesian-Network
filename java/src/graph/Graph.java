package graph;

import java.util.List;
import java.util.NoSuchElementException;

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

public interface Graph<T> {
	
	/**
	 * Adds a node to the graph
	 * @param t	node to add to the graph
	 */
	public void addNode(T t);
	
	/**
	 * Adds an edge to the graph based on the two given nodes
	 * @param t1	node 1 of the edge
	 * @param t2	node 2 of the edge
	 * @return	true if the edge was added to the graph
	 */
	public boolean addEdge(T t1, T t2);
	
	/**
	 * Adds multiple edges to the graph. The indexes of the nodes of the lists are used to match the edge
	 * to be added. If one of the list is bigger then the other, the nodes wth no match are not considered.  
	 * @param nodes1	first list of nodes
	 * @param nodes2	second list of nodes
	 * @return	the number of edges added to the graph
	 */
	public int addEdge(List<? extends T> nodes1, List<? extends T> nodes2);
	
	/**
	 * Removes a node from the graph
	 * @param t	node to be removed
	 */
	public void removeNode(T t);
	
	/**
	 * Removes all nodes from the graph. This removes all the edges as well.
	 */
	public void removeAllNodes();
	
	/**
	 * Removes the edge between the nodes t1 an t2 from the graph.
	 * @param t1	node 1
	 * @param t2	node 2
	 * @return	true if the edge was removed
	 */
	public boolean removeEdge(T t1, T t2) throws NullPointerException, NoSuchElementException;
	
	/**
	 * Removes all edges from the graph.
	 */
	public void removeAllEdges();
	
	/**
	 * Removes all edges between the node t
	 * @param t	node
	 */
	public void removeAllEdges(T t);
	
	/**
	 * Returns the number of nodes in the graph.
	 * @return number of nodes in the graph
	 */
	public int nodeCount();
	
	/**
	 * Returns the number of edges in the graph.
	 * @return number of edges in the graph
	 */
	public int edgeCount();
	
	/**
	 * Returns a list with all the nodes in the graph
	 * @return a list with all the nodes in the graph
	 */
	public List<T> getNodes();
	
	/**
	 * Returns two lists with the edges of the graph.
	 * @param nodes1 list of nodes 1
	 * @param nodes2 list of nodes 2
	 */
	public void getEdges(List<? super T> srcNodes, List<? super T> destNodes);
	
}
