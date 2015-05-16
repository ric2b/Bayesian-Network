import java.util.Iterator;

import bayessian.StaticRandomVariable;
import graph.DirectedAcyclicGraph;

public class graphTest {
	static int N = 3;
	
	
	public static void main(String[] args) {
		DirectedAcyclicGraph<StaticRandomVariable> mygraph = new DirectedAcyclicGraph<>();
		
		StaticRandomVariable[] vars = new StaticRandomVariable[N];
		for(int i = 0; i<N; i++){
			vars[i] = new StaticRandomVariable("Var"+i, i, 0);
			mygraph.addNode(vars[i]);
		}

		System.out.println(mygraph.getNodes());
		
		System.out.println("\n");
		mygraph.addEdge(vars[0], vars[1]);
		mygraph.addEdge(vars[1], vars[2]);
		mygraph.addEdge(vars[2], vars[0]);
		System.out.println("p(0): " + mygraph.getParents(vars[0]));
		System.out.println("p(1): " + mygraph.getParents(vars[1]));
		System.out.println("p(2): " + mygraph.getParents(vars[2]));
		
				
		
	}

}
