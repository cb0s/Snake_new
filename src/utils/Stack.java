package utils;

/**
 * @author Cedric
 * @version 1.0
 * @param <E>
 */
public class Stack<E> {
	
	private int size;
	private ListElement first;
	
	private class ListElement {
		private E data;
		private ListElement next;

		public ListElement(E data) {
			this.data = data;
			size++;
		}

		public void add(E data) {
			if(isEnd()) next = new ListElement(data);
			else next.add(data);
		}
		
		private E getData() {
			return data;
		}
		
		public E removeLast() {
			if(next.isEnd()) {
				E data = next.getData();
				next = null;
				size--;
				return data;
			} else {
				return next.removeLast();
			}
		}
		
		public boolean isEnd() {
			return next == null ? true : false;
		}

		public Object[] getListArray() {
			Object[] o;
			if(!isEnd()) {
				Object[] o2 = next.getListArray();
				o = new Object[o2.length+1];
				for(int i = 0; i < o2.length; i++) o[i] = o2[i];
			} else
				o = new Object[1];
			o[o.length-1] = data;
			return o;
		}
	}
	
	public void add(E dataElement) {
		if (first == null) first = new ListElement(dataElement);
		else first.add(dataElement);
	}
	
	public void removeLast() {
		first.removeLast();
	}
	
	/**
	 * @return Returns an Object Array of all the things put in -> you will need to convert it to the class you want
	 */
	public Object[] toArray() {
		Object[] o = first.getListArray();
		if(o.length > 1) {
			for(int i = 0, j = o.length-1; i < o.length/2; i++, j--) {
				Object helper = o[i];
				o[i] = o[j];
				o[j] = helper;
			}
		}
		return o;
	}
	
	public void loadFromArray(E[] array) {
		first = null;
		for(E e : array) {
			add(e);
		}
	}
	
	/**
	 * @return Returns the actual size of the list
	 */
	public int size() {
		return size;
	}
}
