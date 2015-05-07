package bayessian;

import graph.DirectedAcyclicGraph;
import java.util.Iterator;

public abstract class Score<T> {
	DirectedAcyclicGraph<T> BNgraph;

	Score(DirectedAcyclicGraph<T> BNgraph){
		this.BNgraph = BNgraph;
	}
	
	abstract float getScore();
}

class LLScore<T> extends Score<T>{
	LLScore(DirectedAcyclicGraph<T> BNgraph) {
		super(BNgraph);
	}

	float getScore(){
		return 0;
	}
	
	int[] getParentRange(int index){
		int r[] = new int[4];
		Iterator<T> parentsIterator = BNgraph.parents(index);
		for(int i = 0; i < 3; i++){
			if((parentsIterator.hasNext())){
				r[i] = ((RandomVariable)parentsIterator.next()).getRange(); 
			}
		}
		return r;
	}
	
	int[] mapJ(int index, int J, int[] r){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		int j[] = new int[4];
					
		j[3] = J/r[3];
		j[0] = (J-j[3])/r[3];
		
		j[2] = j[0]%r[2];
		j[1] = (j[0]-j[2])/r[2];
		
		return j;
	}
}

class MDLScore<T> extends LLScore<T>{

	MDLScore(DirectedAcyclicGraph<T> BNgraph) {
		super(BNgraph);
	}

}

