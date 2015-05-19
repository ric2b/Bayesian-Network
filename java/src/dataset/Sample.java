package dataset;

import java.util.Arrays;

public class Sample {
	
	private static int length = -1;	// todas as amostram têm o mesmo comprimento; este valor é determinado
												// quando se cria o primeiro objecto da classe Sample
	int[] values = null; 				// vector de inteiros que corresponde a uma linha de uma time slice
	
	/**
	 * Constructs a new sample
	 * @throws UndefinedSampleLengthException	when the sample length was not statically set previously
	 */
	public Sample() throws UndefinedSampleLengthException {
		
		if(Sample.length == -1) {
			// implementar uma excepção nossa para isto
			throw new UndefinedSampleLengthException();
		}
		
		this.values = new int[Sample.length];
	}
	
	/**
	 * Constructs a new sample and fills the sample with the values in the array. If the length of the
	 * Sample class was not specified yet then length of the class is set to the array length. If the lengths
	 * of the Sample class and the array don't match a IllegalArgumentException is thrown.
	 * @param values of the sample
	 */
	public Sample(int[] values) {
		if(values.length == 0) {
			throw new IllegalArgumentException("o array values deve ter um comprimento superior a zero");
		}
		
		if(Sample.length == -1) {
			// o comprimento das samples ainda não está definido
			// usar comprimento do array recebido
			Sample.length = values.length;
		}
		
		// é criado um array de Sample.length posições
		// caso values tenha menos valores que Sample.length os restantes são preenchidos com zeros
		// caso values tenha mais valores que Sample.length apenas são copiados os valores necessários
		this.values = Arrays.copyOfRange(values, 0, Sample.length);
	}
	
	/**
	 * Sets the value of the sample at the position @index with the value @value.
	 * @param index	position of the value
	 * @param value	new value
	 */
	public void setValue(int index, int value) {
		values[index] = value;
	}
	
	/**
	 * Returns the value of the sample at the position @index.
	 * @param index	position of the value
	 * @return	value at the position @index
	 */
	public int getValue(int index) {
		return values[index];
	}
	
	/**
	 * Returns the current length of the class Sample. If the length is not specified -1 is returned.
	 * @return current length of the class Sample. If the length is not specified -1 is returned.
	 */
	public static int length() {
		return Sample.length;
	}
	
	/**
	 * Sets the length of the class Sample.
	 */
	public static void setLength(int length) {
		
		if(length <= 0) {
			throw new IllegalArgumentException("comprimento da saple tme que ser maior que zero");
		}
		
		Sample.length = length;
	}
	
	public String toString() {
		return Arrays.toString(values);
	}
}