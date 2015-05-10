package bayessian;

public class RandomVector {
	
	RandomVariable[] vars = null;
	EstimateTable[] estimates = null;
	
	public RandomVector(String [] nameRVars, int[] rangeOfRVars) {
		vars = new StaticRandomVariable[nameRVars.length];	
		for(int i = 0; i < nameRVars.length; i++) {
			RandomVariable rVar = new StaticRandomVariable(nameRVars[i], rangeOfRVars[i]);
			EstimateTable estimate = new EstimateTable(PARENTCONFIGCOUNT, rangeOfRVars[i]);
			vars[i] = rVar;
			estimates[i] = estimate;
		}
	}
	
	public RandomVariable[] getRandomVector() {
		return vars;
	}
	
	public EstimateTable[] getEstimateTables() {
		return estimates;
	}
}
