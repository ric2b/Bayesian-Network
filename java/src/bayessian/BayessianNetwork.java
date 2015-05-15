package bayessian;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import score.Score;
import dataset.Dataset;

import javax.naming.directory.InvalidAttributeValueException;

import graph.DirectedAcyclicGraph;
import graph.operation.AddOperation;
import graph.operation.EdgeOperation;
import graph.operation.FlipOperation;
import graph.operation.RemoveOperation;

public class BayessianNetwork<T extends RandomVariable> implements Iterable<Integer> {
	
	protected DirectedAcyclicGraph<RandomVariable> graph = null;
	protected RandomVariable[] vars = null;
	protected EstimateTable[] estimates = null;
	protected Map<RandomVariable, Integer> varsToIndex = null; 
	
	/**
	 * 
	 * @param vars
	 * @param dataset
	 * @param score
	 */
	public BayessianNetwork(RandomVariable[] vars, Dataset dataset, Score score) {
		this.vars = Arrays.copyOf(vars, vars.length);
		this.estimates = new EstimateTable[vars.length];	// uma tabela de estimativas por variavel aleatoria
		
		// construir mapa de indices
		this.varsToIndex = new HashMap<>(this.vars.length);
		for(int i = 0; i < this.vars.length; i++) {
			varsToIndex.put(this.vars[i], i);
		}
		
		// construir Bayessian Network
		greedyHillClimbingAlgorithm(dataset, score);
	}
	
	protected void greedyHillClimbingAlgorithm(Dataset dataset, Score score) {
		
		// começar com o grafo vazio
		graph = new DirectedAcyclicGraph<RandomVariable>();
		
		// operação sobre o grafo actual que resultou no grafo com melhor score
		EdgeOperation<DirectedAcyclicGraph<RandomVariable>, RandomVariable> operation = null;
		int bestScore = -1;		// melhor score obtido numa iteração
		
		do {
			if(operation != null) {
				operation.exec(graph);
			}
			
			bestScore = score.getScore(this, dataset);
			
			for(int i = 0; i < vars.length; i++) {
				for(int j = 0; j < vars.length; j++) {
					if(i == j) {
						// um nó não se pode ligar a si próprio
						continue;
					}
					
					if(graph.getParents(vars[i]).contains(vars[j])) {	// testar se j é pai de i
						
						// operacao de remover aresta
						graph.removeEdge(vars[j], vars[i]);
						int curScore = score.getScore(this, dataset);
						if(curScore > bestScore) {
							bestScore = curScore;
							operation = new RemoveOperation<>(vars[j], vars[i]);
						}
						// restaurar grafo
						graph.addEdge(vars[j], vars[i]);
						
						// operacao de inverter aresta
						if(graph.flipEdge(vars[j], vars[i])) {
							curScore = score.getScore(this, dataset);
							if(curScore > bestScore) {
								bestScore = curScore;
								operation = new FlipOperation<>(vars[j], vars[i]);
							}
							//restaurar grafo
							graph.flipEdge(vars[i], vars[j]);
						}
						
					} else {
						// não existe aresta entre j e i
						if(addAssociation(j, i)) {	// adicionar aresta com teste
							int curScore = score.getScore(this, dataset);
							if(curScore > bestScore) {
								bestScore = curScore;
								operation = new AddOperation<>(vars[j], vars[i]);
							}
							//restaurar grafo
							graph.removeEdge(vars[j], vars[i]);
						}
					}
				}
			}
			
			
		} while(operation != null);
		
	}
	
	protected boolean addAssociation(int srcIndex, int destIndex) {
		return graph.addEdge(vars[srcIndex], vars[destIndex]);
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
	
	protected void fillEstimateTable(Dataset dataset) {
		for(int i = 0; i < vars.length; i++) { //iterar sobre as RVars
			EstimateTable estimate;
			try {
				estimate = new EstimateTable(getParentConfigurationCount(i), getRange(i)); //criar estimate table para cada RVar	
				for(int j = 0; j < getParentConfigurationCount(i); j++) { //iterar sobre as configura�oes dos pais da RVar
					int Nij = InstanceCounting.getNij(i, j, this, dataset);
					for(int k = 0; k < getRange(i); k++) { //iterar sobre o range da RVar	
						int Nijk = InstanceCounting.getNijk(i, j, k, this, dataset);
						double estimateValue = (Nijk + 0.5)/(Nij + getRange(i)*0.5);
						estimate.setEstimate(j, k, estimateValue);
					}			
				}			
				estimates[i] = estimate;
			} catch (InvalidAttributeValueException e) {
				e.printStackTrace();
			} 	
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
