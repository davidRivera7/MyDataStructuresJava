//Check out my addAllVersion2() 
package dataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicArray<T> implements Iterable<T> {
    private T[] arr;
    private int size; // size user thinks array is
    private int totalCapacity; // actual array size
    
    @SuppressWarnings("unchecked")
    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) 
            throw new RuntimeException("initialCapacity can't be negative");
            
        totalCapacity = initialCapacity;        
        arr = (T[]) new Object[totalCapacity];
    }
    
    public DynamicArray() { this(10); }

    public void set(int index, T value) {
        if(index < 0 || index >= size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);                               
        
        arr[index] = value;
    }

    public void add(T value) { add(size, value); }

    //Time complexity = O(n)
    //Space complexity = O(n)
    public void add(int index, T value) {
        if (index < 0 || index > size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);                               

        if(isResizedNeeded()) 
            resizeArray();        

        for(int i = size; i > index; i--) 
            arr[i] = arr[i - 1];        

        arr[index] = value;
        size++;
    }

    public void addFirst(T value) {
        add(0, value);
    }

    public void addLast(T value) {
        add(value);
    }

    public void addAll(Collection<T> c) {
        for(T value : c) 
            add(value);        
    }

    //Time complexity = O(n X c)
    public void addAll(int index, Collection<T> c) {
        int i = 0;
        for(T value : c) {
            add(index + i, value);
            i++;
        }        
    }

    //another fastest way to add a collection at the specified index
    //time complexity: O(n + c)
    //space complexity = O(n) = O(size)
    @SuppressWarnings("unchecked")
    public void addAllVersion2(int index, Collection<T> c) {
        if (index < 0 || index > size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);                               

        int length = size - 1 - index + 1;
        T[] elementsMoved = (T[]) new Object[length];

        //copying elements which will moved to the right after inserting elements of 'c'        
        for (int i = 0; i < length; i++) {
            elementsMoved[i] = arr[index + i];
        }
        
        //setting or adding elements of 'c' respectively
        int i = 0;
        for (T t : c) {
            if (index + i < size) {
                set(index + i, t);
            } else {
                add(t);
            }
            i++;
        }    
        
        //reassigning elements moved to the right, setting or adding them respectively
        for (T e : elementsMoved) {
            if (index + i < size) {
                set(index + i, e);
            } else {
                add(e);
            }
            i++;
        }
    }

    public T getFirst() {
        if(isEmpty())
            throw new NoSuchElementException("This DynamicArray is empty");

        return arr[0];
    }

    public T getLast() {
        if(isEmpty())
            throw new NoSuchElementException("This DynamicArray is empty");

        return arr[size - 1];
    }

    public T get(int index) {
        if (index < 0 || index >= size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);        

        return arr[index];
    }

    public boolean isEmpty() {
        return size == 0;
    }    

    public void clear() {
        for(int i = 0; i < size; i++) 
            arr[i] = null;
        
        size = 0;        
    }

    public boolean contains(T o) {        
        return indexOf(o) != -1;
    }
    
    public boolean containsAll(Collection<? extends T> c) {        
        for(T o : c) 
            if(!contains(o))
                return false;    

        return true;
    }
    
    public T remove(int index) {
        if(index < 0 || index > size - 1) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
                                
        T removed = arr[index];        

        for(int i = index; i < size - 1; i++) 
            arr[i] = arr[i + 1];
        
        arr[--size] = null;                                                    
        return removed;
    }

    public boolean remove(T o) {
        int index = indexOf(o);
        if(index == -1)
            return false;

        remove(index);
        return true;
    }

    public boolean removeAll(Collection<? extends T> c) {
        boolean isListchanged = false;

        for(T o : c) 
            if(remove(o))
                isListchanged = true;

        return isListchanged;
    }

    public T removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("This DynamicArray is empty");
                
        T removed = remove(0);        
        return removed;
    }

    public T removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("This DynamicArray is empty");

        T removed = remove(size - 1);        
        return removed;
    }

    public int indexOf(T o) {
        for(int i = 0; i < size; i++) {
            if(o == null) {
                if(arr[i] == null) return i;
            } else {
                if(o.equals(arr[i])) return i; //avoids NullPointerException
            }
        }

        return -1;
    }

    public int size() {
        return size;
    }

    public DynamicArray<T> reversed() {
        DynamicArray<T> reversed = new DynamicArray<>(size);

        for(int i = size -1; i >= 0; i--) 
            reversed.add(arr[i]);            
        
        return reversed;
    }

    @Override
    public String toString() {
        if(isEmpty())
            return "[]";

        StringBuilder sb = new StringBuilder("[");
        
        for(int i = 0; i < size - 1; i++) 
            sb.append(arr[i] + ", ");
        
        sb.append(arr[size - 1] + "]");

        return sb.toString();
    }
        
    //Time complexity = O(n)
    //Space complexity = O(n)
    @SuppressWarnings("unchecked")
    private void resizeArray() {        
        if(totalCapacity == 0) totalCapacity = 1;
        else totalCapacity *= 2;

        T[] resizedArr = (T[]) new Object[totalCapacity];

        for(int i = 0; i < size; i++) 
            resizedArr[i] = arr[i];
        
        arr = resizedArr;
    }

    private boolean isResizedNeeded() {        
        return size == totalCapacity;
    }    

    @Override
    public Iterator<T> iterator() {        
        return new Iterator<T>() {
            int i = -1;

            @Override
            public boolean hasNext() {                
                return i + 1 < size;
            }

            @Override
            public T next() {                   
                return arr[++i];
            }
            
        };
    }    
            
}
