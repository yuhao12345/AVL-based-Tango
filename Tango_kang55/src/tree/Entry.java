package tree;

public class Entry {
	private int key;
	private Object value;
	public Entry() {}
	public Entry(int k) {
		key=k;
	}
	public Entry(int k,Object v) {
		key=k;
		value=v;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int k) {
		key=k;
	};
	public Object getValue() {
		return value;
	};
	public void setValue(Object v) {
		value=v;
	};
}