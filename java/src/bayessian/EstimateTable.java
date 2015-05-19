package bayessian;

import java.util.Arrays;

public class EstimateTable {
	
	private double estimates[][] = null;
	
	/**
	 * Constructs an estimate table
	 * @param configCount	number of configurations of the estimate table
	 * @param valueCount	number of values of the estimate table
	 */
	public EstimateTable(int configCount, int valueCount) {
		
		if(configCount <= 0) {
			throw new IllegalArgumentException("o numero de configuracoes tem que ser maior que zero");
		}
		
		if(valueCount <= 0) {
			throw new IllegalArgumentException("o numero de valores tem que ser maior que zero");
		}
		
		this.estimates = new double[configCount][valueCount];
	}
	
	/**
	 * Returns the number of configurations of the estimate table
	 * @return number of configurations of the estimate table
	 */
	public int getConfigCount() {
		return this.estimates.length;
	}
	
	/**
	 * Returns the number of values of the estimate table
	 * @return number of values of the estimate table
	 */
	public int getValueCount() {
		return this.estimates[0].length;
	}
	
	/**
	 * Sets the estimate for the given config and value
	 */
	public void setEstimate(int config, int value, double estimate) {
		//caso o valor de config ou de value nao esteja dentro dos limites do array e feito o throw
		//de uma ArrayIndexOutOfBoundsException
		this.estimates[config][value] = estimate;
	}
	
	/**
	 * Returns the estimate for the given configuration and value
	 * @return estimate for the given configuration and value
	 */
	public double getEstimate(int config, int value) {
		//caso o valor de config ou de value nao esteja dentro dos limites do array e feito o throw
		//de uma ArrayIndexOutOfBoundsException
		return this.estimates[config][value];
	}
	
	/**
	 * Returns the estimate in a 2 dimensional array form
	 * @return 2 dimensional array of the estimates
	 */
	public double[][] getEstimates() {
		return this.estimates;
	}
	
	public String toString() {
		String string = "";
		for(double[] row : estimates) {
			string += Arrays.toString(row) + '\n';
		}
		
		return string;
	}
}