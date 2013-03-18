 /* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */


public class MachinePlayer extends Player {
public final static int LARGE = 100;
private int compcolor;
private int oppcolor;
private int searchDepth;
private Board board;
 
 // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
      compcolor = color;
      oppcolor = opposite(compcolor);
      searchDepth = 3;
      board = new Board();
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
      compcolor = color;
      oppcolor = opposite(compcolor);
      board = new Board();
      this.searchDepth = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
      Move returnMove;
      if( (board.legalMovesRemain(compcolor) == false) && (board.numPcs[compcolor]<10) ) {
        Move quit = new Move();
        return quit;
      }
      if(board.numPcs[compcolor]>=10){ //reduce the searchdepth of a stepmove
	  searchDepth = 2;
      }
      returnMove = compMove(compcolor,searchDepth);
      board.makeMove(returnMove,compcolor);
      return returnMove;
  } 

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
      if(board.isLegal(m,oppcolor)){
	      board.makeMove(m,oppcolor);
	      return true;
	  }
	  else{
	      return false;
	  }
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
    public boolean forceMove(Move m) {
	if(board.isLegal(m,compcolor)){
	    board.makeMove(m,compcolor);
	    return true;
	}
	else{
	    return false;
	} 
    }


    public Move compMove(int color, int searchDepth) {
	int maxScore = -LARGE;
	Move maxMove  = null;
	MoveIterator it;
	int currScore;
	Move currMove;
	int oppcolor = opposite(color);

	if (board.numPcs[color] < 3){
	    maxMove = board.makeFirstMove(color);
	}
	else{
	    it = new MoveIterator(color,board);
	    while (( currMove = it.getNext()) != null) {
		currScore = scoreMove(currMove, color, 1, maxScore);
		if (currScore > maxScore) {
		    maxScore = currScore;
		    maxMove = currMove;
		}
	    }
	    if(maxMove == null){  //it will prevent opponent from winning.
		it = new MoveIterator(color,board);
		int minPair = board.countPairs(oppcolor);
		int currPair = LARGE;
		maxMove = it.legalmove.head.item;
		while((currMove = it.getNext())!=null){
		    board.makeMove(currMove,color);
		    currPair = board.countPairs(oppcolor);
		    if(currPair < minPair){
			minPair = currPair;
			maxMove = currMove;
		    }
		    board.undoMove(currMove,color);
		}
	    }
	}
	return maxMove;
    }

    private int scoreMove(Move m, int color, int currDepth, int cutoff) {
	board.makeMove(m, color);
	int opponent = opposite(color);
	int retval;
	
	if (currDepth >= searchDepth) {
	    retval = eval(color);   // eval is higher if pos is good for this color
	    board.undoMove(m, color);
	    return retval;
	}
	
	if (board.hasNetwork(color)  &&  !board.hasNetwork(opponent)) {
	    board.undoMove(m, color);
	    return LARGE;
	}
	retval = -tryAll(opponent, currDepth, cutoff);
	board.undoMove(m, color);
	return retval;
    }

    private int tryAll(int opponent, int currDepth, int cutoff) {
	int currVal, maxVal = -LARGE;
	MoveIterator it = new MoveIterator(opponent,board);
	Move currMove;
	int currSum = 0;
	
	while ((currMove = it.getNext()) != null) {
	    currVal = scoreMove(currMove, opponent, currDepth+1, maxVal);
	    if (currVal > maxVal){ 
		maxVal = currVal;
		if (maxVal >= -cutoff)
		    break;
	    }
	}
	return maxVal;
    }
    
    private int opposite(int color){
	if(color == board.BLACK)
	    return board.WHITE;
	else if (color == board.WHITE)
	    return board.BLACK;
	else
	    return board.EMPTY;
    }
    
    private int eval(int color){
	int opponent = opposite(color);
	int score=0;
	if(board.hasNetwork(color)&&!board.hasNetwork(opponent)){
	    return LARGE;
	}
	else if(board.hasNetwork(opponent)&&!board.hasNetwork(color)){
	    return -LARGE;
	}
	else{
	    score=board.countPairs(color)-board.countPairs(opponent);
	    Move[] moveArray = board.genCurrMove(color);
	    for(int i =0;i<board.numPcs[color];i++){
		if((moveArray[i].x1==0) || (moveArray[i].x1==7) || (moveArray[i].y1==0) || (moveArray[i].y1==7)){
		    score++;
		    break;
		}
	    }
            return score;
	}
    }
}
