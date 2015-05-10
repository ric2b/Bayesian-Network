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

public class BayessianNetwork<T extends RandomVariable> implements Iterable<Integer> {
	
	protected DirectedAcyclicGraph<RandomVariable> graph = new DirectedAcyclicGraph<RandomVariable>();
	protected RandomVariable[] vars = null;
	protected EstimateTable[] estimates = null;
	protected Map<RandomVariable, Integer> varsToIndex = null; 
	
	/**
	 * 
	 * @param vars
	 * @param dataset
	 * @param score
	 */
	public BayessianNetwork(RandomVariable[] vars, EstimateTable estimates [], TransitionDataset dataset, Score<T> score) {
		this.vars = Arrays.copyOf(vars, vars.length);
		this.estimates = Arrays.copyOf(estimates, estimates.length);
		
		//construir mapa de indices
		this.varsToIndex = new HashMap<>(this.vars.length);
		for(int i = 0; i < this.vars.length; i++) {
			varsToIndex.put(this.vars[i], i);
		}
		
		// implementar geedy-hill para construir dataset
	}
	
	/**
	 * Retorna um objecto do tipo RandomVariable da variavel aleatoria actual do iterador.
	 * @return variavel aleatoria actual
	 */
	public RandomVariable getVariable(int index) {
		return vars[index];
	}	
	
	public EstimateTable getEstimate(int index) {
		return estimates[index];
	}
	
	/**
	 * Retorna o range da variavel aleatoria actual.
	 * @return range da variavel aleatoria actual
	 */
	public int getRange(int index) {
		return vars[index].getRange();
	}
	
	/**
	 * Retorna uma coleção com os indices dos pais da variável aleatória actual do iterador.
	 * @return coleção dos indices dos pais
	 */
	public Collection<Integer> getParents(int index) {
		Collection<Integer> parents = new ArrayList<>(3);
		//converter collection de random variables para collection de indices
		for(RandomVariable parent : graph.getParents(vars[index])) {
			parents.add(varsToIndex.get(parent));
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
	
	protected void fillEstimateTable() {
		for(int i = 0; i < vars.length; i++) { //iterar sobre as RVars
			EstimateTable estimate = new EstimateTable(getParentConfigurationCount(i), getRange(i)); //criar estimate table para cada RVar
			for(int j = 0; j < getParentConfigurationCount(i); j++) { //iterar sobre as configura�oes dos pais da RVar
				int Nij = InstanceCounting.getNij(i, j, this);
				for(int k = 0; k < getRange(i); k++) { //iterar sobre o range da RVar	
					int Nijk = InstanceCounting.getNijk(i, j, k, this);
					double estimateValue = (Nijk + 0.5)/(Nij + getRange(i)*0.5);
					estimate.setEstimate(j, k, estimateValue);
				}			
			}			
			estimates[i] = estimate;
		}
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
