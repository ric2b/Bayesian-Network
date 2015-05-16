package bayessian;

import dataset.Dataset;

public class InstanceCounting {

	public static int[] mapJToj(int[] parentRanges, int J){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		int j[] = new int[parentRanges.length];
		
		if(j.length == 1) {
			j[0] = J;
		} else if(j.length == 2) {
			j[0] = J % parentRanges[0];
			j[1] = J / parentRanges[0];
		} else {
			j[2] = J % parentRanges[2];
			int tempValue = (J - j[2]) / parentRanges[2];
			j[1] = tempValue % parentRanges[1];
			j[0] = (tempValue - j[1]) / parentRanges[1];
		}
		
		return j;
	}
	
	public static int mapjToJ(int[] parentRanges, int[] j){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		// j should have size 3 and if parent 1 or 2 don't exist their respective positions should have value 0
		// j[i] and parentRanges[i] should be related to the same parent (same order for both arrays)
		
		int J = 0;
		for (int i = 0; i < parentRanges.length; i++) {
			J += j[i + 1] * parentRanges[i];
		}
		
		return J;
	}
	
	public static int getNijk(int i, int J, int k, BayessianNetwork<? extends RandomVariable> BN, Dataset dataset) {
		return dataset.countValues(i, k, BN.getParents(i), InstanceCounting.mapJToj(BN.getParentRanges(i),J));
	}
	
	public static int getNij(int i, int J, BayessianNetwork<? extends RandomVariable> BN, Dataset dataset) {
		int Nij = 0;
		
		for(int k = 0; k < BN.getRange(i); k++){
			Nij += InstanceCounting.getNijk(i, J, k, BN, dataset);
		}
		
		return Nij;
	}
}
