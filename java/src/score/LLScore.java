package score;

import graph.DirectedAcyclicGraph;

public class LLScore<T> extends Score<T> {
	LLScore(DirectedAcyclicGraph<T> BNgraph) {
		super(BNgraph);
	}
	
	int getScore() {
		int score = 0;
		/*
		for(int i: BNIterator) {
			Q = BN.getparentranges();
			for(int j = 0; int < Q; j++) {
				N = i.getRange();
				for(int k; k < N; k++) {
					score += BN.getNijk(i,j,k) * log (BN.getNijk(i,j,k)/BN.getNij(i,j));
				}
			}
		}
		*/
		return score;
	}
}
