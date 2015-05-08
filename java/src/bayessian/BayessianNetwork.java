package bayessian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dataset.TransitionDataset;
import graph.DirectedAcyclicGraph;

public class BayessianNetwork<T extends RandomVariable> {
	
	protected DirectedAcyclicGraph<RandomVariable> graph = new DirectedAcyclicGraph<RandomVariable>();
	protected RandomVariable[] vars = null;
	protected Map<RandomVariable, Integer> varsToIndex = null; 
	
	public BayessianNetwork(RandomVariable[] vars, TransitionDataset dataset, Score score) {
		this.vars = Arrays.copyOf(vars, vars.length);
		
		//construir mapa de indices
		this.varsToIndex = new HashMap<>(this.vars.length);
		for(int i = 0; i < this.vars.length; i++) {
			varsToIndex.put(this.vars[i], i);
		}
		
		// implementar geedy-hill para construir dataset
	}

	class BayessianIterator implements Iterator<Integer> {

		protected int currentIndex = 0;
		
		@Override
		public boolean hasNext() {
			return currentIndex < vars.length;
		}

		@Override
		public Integer next() {
			//retornar index actual e incrementar index actual para o próximo
			return Integer.valueOf(currentIndex++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public Collection<Integer> getParents() {
			Collection<Integer> parents = new ArrayList<>(3);
			//converter collection de random variables para collection de indices
			for(RandomVariable parent : graph.getParents(vars[currentIndex])) {
				parents.add(varsToIndex.get(parent));
			}
			
			return parents;
		}
		
		public int getRange() {
			return vars[currentIndex].getRange();
		}
		
		public RandomVariable getVariable() {
			return vars[currentIndex];
		}
		
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
	
	public BayessianIterator iterator() {
		return new BayessianIterator();
	}
	
}
