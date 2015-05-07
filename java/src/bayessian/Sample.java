package bayessian;

public class Sample {
	
	int[] values = null; //vector de inteiros que corresponde a uma linha de uma time slice

	public Sample(int size) {
		values = new int[size];
	}
	
	public Sample(int[] valuesArg) {
		values = valuesArg;
	}
	
	public void setValue(int index, int value) {
		values[index] = value;
	}
	
	public int getValue(int index) {
		return values[index];
	}
}