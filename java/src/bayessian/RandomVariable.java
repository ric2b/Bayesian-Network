package bayessian;

import java.util.Iterator;

public abstract class RandomVariable implements Iterable<Integer> {

	protected String name = null;
	protected int range = -1;
	protected int timeInstant = -1;
	
	/**
	 * Constructs a random variable with the given name, range and time instant
	 * @param name			random variable name
	 * @param range			random variable range
	 * @param timeInstant	random variable time instant
	 */
	public RandomVariable(String name, int range, int timeInstant) {
		if(range < 0 || timeInstant < 0) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.range = range;
		this.timeInstant = timeInstant;
	}
	
	/**
	 * Returns the name of the random variable
	 * @return name of the random variable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the range of the random variable
	 * @return range of the random variable
	 */
	public int getRange() {
		return range;
	}
	
	/**
	 * Returns the value with the given position of the random variable
	 * @return value with the given position of the random variable
	 */
	public abstract int getValue(int index);
	
	/**
	 * Returns the time instant of the random variable
	 * @return time instant of the random variable
	 */
	public int getTimeInstant() {
		return timeInstant;
	}
	
	/**
	 * Returns iterator for the values of the random variable
	 */
	public abstract Iterator<Integer> iterator();
	
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
