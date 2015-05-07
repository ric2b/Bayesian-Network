package bayessian;

public class RandomVector {
	
	RandomVariable[] vars = null;
	
	public RandomVector(String [] nameRVars, int[] rangeOfRVars) {
		vars = new StaticRandomVariable[nameRVars.length];	
		for(int i = 0; i < nameRVars.length; i++) {
			RandomVariable RVar = new StaticRandomVariable(nameRVars[i], rangeOfRVars[i]);
			vars[i] = RVar;
		}
	}
	
	public RandomVariable[] getRandomVector() {
		return vars;
	}
}
