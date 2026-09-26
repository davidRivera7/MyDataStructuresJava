//Check out my interesting functions 'reversed()' and 'addAll(int index, Collection<? extends T> c)' :)
package dataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T> {
    private static class Node<T> {
        T value;
        Node<T> next;        

        Node(T value) {
            this.value = value;               
        }

        @Override
        public String toString() {            
            return (value == null) ? null : value.toString();
        }    
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    // Empty this linked list, O(n)
    public void clear() {
        if(head != null) {
            head = tail = null;
            size = 0;
        }        
    }

    // Return the size of this linked list
    public int size() {
        return size;
    }

    // Is this linked list empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Add an element to the tail of the linked list, O(1)
    public void add(T elem) {
        addLast(elem);
    }

    // Add a node to the tail of the linked list, O(1)
    public void addLast(T elem) {
        if(head == null) {
            head = tail = new Node<>(elem);                    
        } else {
            tail.next = new Node<>(elem);
            tail = tail.next;                                    
        }        
        size++;
    }
    
    // Add an element to the beginning of this linked list, O(1)
    public void addFirst(T elem) {
        if(head == null) {
            head = tail = new Node<>(elem);
        } else {
            Node<T> newHead = new Node<>(elem);
            newHead.next = head;
            head = newHead;
        }
        size++;
    }

    //Inserts value in the Linked List at the specified index, O(n)
    public void addAt(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index + " out of bounds for length " + size);
        }

        if (index == 0) {
            addFirst(value);
            return;
        }

        int i = 0;
        Node<T> previous = head;
        while (i + 1 < index) {
            i++;
            previous = previous.next;
        }

        Node<T> newElement = new Node<>(value);
        newElement.next = previous.next;        
        previous.next = newElement;

        if (newElement.next == null) tail = newElement;

        size++;
    }

    public void addAll(Collection<T> c) {
        for (T t : c) 
            add(t);        
    }

    public void addAll(int index, Collection<? extends T> c) {        
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index + " out of bounds for length " + size);
        }

        if (c == null || c.isEmpty()) {
            return;
        }

        LinkedList<T> cElements = new LinkedList<>();
        for(T t : c) cElements.add(t);
        
        if (isEmpty()) {
            head = cElements.head;
            tail = cElements.tail;
            size = c.size();
            return;
        }

        if (index == 0) {
            cElements.tail.next = head; 
            head = cElements.head;               
        } else {

            int i = 0;
            Node<T> pointer = head;
            while (i + 1 < index) {            
                i++;
                pointer = pointer.next;
            }            
            cElements.tail.next = pointer.next;                        
            pointer.next = cElements.head;

            if (cElements.tail.next == null) tail = cElements.tail;            
        }

        size += c.size();
    }

    // Check the value of the first node if it exists, O(1)
    public T peekFirst() {
        return (head != null) ? head.value : null;
    }

    // Check the value of the last node if it exists, O(1)
    public T peekLast() {
        return (tail != null) ? tail.value : null;
    }

    // Remove the first value at the head of the linked list, O(1)
    public T removeFirst() {
        if(isEmpty()) throw new NoSuchElementException("LinkedList is empty");
                
        T removed = head.value;
        if (size == 1) {            
            clear();            
        } else {
            head = head.next;
            size--;
        }
        return removed;        
    }
    
    //remove the tail value at the tail of the Linked List, O(n)
    public T removeLast() {
        if(isEmpty()) throw new NoSuchElementException("LinkedList is empty");
                
        return removeAt(size - 1);
    }        
    
    // remove Node at position index of the Linked List, O(n)
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index + " out of bounds for length " + size);
        }

        if (index == 0) {
            return removeFirst();            
        }        
        
        int i = 0;
        Node<T> pointer = head;
        while (i + 1 != index) {
            pointer = pointer.next;
            i++;
        }
        
        T valueRemoved = pointer.next.value;

        Node<T> nodeRemoved = pointer.next;
        if (nodeRemoved == tail) tail = pointer;        

        pointer.next = pointer.next.next;        
        size--;        
        return valueRemoved;
    }
    
    // removes the the node of the Linked List that stores obj if exists, O(n)
    public boolean remove(Object obj) {        
        int index = indexOf(obj);
        if (index == -1) return false;        
        removeAt(index);
        return true;
    }
    
    // gets the index of the node that stores obj, O(n)
    public int indexOf(Object obj) {
        int i = 0;
        Node<T> pointer = head;
        while (pointer != null) {
            if (obj == null) {
                if (pointer.value == obj) return i; //in case we have a node that stores a null value                                    
            } else {
                if (pointer.value.equals(obj)) return i;
            }            
            pointer = pointer.next;
            i++;
        }

        return -1;
    }
    
    //checks if obj exists in the Linked List, O(n)
    public boolean contains(Object obj) {        
        return indexOf(obj) == -1 ? false : true;
    }

    //checks if this list contains all of the elements of the specified collection, O(c x n)
    public boolean containsAll(Collection<?> c) {        
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }            
        }
        return true;
    }

    //O(n)
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index + " out of bounds for length " + size);
        }

        int i = 0;
        Node<T> pointer = head;
        while (i != index) {
            i++;
            pointer = pointer.next;
        }

        return pointer.value;
    }

    /**
     * O(c X n)
     * Removes from this list all of its elements that are contained in the specified collection. 
     * @return true if this list changed as a result of the call
     */
    public boolean removeAll(Collection<?> c) {
        boolean isChanged = false;
        for (Object o : c) {
            if (remove(o)) {
                isChanged = true;
            }
        }

        return isChanged;
    }
    
    //space complexity O(1)
    //time complexity O(n)    
    public void reversed() {        
        if (isEmpty()) return;        
        
        Node<T> previous = null;       
        Node<T> current = head;        
        head = tail;
        tail = current;
        
        while (current != null) {
            Node<T> next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }       
    }

    public void set(int index, T value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(index + " out of bounds for length " + size);
        }

        int i = 0;
        Node<T> pointer = head;
        while (i != index) {
            i++;
            pointer = pointer.next;
        }
        pointer.value = value;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");

        Node<T> pointer = head;
        while (pointer.next != null) {
            sb.append(pointer + ", ");
            pointer = pointer.next;
        }

        sb.append(pointer + "]");

        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {        
        return new Iterator<T>() {
            Node<T> pointer = head;

            @Override
            public boolean hasNext() {                
                return pointer != null;
            }

            @Override
            public T next() {                                
                T value = pointer.value;
                pointer = pointer.next;
                return value;
            }
            
        };
    }    
    
}
