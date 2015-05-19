package bayessian;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import score.Score;
import dataset.Dataset;
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
	protected int varCount = 0; 
	protected static int parentCount = 3;
	
	/**
	 * Builds a Bayessian Network for given random vector and the repective dataset. To build the 
	 * Bayessian Network first it builds a graph of the variables dependencies. It uses the 
	 * Greedy Hill-Climbing Algorithm with the given score function and stop criterion, to build the graph.
	 * After building the graph, is computed an estimate table for each random variable. 
	 * @param vars		random vector with all the random variables
	 * @param dataset	dataset used to build the bayessian network
	 * @param score		score function used in the Greedy Hill-Climbing Algorithm
	 * @param varCount	count of the random variables
	 * @param criterion stopping criterion used in the Greedy Hill-Climbing Algorithm
	 */
	public BayessianNetwork(RandomVariable[] vars, Dataset dataset, Score score, int varCount, StopCriterion criterion) {
		this.varCount = varCount;
		this.vars = Arrays.copyOf(vars, vars.length);
		this.estimates = new EstimateTable[vars.length];	// uma tabela de estimativas por variavel aleatoria
		
		// construir mapa de indices
		this.varsToIndex = new HashMap<>(this.vars.length);
		for(int i = 0; i < this.vars.length; i++) {
			varsToIndex.put(this.vars[i], i);
		}
		
		// começar com o grafo vazio
		graph = new DirectedAcyclicGraph<RandomVariable>(vars);
		
		// construir Bayessian Network
		greedyHillClimbingAlgorithm(dataset, score, criterion);
		
		fillEstimateTable(dataset);
	}
	
	/**
	 * Executes the Greedy Hill-Climbing Algorithm using a given dataset, score and stopping criterion
	 * on the bayessian network object.
	 * @param dataset	dataset used to compute the score of the graph
	 * @param score		score function used
	 * @param criterion	stopping criterion used
	 */
	protected void greedyHillClimbingAlgorithm(Dataset dataset, Score score, StopCriterion criterion) {
		
		// operação sobre o grafo actual que resultou no grafo com melhor score
		EdgeOperation<DirectedAcyclicGraph<RandomVariable>, RandomVariable> operation = null;
		Set<DirectedAcyclicGraph<RandomVariable>> tabuList = new HashSet<>();
		
		double randomBestScore = Double.NEGATIVE_INFINITY;		// melhor score obtido numa iteração
		do {
			if(operation != null) {
				operation.exec(graph);
				operation = null;
			}
			
			randomBestScore = score.getScore(this, dataset);
	
			for(int i = 0; i < vars.length; i++) {
				for(int j = 0; j < vars.length; j++) {
					if(i == j) {
						// um nó não se pode ligar a si próprio
						continue;
					}
					
					if(graph.getParents(vars[i]).contains(vars[j])) {	// testar se j é pai de i
						
						// operacao de remover aresta
						graph.removeEdge(vars[j], vars[i]);
						
						if(!tabuList.contains(this.graph)) { // ignorar grafo se já estiver na tabu list
							double curScore = score.getScore(this, dataset);
							if(curScore > randomBestScore) {
								randomBestScore = curScore;
								operation = new RemoveOperation<>(vars[j], vars[i]);
								
								//adicionar grafo à tabu list
								tabuList.add((DirectedAcyclicGraph<RandomVariable>) this.graph.clone());
							}
						}
						
						// restaurar grafo
						graph.addEdge(vars[j], vars[i]);
						
						// operacao de inverter aresta
						if(flipAssociation(j, i)) {
							if(!tabuList.contains(this.graph)) { // ignorar grafo se já estiver na tabu list
								double curScore = score.getScore(this, dataset);
								if(curScore > randomBestScore) {
									randomBestScore = curScore;
									operation = new FlipOperation<>(vars[j], vars[i]);
									
									//adicionar grafo à tabu list
									tabuList.add((DirectedAcyclicGraph<RandomVariable>) this.graph.clone());
								}
							}
							
							//restaurar grafo
							graph.flipEdge(vars[i], vars[j]);
						}
						
					} else {
						// não existe aresta entre j e i
						if(addAssociation(j, i)) {	// adicionar aresta com teste
							if(!tabuList.contains(this.graph)) { // ignorar grafo se já estiver na tabu list
								double curScore = score.getScore(this, dataset);
								if(curScore > randomBestScore) {
									randomBestScore = curScore;
									operation = new AddOperation<>(vars[j], vars[i]);
									
									//adicionar grafo à tabu list
									tabuList.add((DirectedAcyclicGraph<RandomVariable>) this.graph.clone());
								}
							}
							
							//restaurar grafo
							graph.removeEdge(vars[j], vars[i]);
						}
					}
				}
			}
			
		} while(!criterion.toStop(this, randomBestScore, operation));	
	}
	
	/**
	 * Adds an association between two random variables of the bayessian network's graph.
	 * This method should be overriden by a sub-class in order to define the sub-class association
	 * criterion.
	 * @param srcIndex	index of the source random variable
	 * @param destIndex	index of the destination random variable
	 * @return	true if the association can be done or false otherwise
	 */
	protected boolean addAssociation(int srcIndex, int destIndex) {
		
		if(graph.getParents(vars[destIndex]).size() >= BayessianNetwork.parentCount) {
			// variavel ja tem 3 pais
			return false;
		}
		
		return graph.addEdge(vars[srcIndex], vars[destIndex]);
	}
	
	/**
	 * Flips a current association between two random variables of the bayessian network's graph.
	 * This method should be overriden by a sub-class in order to define the sub-class association
	 * criterion.
	 * @param srcIndex	index of the source random variable of the original association
	 * @param destIndex	index of the destination random variable of the original association
	 * @return	true if the inverse association can be done or false otherwise
	 */
	protected boolean flipAssociation(int srcIndex, int destIndex) {
		
		if(graph.getParents(vars[srcIndex]).size() == BayessianNetwork.parentCount) {	// variavel ja tem 3 pais
			return false;
		}
		
		return graph.flipEdge(vars[srcIndex], vars[destIndex]);
	}
	
	/**
	 * Fills all the estimate tables from a given dataset of each random variable after 
	 * the graph is built. 
	 * @param index	position of the random variable
	 */
	protected void fillEstimateTable(Dataset dataset) {
		for(int i = 0; i < vars.length; i++) { //iterar sobre as RVars
			EstimateTable estimate;
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
		}
	}
	
	/**
	 * Returns the random variable with the specified index in the network.
	 * @param index	position of the random variable
	 * @return random variable with the specified index in the network
	 */
	public RandomVariable getVariable(int index) {
		return vars[index];
	}	
	
	/**
	 * Returns the estimate table of the random variable with the specified index in the network. 
	 * @param index	position of the random variable
	 * @return	the estimate table of the random variable with the specified index in the network.
	 */
	public EstimateTable getEstimate(int index) {
		return estimates[index];
	}
	
	/**
	 * Returns the range of the random variable with the specified index in the network. 
	 * @param index	position of the random variable
	 * @return range of the repective random variable
	 */
	public int getRange(int index) {
		return vars[index].getRange();
	}
	
	/**
	 * Returns all the parents of the random variable with the specified index in the network.
	 * The ordering of the parents is undefined. In case the variable doesn't have parents an
	 * empty array is returned. 
	 * @param index	position of the random variable
	 * @return array of parents of the repective random variable
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
	 * Returns the parent configuration count of the random variable with the specified 
	 * index in the network. In case the variable doesn't have parents the count is 1. 
	 * @param index	position of the random variable
	 * @return the parent configuration count of the repective random variable
	 */
	public int getParentConfigurationCount(int index) {
		Collection<RandomVariable> parents = graph.getParents(vars[index]);
		if(parents.isEmpty()) {
			//configuração vazia
			return 1;
		}
		
		int count = 1;
		for(RandomVariable parent : parents) {
			count *= parent.getRange();
		}		
		return count; 
	}

	/**
	 * Returns the parent ranges of the random variable with the specified index in the network. 
	 * In case the variable doesn't have parents an empty array is returned.  
	 * @param index	position of the random variable
	 * @return array of the parent ranges count of the repective random variable
	 */
	public int[] getParentRanges(int index) {
		Collection<RandomVariable> parents = graph.getParents(vars[index]);
		int[] parentRanges = new int[parents.size()];
		
		int i = 0;
		for(RandomVariable parent : parents) {
			parentRanges[i] = parent.getRange();
			i++;
		}
		
		return parentRanges; 
	}
	
	/**
	 * Iterator used to iterate through all random variables in the network.
	 */
	protected class BayessianIterator implements Iterator<Integer> {

		protected int currentIndex = 0;		// indice da variável aleatória actual
		
		/**
		 * Returns true if the iteration has more random variables. (In other words, returns true 
		 * if next() would return an element rather than throwing an exception.)
		 * @return	true if the iteration has more elements
		 */
		@Override
		public boolean hasNext() {
			return currentIndex < BayessianNetwork.this.vars.length;
		}
		
		/**
		 * Returns the next index of the random variables.
		 * @return next next index of the random variables
		 * @throws NoSuchElementException	when there is no more indexes in the iteration 
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
		 * Not implemented
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}	

	}
	
	/**
	 * Returns an iterator over the indexes of all the random variables in the network.
	 * @return an random variable index iterator
	 */
	public Iterator<Integer> iterator() {
		return new BayessianIterator();
	}
	
	/**
	 * Returns a string with the Bayessian Network format 
	 */
	public String toString() {
		String string = "=== Structure connectivity\n";
		
		for(int i = 0; i < vars.length; i++) {
			string += vars[i].toString() + " : ";
			for(RandomVariable var: graph.getParents(vars[i])) {
				string += var + " ";
			}
			
			if(i != vars.length - 1) {
				string += '\n';
			}
		}
		
		return string;
	}
	
	/**
	 * Returns the total number of random variables in the bayssian network
	 * @return total number of random variables in the bayssian network
	 */
	public int size() {
		return vars.length;
	}
	
}
