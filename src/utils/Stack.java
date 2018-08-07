package utils;

/**
 * @author Cedric
 * @version 1.2
 * @param <E> The Datatype of the Element you want to store
 * @category util
 */
public class Stack<E> {
	
	private int size;
	private ListElement first;
	
	/**
	 * 
	 * @author Cedric
	 * @version 1.0
	 * 
	 * This is needed to add Elements to the list.
	 * 
	 */
	private class ListElement {
		private E data;
		private ListElement next;

		private ListElement(E data) {
			this.data = data;
			size++;
		}

		private E getData() {
			return data;
		}
		
		private void add(E data) {
			if(isEnd()) next = new ListElement(data);
			else next.add(data);
		}
		
		private E get(int i) {
			if (i == 0) return data;
			else {
				if (!next.isEnd()) return next.get(i-1);
				else return null;
			}
		}
		
		private int getIndex(E data, int passed) {
			if (this.data == data) return passed;
			else {
				if (!next.isEnd()) return next.getIndex(data, passed+1);
				else return -1;
			}
		}
		
		private E removeLast() {
			if(next.isEnd()) {
				E data = next.getData();
				next = null;
				size--;
				return data;
			} else {
				return next.removeLast();
			}
		}
		
		private boolean isEnd() {
			return next == null ? true : false;
		}

		private Object[] getListArray() {
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
	
	/**
	 * Adds a new Element to the list.
	 * 
	 * @param dataElement The Element which should be added.
	 */
	public void add(E dataElement) {
		if (first == null) first = new ListElement(dataElement);
		else first.add(dataElement);
	}
	
	public void addArray(E[] array) {
		if (array != null) for (E e : array) add(e);
	}
	
	public void addStack(Stack<E> stack) {
		if (stack != null) for (int i = 0; i < stack.size(); i++) add(stack.get(i));
	}
	
	/**
	 * Removes the last Element from the list.
	 */
	public void removeLast() {
		first.removeLast();
	}
	
	public E get(int i) {
		if (first != null) return first.get(i);
		else return null;
	}
	
	public int getIndex(E data) {
		if (first != null) return first.getIndex(data, 0);
		else return -1;
	}
	
	/**
	 * Returns all the stored Objects in the List in an Object-Array -> you will need to cast it to <strong>>E<</strong>
	 * 
	 * @return An Object Array of all the things put in -> you will need to convert it to the class you want
	 */
	public Object[] toArray() {
		if (first != null) {
			Object[] o = first.getListArray();
			if(o.length > 1) {
				for(int i = 0, j = o.length-1; i < o.length/2; i++, j--) {
					Object helper = o[i];
					o[i] = o[j];
					o[j] = helper;
				}
			}
			return o;
		} else return null;
	}
	
	/**
	 * Initializes a new List from an Array
	 * 
	 * @param array InputArray to add
	 */
	public void loadFromArray(E[] array) {
		first = null;
		addArray(array);
	}
	
	/**
	 * Returns the actual size of the list.
	 * 
	 * @return The actual size of the list
	 */
	public int size() {
		return size;
	}
}
