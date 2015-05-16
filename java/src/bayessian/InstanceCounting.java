package bayessian;

import dataset.Dataset;
import dataset.Sample;

public class InstanceCounting {

	public static int[] mapJToj(int[] parentRanges, int J){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		int j[] = new int[parentRanges.length];
		
		if(j.length == 0) {
			j = null;
		} else if(j.length == 1) {
			j[0] = J;
		} else if(j.length == 2) {
			j[0] = J % parentRanges[0];
			j[1] = J / parentRanges[0];
		} else if(j.length == 3) {
			j[2] = J % parentRanges[2];
			int tempValue = (J - j[2]) / parentRanges[2];
			j[1] = tempValue % parentRanges[1];
			j[0] = (tempValue - j[1]) / parentRanges[1];
		}
		
		return j;
	}
	
	public static int mapjToJ(int[] parentRanges, int[] j){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		// j[i] and parentRanges[i] should be related to the same parent (same order for both arrays)
		int[] r = new int[3];
		int[] jtmp = new int[3];
		
		for(int i=0; i<parentRanges.length; i++){
			r[i] = parentRanges[i];
			jtmp[i] = j[i];
		}
		
		return jtmp[0] + jtmp[1]*r[0] + jtmp[2]*r[1];
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
	
	public static int getjOfProbability(int indexOfVar, Sample sample, int[] parents, int[] d, TransitionBayessianNetwork<? extends RandomVariable> BN) {
		int[] jArray = new int[parents.length];
		
		for(int i = 0; i < parents.length; i++) {
			if(BN.isFutureVar(parents[i])){ //pai da RVar que se esta a considerar e do futuro (valor corresponde ao de d)
				jArray[i] = d[parents[i] - BN.varCount];
		
			}
			else{ //pai da RVar que se esta a considerar e do passado - retirar valor do test data set (sample)
				jArray[i] = sample.getValue(parents[i]);
			}
		}
	
		return mapjToJ(BN.getParentRanges(indexOfVar), jArray);
	}
	
	public static int getjLinhaOfProbability(int indexOfVar, int value, int l, Sample sample, int[] parents, int[] d, TransitionBayessianNetwork<? extends RandomVariable> BN) {
		int[] jArray = new int[d.length-1];
		
		for(int i = 0; i < d.length-1; i++) {
			if(BN.isFutureVar(parents[i])){ //pai da RVar e do futuro
				if((parents[i] - BN.varCount) == indexOfVar){ //pai da RVar e a variavel que se esta a considerar
					jArray[i] = value;
				}
				else{ //e um pai do futuro mas nao e a RVar
					jArray[i] = d[parents[i] - BN.varCount];
				}
		
			}
			else{ //pai da RVar e do passado
				jArray[i] = sample.getValue(parents[i]);
			}	
		}
	
		return mapjToJ(BN.getParentRanges(l), jArray);
	}
}
