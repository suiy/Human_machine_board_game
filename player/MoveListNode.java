package player;
public class MoveListNode{

    protected MoveListNode next;
    protected Move item;
    public MoveListNode(Move item, MoveListNode next){
	this.next =next;
	this.item = item;

    }
}