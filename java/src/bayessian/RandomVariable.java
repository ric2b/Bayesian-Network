package bayessian;

import java.util.Iterator;

public abstract class RandomVariable implements Iterable<Integer> {
	
	protected String name = null;
	protected int range = -1;
	
	public String getName() {
		return name;
	}
	public int getRange() {
		return range;
	}
	
	public abstract Iterator<Integer> iterator();
	
	public abstract int getValueCount();
}
