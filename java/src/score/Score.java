package score;

import graph.DirectedAcyclicGraph;

public abstract class Score<T> {
	DirectedAcyclicGraph<T> BNgraph;	
	
	Score(DirectedAcyclicGraph<T> BNgraph) {
		this.BNgraph = BNgraph;
	}
	
	abstract int getScore();
}
