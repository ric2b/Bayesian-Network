package score;

import java.util.Iterator;

import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public class MDLScore<T> extends LLScore<T> {
	MDLScore(BayessianNetwork<? extends RandomVariable> BN) {
		super(BN);
	}
	
	int getNetworkComplexity() { // PROVAVELMENTE ISTO DEVE SER UM MÉTODO DA BN
		
		int complexity = 0;
		
		Iterator<Integer> BNNodesIterator = BN.iterator();
			//for(int i: BN.iterator()) { Não consigo fazer isto, depois volto a tentar
		while(BNNodesIterator.hasNext()){	
			int i = BNNodesIterator.next();
			//complexity += (i.getRange() - 1) * i.getParentConfigurations();
		}
		
		return complexity;
	}
	
	int getScore() {
		int score = 0;
		
		//score = super.getScore() - log(N)*getNetworkComplexity()/2.0;
		
		return score;
	}
}
