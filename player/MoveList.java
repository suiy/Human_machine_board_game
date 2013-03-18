package player;
/*MoveList is a single-linked list used to store moves*/
public class MoveList{
    protected MoveListNode head,tail;
    protected int size;
    public MoveList(){
	head =null;
	tail = null;
	size = 0;
    }
    protected void insert(Move item){
	if(head==null){
	    tail = new MoveListNode(item,null);
	    head = tail;
	}
	else{
	tail.next = new MoveListNode(item,null);
	tail = tail.next;
	}
    }
}
