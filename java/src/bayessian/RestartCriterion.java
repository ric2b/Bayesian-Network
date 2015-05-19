package bayessian;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import graph.Graph;
import graph.operation.EdgeOperation;

public class RestartCriterion implements StopCriterion {
	
	private int numberOfRandomRestarts = 0;
	private int randomItr = 0;

	private static Random randomOperation = new Random();
	private static Random randomOperationCount = new Random();
	private static Random randomSource = new Random();
	private static Random randomDestiny = new Random();
	
	List<RandomVariable> srcNodesOfBestGraph = new ArrayList<>();
	List<RandomVariable> destNodesOfBestGraph = new ArrayList<>();
	
	private double bestScore = Double.NEGATIVE_INFINITY;		// melhor score obtido em todos os random restarts
	
	/**
	 * Sets the number of random-restarts to be performed.
	 * @param numberOfRandomRestarts number of random-restarts to be performed
	 */
	public RestartCriterion(int numberOfRandomRestarts) {
		this.numberOfRandomRestarts = numberOfRandomRestarts;
	}
	
	/**
	 * Returns true when the random-restart count reaches the value specified in the constructor.
	 * Randomly restarts the current graph of the Bayessian Network when the operation is null.
	 */
	@Override
	public boolean toStop(BayessianNetwork<? extends RandomVariable> BN, double bestScore,
			EdgeOperation<? extends Graph<? extends RandomVariable>, ? extends RandomVariable> operation) {
		
		if(operation == null) {
			if(numberOfRandomRestarts > 0) {
				if(bestScore > this.bestScore) {
					this.bestScore = bestScore;
					srcNodesOfBestGraph.clear();
					destNodesOfBestGraph.clear();
					BN.graph.getEdges(srcNodesOfBestGraph, destNodesOfBestGraph);
				}
		
				this.randomlyRestartGraph(BN);
			}	
		} else {
			return false;
		}
		
		randomItr++;
		if(randomItr > numberOfRandomRestarts) {
			if(numberOfRandomRestarts > 0) {
				BN.graph.removeAllEdges();
				BN.graph.addEdge(srcNodesOfBestGraph, destNodesOfBestGraph);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Randomly restarts the current graph of the Bayessian Network when the operation is null.
	 * @param BN bayssian network with the graph to restart
	 */
	protected void randomlyRestartGraph(BayessianNetwork<? extends RandomVariable> BN) {
		int numberOfRandomIterations = (randomOperationCount.nextInt(BN.vars.length*2)) + 2;
		
		for (int i = 0; i < numberOfRandomIterations; i++) {
			int operToDo = randomOperation.nextInt(2);

			int src = randomSource.nextInt(BN.vars.length);
			int dest = randomDestiny.nextInt(BN.vars.length);
			
			switch(operToDo) {
			case 0: //add
				if(!BN.addAssociation(src, dest)) {
					i--; // operação não conta
				}
				break;
			case 1: //remove
				if(!BN.graph.removeEdge(BN.vars[src], BN.vars[dest])) {
					i--; // operação não conta
				}
				break;
			default:
				break;
			}
		}
		
	}

}
