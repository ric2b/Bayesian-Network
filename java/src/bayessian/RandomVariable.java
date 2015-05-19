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
	
	public abstract int getValue(int index);
	
	public int getTimeInstant() {
		return timeInstant;
	}
	
	public abstract Iterator<Integer> iterator();
	
	public abstract int getValueCount();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + range;
		result = prime * result + timeInstant;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(!(obj instanceof RandomVariable))
			return false;
		
		RandomVariable other = (RandomVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (range != other.range)
			return false;
		if (timeInstant != other.timeInstant)
			return false;
		return true;
	}
	
	public String toString() {
		return name; 
	}
	
}
