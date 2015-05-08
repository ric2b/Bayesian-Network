package bayessian;

import java.util.Arrays;

public class Sample {
	
	private static int length = -1;		// todas as amostram têm o mesmo comprimento; este valor é determinado
										// quando se cria o primeiro objecto da classe Sample
	int[] values = null; 				// vector de inteiros que corresponde a uma linha de uma time slice
	
	public Sample(int numberOfRVars) {
		if(Sample.length != -1 && Sample.length != numberOfRVars) {
			throw new IllegalArgumentException("o numero de variaveis aleatorias já está definido");
		}
		
		Sample.length = numberOfRVars;
		this.values = new int[numberOfRVars];
	}
	
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
	
	public void setValue(int index, int value) {
		values[index] = value;
	}
	
	public int getValue(int index) {
		return values[index];
	}
	
	public int length() {
		return Sample.length;
	}
	
	public static void main(String[] args) {
		Sample s1 = new Sample(new int[10]);
		
		System.out.println("sample 1");
		for(int i = 0; i < Sample.length; i++) {
			System.out.println(s1.getValue(i));
		}
		
		Sample s2 = new Sample(new int[1]);
		
		System.out.println("sample 2");
		for(int i = 0; i < Sample.length; i++) {
			System.out.println(s2.getValue(i));
		}
	}
}