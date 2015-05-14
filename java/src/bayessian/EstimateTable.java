package bayessian;

import javax.naming.directory.InvalidAttributeValueException;

public class EstimateTable {
	
	private double estimates[][] = null;
	
	public EstimateTable(int configCount, int valueCount) throws InvalidAttributeValueException {
		
		if(configCount <= 0) {
			throw new InvalidAttributeValueException("o numero de configuracoes tem que ser maior que zero");
		}
		
		if(valueCount <= 0) {
			throw new InvalidAttributeValueException("o numero de valores tem que ser maior que zero");
		}
		
		this.estimates = new double[configCount][valueCount];
	}
	
	public int getConfigCount() {
		return this.estimates.length;
	}
	
	public int getValueCount() {
		return this.estimates[0].length;
	}
	
	public void setEstimate(int config, int value, double estimate) {
		//caso o valor de config ou de value nao esteja dentro dos limites do array e feito o throw
		//de uma ArrayIndexOutOfBoundsException
		this.estimates[config][value] = estimate;
	}
	
	public double getEstimate(int config, int value) {
		//caso o valor de config ou de value nao esteja dentro dos limites do array e feito o throw
		//de uma ArrayIndexOutOfBoundsException
		return this.estimates[config][value];
	}
	
	public double[][] getEstimates() {
		return this.estimates;
	}
}