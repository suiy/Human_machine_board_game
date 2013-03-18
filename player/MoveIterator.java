package player;

/*MoveIterator will store all the legal moves that this color can make on the current board as a list.*/
public class MoveIterator{

    protected MoveList legalmove;
    protected MoveListNode node;
    protected Board board;
    public MoveIterator(int color,Board board){
	this.board = board;
        legalmove =board.genMove(color);
        node = legalmove.head;
    }
/**when we call getNext(), it will return the next item in the legalMove list
@return if the list is empty, it will return null.otherwise it will return the next move in the list.
*/ 
    protected Move getNext(){
	Move returnMove;
	if (node == null){
	    return null;
	}
	else{
	    returnMove = node.item;
            node = node.next;
            return returnMove;
	}
    }
    
}
