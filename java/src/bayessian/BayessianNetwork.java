package bayessian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import dataset.TransitionDataset;
import graph.DirectedAcyclicGraph;

public class BayessianNetwork<T extends RandomVariable> {
	
	protected DirectedAcyclicGraph<RandomVariable> graph = new DirectedAcyclicGraph<RandomVariable>();
	protected RandomVariable[] vars = null;
	protected Map<RandomVariable, Integer> varsToIndex = null; 
	
	/**
	 * 
	 * @param vars
	 * @param dataset
	 * @param score
	 */
	public BayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score score) {
		this.vars = Arrays.copyOf(vars, vars.length);
		
		//construir mapa de indices
		this.varsToIndex = new HashMap<>(this.vars.length);
		for(int i = 0; i < this.vars.length; i++) {
			varsToIndex.put(this.vars[i], i);
		}
		
		// implementar geedy-hill para construir dataset
	}
	
	/**
	 * Iterador que pode ser usado para iterar por todas as variáveis aleatórias da BN.
	 * Este iterador utiliza apenas os indices das variáveis tendo em conta o vector aleatório.
	 * O objectivo deste iterador é ser usado por métodos deste package para iterar pelas vários
	 * indices que correspondem às variáveis aleatórias armazenadas em vars.
	 */
	protected class BayessianIterator implements Iterator<Integer> {

		protected int currentIndex = 0;		// indice da variável aleatória actual
		
		/**
		 * Testa se ainda existem variáveis para iterar por neste iterador.
		 */
		@Override
		public boolean hasNext() {
			return currentIndex < vars.length;
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
		
		/**
		 * Retorna uma coleção com os indices dos pais da variável aleatória actual do iterador.
		 * @return coleção dos indices dos pais
		 */
		public Collection<Integer> getParents() {
			Collection<Integer> parents = new ArrayList<>(3);
			//converter collection de random variables para collection de indices
			for(RandomVariable parent : graph.getParents(vars[currentIndex])) {
				parents.add(varsToIndex.get(parent));
			}
			
			return parents;
		}
		
		/**
		 * Retorna o range da variável aleatória actual.
		 * @return range da variável aleatória actual
		 */
		public int getRange() {
			return vars[currentIndex].getRange();
		}
		
		/**
		 * Retorna um objecto do tipo RandomVariable da variável aleatória actual do iterador.
		 * @return variável aleatória actual
		 */
		public RandomVariable getVariable() {
			return vars[currentIndex];
		}
		
		/**
		 * Retorna o número de configurações dos pais da variável aleatória actual.
		 * @return número de configurações dos pais
		 */
		public int getParentConfigurationCount() {
			
			Collection<RandomVariable> parents = graph.getParents(vars[currentIndex]);
			if(parents.isEmpty()) {
				return 0;
			}
			
			int count = 1;
			for(RandomVariable parent : parents) {
				count *= parent.getRange();
			}
			
			return count; 
		}
		
	}
	
	protected BayessianIterator iterator() {
		return new BayessianIterator();
	}
	
}
