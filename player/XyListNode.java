/* XyListNode.java */


package player;


/**
 *  adapted from SListNode by           
 *  @author Kathy Yelick and Jonathan Shewchuk          
 */

class XyListNode {
    protected int x;
    protected int y;
    protected XyListNode next;
    
    /**
     *  XyListNode() (with two parameters) constructs a list node referencing
     *  the ints x and y.                                                           */
		
    XyListNode(int x, int y) {
	this.x = x;
	this.y = y;
	next = null;
    }
    
    /**                                                                             *  XyListNode() (with three parameters) constructs a list node referencing
     *  the ints x and y, whose next list node is to be "next".
     */
    
    XyListNode(int x, int y, XyListNode next) {
	this.x = x;
	this.y = y;
	this.next = next;
    }
}
    
    