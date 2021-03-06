package bayessian;

import dataset.Dataset;
import dataset.Sample;

public class InstanceCounting {

	public static int[] mapJToj(int[] parentRanges, int J){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		int j[] = new int[parentRanges.length];
		int jtmp = J;
		int multtmp;// = 1;
		
		for(int i = (j.length-1); i >= 0; i--) {
			
			// c�lculo de j'[i]
			if(i != j.length-1) {
				multtmp = 1;
				for(int k = 0; k <= i; k++) {
					multtmp *= parentRanges[k];
				}
				jtmp %= multtmp;
			}
			// jtmp � j'[i]
			
			multtmp=1;
			for(int k = 0; k < i; k++) {
				multtmp *= parentRanges[k];
			}

			j[i] = jtmp / multtmp; 
		}
		
		return j;
	}
	
	public static int mapjToJ(int[] parentRanges, int[] j){ 
		// 'J' is the configurations using all parents, 'j' is the configuration of a single parent
		// j[i] and parentRanges[i] should be related to the same parent (same order for both arrays)
		
		int J = 0;
		
		for(int i=0; i < (j.length); i++) {
			int rangeMult = 1;
			
			for(int k=0; k < i; k++) {
				rangeMult *= parentRanges[k];
			}
			
			J += j[i] * rangeMult;
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
	
	public static int getjOfProbability(int indexOfVar, Sample sample, int[] parents, int[] d, TransitionBayessianNetwork<? extends RandomVariable> BN) {
		
		if(parents.length == 0) {
			// configuracao vazia
			return 0;
		} 
		
		int[] jArray = new int[parents.length];
		
		for(int i = 0; i < jArray.length; i++) {
			if(BN.isFutureVar(parents[i])){ //pai da RVar que se esta a considerar e do futuro (valor corresponde ao de d)
				jArray[i] = d[parents[i] - BN.varCount];
		
			}
			else{ //pai da RVar que se esta a considerar e do passado - retirar valor do test data set (sample)
				jArray[i] = sample.getValue(parents[i]);
			}
		}
	
		return mapjToJ(BN.getParentRanges(indexOfVar + BN.varCount), jArray);
	}
	
	public static int getjLinhaOfProbability(int indexOfVar, int value, int l, Sample sample, int[] parents, int[] d, TransitionBayessianNetwork<? extends RandomVariable> BN) {
		
		if(parents.length == 0) {
			// configuracao vazia
			return 0;
		} 
		
		int[] jArray = new int[parents.length];
		
		for(int i = 0; i < jArray.length; i++) {
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
	
		return mapjToJ(BN.getParentRanges(l+BN.varCount), jArray);
	}
}
