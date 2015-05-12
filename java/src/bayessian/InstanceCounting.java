package bayessian;

import dataset.Dataset;

public class InstanceCounting {

	public static int[] mapJToj(int[] parentRanges, int J){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		int j[] = new int[3];
		int tempVal;
		
		j[2] = J/parentRanges[3];
		tempVal = (J-j[3])/parentRanges[3];
		
		j[1] = tempVal%parentRanges[2];
		j[0] = (tempVal-j[2])/parentRanges[2];
		
		return j;
	}
	
	public static int mapjToJ(int[] parentRanges, int[] j){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		// j should have size 3 and if parent 1 or 2 don't exist their respective positions should have value 0
		// j[i] and parentRanges[i] should be related to the same parent (same order for both arrays)
		return j[2]*parentRanges[1] + j[1]*parentRanges[0] + j[0];
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
