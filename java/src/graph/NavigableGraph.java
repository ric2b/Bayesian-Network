package graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface NavigableGraph<T> {
	
	public Iterator<T> parents(T t) throws NullPointerException, NoSuchElementException;
	public Iterator<T> children(T t) throws NullPointerException, NoSuchElementException;

}