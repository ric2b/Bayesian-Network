package components;

public class Node {
	
	private Object object = null;
	
	public Node(Object object) {
		this.object = object;
	}
	
	public Object getObject() {
		return object;
	}
	
	public void setObject(Object object) {
		this.object = object;
	}
	
	@Override
	public String toString() {
		return "node[" + object.toString() + "]";
	}
}
