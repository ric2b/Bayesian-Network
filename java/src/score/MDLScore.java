package score;

import graph.DirectedAcyclicGraph;

public class MDLScore<T> extends LLScore<T> {

	MDLScore(DirectedAcyclicGraph<T> BNgraph) {
		super(BNgraph);
	}
	
	int getScore() {
		int score = 0;
		
		//score = super.getScore() - log B/N;
		
		return score;
	}
}
