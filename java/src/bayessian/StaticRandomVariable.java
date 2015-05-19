package bayessian;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StaticRandomVariable extends RandomVariable {

	public StaticRandomVariable(String name, int range, int timeInstant) {
		super(name, range, timeInstant);
	}

	public void	setRange(int range) {
		this.range = range;
	}
	
	@Override
	public int getValueCount() {
		return range;
	}
	
	protected class StaticRandomVariableIterator implements Iterator<Integer> {

		protected int currentValue = 0;
		
		@Override
		public boolean hasNext() {
			//testar se o valor actual ja ultrapassou (range - 1)
			return this.currentValue < range;
		}

		@Override
		public Integer next() throws NoSuchElementException {
			if(!this.hasNext()) {
				throw new NoSuchElementException();
			}
			
			//retornar valor actual e passar ao valor seguinte
			return currentValue++;
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException(); 
		}
		
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new StaticRandomVariableIterator();
	}

	@Override
	public int getValue(int index) {
		
		if(index >= range) {
			throw new NoSuchElementException();
		}
		
		return index;
	}
}
