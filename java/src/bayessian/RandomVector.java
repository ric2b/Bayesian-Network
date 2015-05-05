package bayessian;

public class RandomVector {
	
	String[] vars = null;
	
	public RandomVector(String [] nameRVars, int numberOfRVars) {
		vars = new String[numberOfRVars];
		for(int i = 0; i < numberOfRVars; i++) {
			vars[i] = nameRVars[i];
		}
	}
	
	public String[] getRandomVector() {
		return vars;
	}
}
