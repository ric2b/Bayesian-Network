package bayessian.graphcomponents;

import bayessian.RandomVariable;
import graph.components.Node;
import graph.components.NodeFactory;

public class BayesNodeFactory<T extends RandomVariable> implements NodeFactory<T> {

	@Override
	public Node<T> createNode(int index, T t) {
		return (Node<T>) new BayesNode<>(index, t);
	}
	
}