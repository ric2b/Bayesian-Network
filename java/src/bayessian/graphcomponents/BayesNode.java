package bayessian.graphcomponents;

import javax.naming.directory.InvalidAttributeValueException;

import bayessian.EstimateTable;
import bayessian.RandomVariable;
import graph.components.Node;

public class BayesNode<T extends RandomVariable> extends Node<T> {
	
	protected BayesNode(int index, T t) {
		super(index, t);
	}

	protected EstimateTable estimates = null;
	protected int parentConfigCount = -1;
	
	public double[][] getEstimates() {
		
		//retornar null quando a estimativas ainda não foram calculadas
		//outra possibilidade seria fazer throw de uma exception
		double[][] estimates = null;
		
		if(this.estimates != null) {
			estimates = this.estimates.getEstimates();;
		}
			
		return estimates;
	}
	
	public void setParentConfigCount(int count) {
		this.parentConfigCount = count;
	}
	
	public void setEstimate(int w, int k, double estimate) throws InvalidAttributeValueException {
		if(this.estimates == null) {
			if(this.parentConfigCount <= 0) {
				throw new NullPointerException("numero de configuração dos pais não foi definido");
			}
			
			//criar tabela de estimativas
			this.estimates = new EstimateTable(parentConfigCount, this.t.getValueCount());
		}
		
		this.estimates.setEstimate(w, k, estimate);
	}
}
