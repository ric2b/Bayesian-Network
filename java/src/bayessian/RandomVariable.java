package bayessian;

import java.util.Iterator;

public abstract class RandomVariable implements Iterable<Integer> {
	
	protected String name = null;
	protected int range = -1;
	protected int timeInstant = -1;
	
	public RandomVariable(String name, int range, int timeInstant) {
		if(range < 0 || timeInstant < 0) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.range = range;
		this.timeInstant = timeInstant;
	}
	
	public String getName() {
		return name;
	}
	public int getRange() {
		return range;
	}
	
	public int getTimeInstant() {
		return timeInstant;
	}
	
	public abstract Iterator<Integer> iterator();
	
	public abstract int getValueCount();
}
