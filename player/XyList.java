/* XyList.java */


package player;

/**                                                                             
 *  The SList class is a singly-linked implementation of the linked list 
 *  abstraction.  SLists are mutable data structures, which can grow at either 
 *  end.                                                                        
                                                                                
 /*  Adapted from the SList class                                                
 *  @author Kathy Yelick and Jonathan Shewchuk                                  
 *  the adaptation XyList uses nodes that contain two ints: x and y             
 **/

public class XyList {

    protected XyListNode head;
    protected XyListNode tail;
    private int size;

    /**                                                                           
     *  SList() constructs an empty list.                                         
     **/

    public XyList() {
	size = 0;
	head = null;
	tail = null;
    }

    /**                                                                           
     *  isEmpty() indicates whether the list is empty.                            
     *  @return true if the list is empty, false otherwise.                       
     **/

    public boolean isEmpty() {
	return size == 0;
    }

    /**                                                                           
     *  length() returns the length of this list.                                 
     *  @return the length of this list.                                          
     **/

    public int length() {
	return size;
    }

    /**                                                                           
     *  insertFront() inserts ints "x" and "y" at the beginning of this list.     
     *  @param x, y the ints to be inserted.                                      
     **/

    protected void insertFront(int x, int y) {
	if (this.isEmpty()) {
	    head = new XyListNode(x, y, head);
	    tail = head;
	}
	else
	    head = new XyListNode(x, y, head);
	size++;
    }

    /**                                                                           
     *  insertEnd() inserts item "x" and "y" at the end of this list.             
     *  @param x, y the ints to be inserted.                                      
     **/

    protected void insertEnd(int x, int y) {
	if (head == null) {
	    tail = new XyListNode(x, y, null);
	    head = tail;
	}
	else {
	    tail.next = new XyListNode(x, y, null);
	    tail = tail.next;
	}
	size++;
    }
}
