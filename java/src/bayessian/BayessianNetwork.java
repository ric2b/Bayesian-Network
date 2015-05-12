package bayessian;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import dataset.TransitionDataset;
import graph.DirectedAcyclicGraph;

public class BayessianNetwork<T extends RandomVariable> implements Iterable<Integer> {
	
	protected DirectedAcyclicGraph<RandomVariable> graph = new DirectedAcyclicGraph<RandomVariable>();
	protected RandomVariable[] vars = null;
	protected Map<RandomVariable, Integer> varsToIndex = null; 
	
	/**
	 * 
	 * @param vars
	 * @param dataset
	 * @param score
	 */
	public BayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score<T> score) {
		this.vars = Arrays.copyOf(vars, vars.length);
		
		//construir mapa de indices
		this.varsToIndex = new HashMap<>(this.vars.length);
		for(int i = 0; i < this.vars.length; i++) {
			varsToIndex.put(this.vars[i], i);
		}
		
		// implementar geedy-hill para construir dataset
	}
	
	/**
	 * Retorna um objecto do tipo RandomVariable da variável aleatória actual do iterador.
	 * @return variável aleatória actual
	 */
	public RandomVariable getVariable(int index) {
		return vars[index];
	}	
	
	/**
	 * Retorna o range da variável aleatória actual.
	 * @return range da variável aleatória actual
	 */
	public int getRange(int index) {
		return vars[index].getRange();
	}
	
//	/**
//	 * Retorna uma coleção com os indices dos pais da variável aleatória actual do iterador.
//	 * @return coleção dos indices dos pais
//	 */
//	public Collection<Integer> getParents(int index) {
//		Collection<Integer> parents = new ArrayList<>(3);
//		//converter collection de random variables para collection de indices
//		for(RandomVariable parent : graph.getParents(vars[index])) {
//			parents.add(varsToIndex.get(parent));
//		}
//		
//		return parents;
//	}
	
	/**
	 * Retorna um array com os indices dos pais da variável aleatória actual do iterador.
	 * @param index
	 * @return array com os indices dos pais
	 */
	public int[] getParents(int index) {
		//obter collection dada pelo grafo
		Collection<RandomVariable> graphParents = this.graph.getParents(this.vars[index]);
		
		//converter collection de random variables para um array de indices dos pais
		int[] parents = new int[graphParents.size()];
		//converter collection de random variables para collection de indices
		int i = 0;
		for(RandomVariable parent : graphParents) {
			parents[i] = this.varsToIndex.get(parent);
			i++;
		}
		
		return parents;
	}
	
	/**
	 * Retorna o número de configurações dos pais da variável aleatória dada.
	 * @return número de configurações dos pais
	 */	
	public int getParentConfigurationCount(int index) {
		Collection<RandomVariable> parents = graph.getParents(vars[index]);
		if(parents.isEmpty()) {
			return 0;
		}
		
		int count = 1;
		for(RandomVariable parent : parents) {
			count *= parent.getRange();
		}		
		return count; 
	}

	/**
	 * Retorna o número de configurações dos pais da variável aleatória dada.
	 * @return número de configurações dos pais
	 */	
	public int[] getParentRanges(int index) {
		Collection<RandomVariable> parents = graph.getParents(vars[index]);
		int[] parentRanges = new int[3];
		
		int i = 0;
		for(RandomVariable parent : parents) {
			parentRanges[i] = parent.getRange();
			i++;
		}
		
		return parentRanges; 
	}
	
	/**
	 * Iterador que pode ser usado para iterar por todas as variáveis aleatórias da BN.
	 * Este iterador utiliza apenas os indices das variáveis tendo em conta o vector aleatório.
	 * O objectivo deste iterador é ser usado por métodos deste package para iterar pelas vários
	 * indices que correspondem às variáveis aleatórias armazenadas em vars.
	 */
	protected class BayessianIterator<E extends Number> implements Iterator<Integer> {

		protected int currentIndex = 0;		// indice da variável aleatória actual
		
		/**
		 * Testa se ainda existem variáveis para iterar por neste iterador.
		 */
		@Override
		public boolean hasNext() {
			return currentIndex < BayessianNetwork.this.vars.length;
		}
		
		/**
		 * Retorna o indice da variavel aleatória actual do iterador e incrementa a posição actual
		 * do iterador para a próxima variável aleatória.
		 */
		@Override
		public Integer next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			
			//retornar index actual e incrementar index actual para o próximo
			return Integer.valueOf(currentIndex++);
		}

		/**
		 * Não implementada!!
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}	

	}
	
	/**
	 * Devolve um BayessianIterator que permite iterar por todas as variáveis aleatórias da BN pelo seus indices.
	 * @return iterador por indices das variaveis da BN
	 */
	public BayessianIterator<Integer> iterator() {
		return new BayessianIterator<Integer>();
	}
}
