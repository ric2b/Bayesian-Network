package score;

import dataset.Dataset;
import bayessian.BayessianNetwork;
import bayessian.RandomVariable;

public class LLScore implements Score {
	
	@Override
	public double getScore(BayessianNetwork<? extends RandomVariable> bayessian, Dataset dataset) {
		
		double score = 0;		
		int[][][] Nijks = dataset.getAllCounts(bayessian);
		
		for(int i = 0; i < Nijks.length; i++) {
			for(int j = 0; j < Nijks[i].length; j++) {
				int Nij = 0;
				for(int k = 0; k < Nijks[i][j].length; k++) {
					Nij += Nijks[i][j][k];
				}
				
				if(Nij != 0) {
					for(int k = 0; k < Nijks[i][j].length; k++) {
						if(Nijks[i][j][k] != 0)
							score += Nijks[i][j][k] * Math.log((Nijks[i][j][k]*1.0)/Nij) / Math.log(2);
					}
				}
			}
		}
		
		return score;
	}

}
