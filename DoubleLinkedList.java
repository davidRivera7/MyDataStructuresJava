
package dataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements Iterable<T> {
    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;        

        Node(T value, Node<T> prev, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        @Override 
        public String toString() {
            return value + "";
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;
    
    //O(1)
    public boolean add(T e) {
        if (isEmpty()) {
            head = tail = new Node<>(e, null, null);
        } else {
            tail.next = new Node<>(e, tail, null);            
            tail = tail.next;
        }
        size++;
        return true;
    }
        
    //O(n)
    public void add(int index, T element) {
        checkElementIndex(index);
        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size) {
            addLast(element);
            return;
        }
        int i = 0;
        Node<T> pointer = head;
        while (i != index) {
            i++;
            pointer = pointer.next;
        }
        Node<T> newItem = new Node<T>(element, pointer.prev, pointer);
        pointer.prev.next = newItem;
        pointer.prev = newItem;
        size++;
    }
        
    //O(c) = O(n)
    public boolean addAll(Collection<? extends T> c) {
        int prevSize = size;
        for (T t : c) 
            addLast(t);
        
        return prevSize != size;
    }    

    //space complexity: O(c) = O(n)
    //time complexity: O(c + index) = O(c + n) 
    public boolean addAll(int index, Collection<? extends T> c) {                
        checkElementIndex(index);
        if (c == null || c.isEmpty()) return false;
        
        DoubleLinkedList<T> cElements = new DoubleLinkedList<>();
        for (T t : c) 
            cElements.add(t);

        if (isEmpty()) {
            head = cElements.head;
            tail = cElements.tail;
            size = cElements.size;            
            return true;
        }
        
        int prevSize = size;
        if (index == 0) {
            cElements.tail.next = head;
            head.prev = cElements.tail;
            head = cElements.head;            
        } else if (index == size) {
            tail.next = cElements.head;
            cElements.head.prev = tail;
            tail = cElements.tail;
        } else {
            int i = 0;
            Node<T> pointer = head;
            while (i != index) {
                i++;
                pointer = pointer.next;
            }
            cElements.head.prev = pointer.prev;
            pointer.prev.next = cElements.head;

            cElements.tail.next = pointer;
            pointer.prev = cElements.tail;            
        }
        size += cElements.size;
        return prevSize != size;
    }    
    
    //O(1)
    public void addFirst(T e) {
        if (isEmpty()) {
            add(e);
            return;
        }      
        head.prev = new Node<T>(e, null, head);        
        head = head.prev;        
        size++;
    }
    
    //O(1)
    public void addLast(T e) {
        add(e);
    }
    
    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds " + size);
    }

    private boolean isElementIndex(int index) {        
        return index >= 0 && index <= size;
    }
    
    //O(n)
    public void reversed() {                
        Node<T> curr = head;
        head = tail;
        tail = curr;
        while (curr != null) {
            Node<T> next = curr.next;

            Node<T> aux = curr.prev;
            curr.prev = curr.next;
            curr.next = aux;

            curr = next;
        }        
    }    
    
    //O(N) preventing memory leaks in other parts of the code
    public void clear() {
        Node<T> pointer = head;
        while (pointer != null) {
            Node<T> next = pointer.next;            
            pointer.prev = pointer.next = null;            
            pointer.value = null;
            pointer = next;
        }        
        head = tail = null;
        size = 0;
    }
    
    //O(n)
    public boolean contains(Object o) {        
        return indexOf(o) == -1 ? false : true;
    }
    
    //O(c x n)
    public boolean containsAll(Collection<?> c) {
        for (Object t : c) {
            if (!contains(t))
                return false;
        }
        return true;
    }
    
    //O(n)
    public T get(int index) {
        if (index < 0 || index >= size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds " + size);
        
        int i = 0;
        Node<T> pointer = head;
        while (i != index) {            
            i++;
            pointer = pointer.next;
        }
        return pointer.value;
    }
    
    //O(1)
    public T getFirst() {        
        return get(0);
    }

    //O(n)
    public T getLast() {          
        return get(size - 1);
    }        

    //O(n)
    public int indexOf(Object o) {
        if (isEmpty()) 
            return -1;

        int i = 0;
        for (T t : this) {
            if (t == null) {
                if (t == o) return i;
            } else {
                if (t.equals(o)) return i;
            }   
            i++;
        }

        return -1;
    }
    
    public boolean isEmpty() {        
        return size == 0;
    }
    
    public Iterator<T> iterator() {        
        return new Iterator<T>() {
            int i;
            Node<T> pointer = head; 

            @Override
            public boolean hasNext() {                
                return i < size;
            }

            @Override
            public T next() {
                if(!hasNext())
                    throw new NoSuchElementException();

                i++;
                T value = pointer.value;
                pointer = pointer.next;
                return value;
            }            
        };
    }
    
    //O(2n) = O(n)
    public boolean remove(Object o) {                
        return remove(indexOf(o)) == null ? false : true;
    }
    
    //O(n)
    public T remove(int index) {
        if (isEmpty()) 
            throw new RuntimeException("Empty list");

        if (index < 0 || index >= size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds " + size);                                
                   
        int i;
        Node<T> pointer;
        //search from the head of the list
        if (index < size/2) 
            for (i = 0, pointer = head; i != index; i++)  
                pointer = pointer.next;
        //search from the tail of the list
        else 
            for (i = size - 1, pointer = tail; i != index; i--)
                pointer = pointer.prev;

        return remove(pointer);
    }

    //O(1)
    private T remove(Node<T> n) {
        if (n == head) return removeFirst();
        if (n == tail) return removeLast();
        
        //At this point there are at least 3 or more nodes                
        n.prev.next = n.next;
        n.next.prev = n.prev;        
        n.prev = n.next = null;
        T removed = n.value;
        n.value = null;        
        size--;
        return removed;
    }
    
    //O(c X n)
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection is null");
        
        boolean isChanged = false;
        for (Object o : c) {
            if (remove(o)) isChanged = true;
        }
        return isChanged;
    }
    
    //O(1)
    public T removeFirst() {   
        if (isEmpty()) 
            throw new RuntimeException("Empty list");
        
        T removed = head.value;
        head = head.next;
        size--;
        
        if (isEmpty()) tail = null;                
        else head.prev = null;                                
    
        return removed;
    }
    
    //O(1)
    public T removeLast() {    
        if (isEmpty()) 
            throw new RuntimeException("Empty list");
                        
        T removed = tail.value;
        tail = tail.prev;            
        size--;        
        if (isEmpty()) head = null;
        else tail.next = null;

        return removed;
    }

    //O(n)
    public T set(int index, T element) {        
        if (index < 0 || index >= size) 
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds " + size);

        int i = 0;
        Node<T> pointer = head;
        while (i != index) {
            i++;
            pointer = pointer.next;
        }
        T removed = pointer.value;
        pointer.value = element;
        return removed;
    }
    
    public int size() {        
        return size;
    }
    
    //time complexity: O(n)
    //space complexity: O(1), it's because 'subList' doesn't grow, it points to the same references of 'this'
    public DoubleLinkedList<T> subList(int fromIndex, int toIndex) {        
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) 
            throw new IndexOutOfBoundsException("invalid arguments");
                
        DoubleLinkedList<T> subList = new DoubleLinkedList<>(); 

        if (fromIndex == toIndex) {
            subList.head = subList.tail = null;
            return subList;
        }

        int i = 0;
        Node<T> pointer = head;
        while (i != fromIndex) {
            i++;
            pointer = pointer.next;            
        }
        subList.head = pointer;        

        while (i < toIndex - 1) {
            i++;
            pointer = pointer.next;            
        }
        subList.tail = pointer;
        subList.size = toIndex - fromIndex; 
        return subList;
    }

    //O(n)
    @Override
    public String toString() {
        if (isEmpty()) return "[]";        
        if (head == tail) return "[" + head + "]";

        StringBuilder sb = new StringBuilder("[");
        Node<T> pointer = head;
        while (pointer != tail) {
            sb.append(pointer + ", ");
            pointer = pointer.next;
        }

        sb.append(pointer + "]");
        return sb.toString();
    }            
}
