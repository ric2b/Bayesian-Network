package bayessian;

public class RandomVector {
	
	RandomVariable[] vars = null;
	
	public RandomVector(String [] nameRVars, int[] rangeOfRVars) {
		vars = new StaticRandomVariable[nameRVars.length];	
		for(int i = 0; i < nameRVars.length; i++) {
			RandomVariable rVar = new StaticRandomVariable(nameRVars[i], rangeOfRVars[i]);
			vars[i] = rVar;
		}
	}
	
	public RandomVariable[] toArray() {
		return vars;
	}
	
}
