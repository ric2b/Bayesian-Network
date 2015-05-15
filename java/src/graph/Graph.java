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
	
	public void addNode(T t);
	public void addEdge(T t1, T t2) throws NullPointerException, NoSuchElementException;
	public void removeNode(T t) throws NullPointerException, NoSuchElementException;
	public void removeAllNodes();
	public void removeEdge(T t1, T t2) throws NullPointerException, NoSuchElementException;
	public void removeAllEdges(T t) throws NullPointerException, NoSuchElementException;
	public int nodeCount();
	public List<T> getNodes();
	
}
