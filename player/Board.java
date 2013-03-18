
package player;
import java.util.*;
public class Board{
    
    private int[][] board;
    public final static int EMPTY = 2;
    public final static int BLACK = 0;
    public final static int WHITE = 1;
    protected int[] numPcs;        //That is used to record how many chips of this color is on the board.
    
    
/****************************/
/**consructor of board class**/

    public Board(){
	board= new int[8][8];
	for(int i=0;i<8;i++){
	    for(int j=0;j<8;j++){
		board[i][j]=EMPTY;  //make an EMPTY board
	    }
	}
	numPcs=new int[2];
	numPcs[0]=0;
	numPcs[1]=0;
    }
    
    /**
     * makeMove() updates the game board configuration after making a legal move
     * @param Move m, int color
     **/
    protected void makeMove(Move m, int color){
	if(isLegal(m,color)){
	    if(m.moveKind==m.STEP){    //make a step move
		board[m.x2][m.y2]=EMPTY;
		board[m.x1][m.y1]=color;
	    }
	    if(m.moveKind==m.ADD){    //make an add move
		board[m.x1][m.y1]=color;
                numPcs[color]++;
	    }
	}
    }

/**
genMove() generates a list of all legal moves available to given color. Implements a loop to check through 2d game board array and utilizes isLegal method.
@param int color
@return a list of all legal moves of this color
**/
    protected MoveList genMove(int color){
	MoveList list = new MoveList();
        Move[] currMove = genCurrMove(color);
        Move current;
	for(int j=0;j<8;j++){
	    for(int i=0;i<8;i++){
		if(board[i][j]==EMPTY){
		    if(numPcs[color]<10){
			current = new Move(i,j);
			if(isLegal(current,color)){
			    list.insert(current);
			}
		    }
		    else{
			for(int h=0;h<10;h++){
			    Move old = currMove[h];
			    current = new Move(i,j,old.x1,old.y1);
			    if(isLegal(current,color)){
				list.insert(current);
			    }
			}
		    }
		}
	    }
	}
	return list;
    }
    
    protected Move makeFirstMove(int color){
        Random x = new Random();
	Random y = new Random();
	Move first = new Move(0,0);
	if(numPcs[color]==0){       //make a random move in the goal area
	    int r = x.nextInt();
	    if(color == WHITE){
		if(r<0){
		    first.x1 = 0;
		    first.y1 = -(r%6)+1;
		}
	        else{
		    first.x1 = 7;
		    first.y1 =r%6+1;
		}
	    }
	    if(color==BLACK){
		if(r<0){
		    first.x1 = -(r%6)+1;
		    first.y1 = 0;
		}
		else{
		    first.x1 = r%6+1;
		    first.y1 = 7;
		}
	    }	    
	} 
	else{         //make a random move
	    do{
		first.x1 = x.nextInt(8);
		first.y1 = y.nextInt(8);
	    }while(!isLegal(first, color));
	}
	makeMove(first, color); 
	return first;
    }
    
    /**
       makeMove() updates the game board configuration after deleting a move.
       @param Move m, int color
    **/
    protected void undoMove(Move m, int color){
	if(m.moveKind==m.STEP){        //Delete a step move
	    board[m.x2][m.y2]=color;
	    board[m.x1][m.y1]=EMPTY;
	}
	if(m.moveKind==m.ADD){        //Delete an add move
	    board[m.x1][m.y1]=EMPTY;
	    numPcs[color]--;
	}
    }

/**
dumpBoard() prints out the current state of the game board.
**/
    protected void dumpBoard(){
	System.out.println();
        for(int y = 0; y < 17; y++){     
	    if(y % 2==0){
		for(int x = 0; x <  33; x++){
		    System.out.print("-");
		}
	    }
	    else{
		System.out.print("|");
		for(int x = 0; x < 8; x++) {
		    int contents = board[x][y/2];
		    if(contents == WHITE) {
			System.out.print(" W |");
		    } 
		    else if (contents == BLACK) {
			System.out.print(" B |");
		    } 
		    else {
			System.out.print("   |");
		    }
		}
		System.out.print(" _"+y/2);
	    }
	    System.out.println();
	}
	for(int x = 0; x <  8; x++){
	    System.out.print("  "+x+"_");
	}
	System.out.println();
    }
    
/**
genCurrMove will generate an array of all the existed chips of this color on    the board.
@param int color
@return Move[]
**/
    protected Move[] genCurrMove(int color){
        Move[] currMove = new Move[10];
	int arraySize = 0;
        for(int j=0;j<8;j++){
	    for(int i=0;i<8;i++){
		if(board[i][j]==color){
		    currMove[arraySize]=new Move(i,j);
		    arraySize++;
		}
            }
	}
	return currMove;
    }


/**hasNetwork module**/
/*******************************************************/
/**
 *  hasNetwork() and explore() methods should be implemented in Board class.
 */
    protected boolean hasNetwork(int color) {
	// declare 2D array of Chip objects
	Chip[][] chipArray = new Chip[2][10];
	// fill chipArray with Chip objects from board
	int b = 0; // # of black chips
	int w = 0; // # of white chips
	for( int i = 0; i <= 7; i++ ) {
	    for( int j = 0; j <= 7; j++ ) {
		if( board[i][j] == -1 ) { 
		    // do nothing 
		}
		if( board[i][j] == BLACK ) {
		    // black chip
		    chipArray[0][b] = new Chip(BLACK, i, j);
		    b++;
		}
		if( board[i][j] == WHITE ) {
		    // white chip
		    chipArray[1][w] = new Chip(WHITE, i, j);
		    w++;
		}
	    }
	}
   
	// look at Chips in start row of board
	for( int n = 0; n <= 7; n++ ) {
	    Chip chip = null;
	    if( color == WHITE ) {
		if( board[0][n] == WHITE ) {
		    for( int k = 0; k <= 9; k++ ) {
			if( chipArray[1][k] != null ) {
			    chipArray[1][k].setVisited(false);
			    if( (chipArray[1][k].getX() == 0) && (chipArray[1][k].getY() == n) ) {
				chip = chipArray[1][k];
			    }
			}
		    }
		    if( explore(color, chip, 1, 4, chipArray) ) { return true; }
		}
	    }
	    if( color == BLACK ) {
		if( board[n][0] == BLACK ) {
		    for( int k = 0; k <= 9; k++ ) {
			if( chipArray[0][k] != null ) {
			    chipArray[0][k].setVisited(false);
			    if( (chipArray[0][k].getX() == n) && (chipArray[0][k].getY() == 0) ) {
				chip = chipArray[0][k];
			    }
			}
		    }
		    if( explore(color, chip, 1, 4, chipArray) ) { return true; }
		}
	    }
	}
	return false;
    }

    protected boolean explore(int color, Chip chip, int len, int dir, Chip[][] chipArray) {
	int x = chip.getX();
	int y = chip.getY();  
	
	// mark chip as visited
	chip.setVisited(true);
  
	// check all 8 directions for first neighbor
	for(int horiz = -1; horiz <= 1; horiz++ ) {
	    for(int vert = -1; vert <= 1; vert++ ) {
		if( (horiz == 0) && (vert == 0) ) { continue; }
		int currDir = (horiz+1)*3 + (vert+1);
		Chip neighbor = null;
		x = chip.getX();
		y = chip.getY();
		while( (neighbor == null) && (0 <= x) && (x <= 7) && (0 <= y) && (y <= 7) ) {
		    x += horiz;
		    y += vert;
		    if( (0 <= x) && (x <= 7) && (0 <= y) && (y <= 7) ) {
			if( board[x][y] != -1 ) {
			    for( int i = 0; i < 2; i++ ) {
				for( int j = 0; j < 10; j++ ) {
				    if( chipArray[i][j] != null ) {
					if( (chipArray[i][j].getX() == x) && (chipArray[i][j].getY() == y) ) {
					    neighbor = chipArray[i][j];
					} 
				    } 
				}
			    }
			}
		    }
		}
		if( neighbor == null ) { continue; }
		if( currDir == dir ) { continue; }
		if( neighbor.getColor() != color ) { continue; }
		if( neighbor.getVisited() == true ) { continue; }
		if( color == WHITE ) {
		    if( neighbor.getX() == 0 ) { continue; }
		}
		else { if( neighbor.getY() == 0 ) continue; }
		if( color == WHITE ) {
		    if( neighbor.getX() == 7 ) {
			if( len >= 5 ) {
			    return true;
			}
		    }
		}
		else if( color == BLACK ) {
		    if( neighbor.getY() == 7 ) {
			if( len >= 5 ) {
			    return true;
			}
		    }
		}
		if( explore(color, neighbor, len+1, currDir, chipArray) ) { return true; } 
	    }
	}
  
	// mark chip as unvisited
	chip.setVisited(false);
  
	return false;
    }
/********************************************************/




/**isLegal module**/
/*************************************************************/
    public boolean isLegal(Move m, int color){
    	if ((m.x1==0 || m.x1==7) && (m.y1==0 || m.y1==7))
	    return false;                                //rule 1
	
	if ((color == WHITE)&&((m.y1==0)||(m.y1==7)))
	    return false;
	if ((color == BLACK)&&((m.x1==0)||(m.x1==7)))
	    return false;                              //rule 2
	
	if(!(board[m.x1][m.y1]==EMPTY))
	    return false;                      //rule 3
	
	if(!(isGoodNeighborhood(m, color)))
	    return false;                       //rule 4  
        return true;
	
    }

   // this method determines the status of each neighboring cell of a
    // proposed move, to see if the move makes a cluster of 3
    //
    protected boolean isGoodNeighborhood(Move m, int color) {
	if(m.moveKind == m.STEP){
	    board[m.x2][m.y2]=EMPTY;
	    if(neighbors(m, color) > 1){
		board[m.x2][m.y2]=color;
	        return false;
	    }
	    if(neighbors(m, color)==1) {
		if(isGoodNeighbor(m, color)){
		    board[m.x2][m.y2]=color;
		    return true;
		}
	        else{
		    board[m.x2][m.y2]=color;
		    return false;
		}
	    }
	    board[m.x2][m.y2]=color;
	    return true;
	}
	else{
	    if(neighbors(m, color) > 1)
	        return false;
	    
	    if(neighbors(m, color)==1) {
		if(isGoodNeighbor(m, color))
		    return true;
	        else
		    return false;
	    }
	    return true;
	}
    }
    

    // this method returns the number of neighboring peices of the same
    // color (it sounds totally racist)
    //
    protected int neighbors(Move m, int color) {
	                                   
	int whiteCount=0;   // a count is kept of the colors of any neighboring
	int blackCount=0;   // pieces, for comparison with color (seperate
	//                   counts, in case we need them later...)
	if(m.x1 > 0) {
	    if(m.y1 > 0) {
		if(board[m.x1-1][m.y1-1]==WHITE)
		    whiteCount++;
		else if(board[m.x1-1][m.y1-1]==BLACK)
		    blackCount++;
	    }
	    
	    if(board[m.x1-1][m.y1]==WHITE)
		whiteCount++;
	    else if (board[m.x1-1][m.y1]==BLACK)
		blackCount++;
	    
	    
	    if(m.y1< 7) {
		if(board[m.x1-1][m.y1+1]==WHITE)
		    whiteCount++;
		else if (board[m.x1-1][m.y1+1]==BLACK)
		    blackCount++;
	    }
	}
	
	if(m.x1 < 7) {
	    if(m.y1 > 0) {
		if(board[m.x1+1][m.y1-1]==WHITE)
		    whiteCount++;
		else if(board[m.x1+1][m.y1-1]==BLACK)
		    blackCount++;
	    }
	    
	    if(board[m.x1+1][m.y1]==WHITE)
		whiteCount++;
	    else if (board[m.x1+1][m.y1]==BLACK)
		blackCount++;
	    
	    
	    if(m.y1 < 7) {
		if(board[m.x1+1][m.y1+1]==WHITE)
		    whiteCount++;
		else if (board[m.x1+1][m.y1+1]==BLACK)
		    blackCount++;
	    }
	}
	
	if(m.y1 > 0) {
	    if(board[m.x1][m.y1-1]==WHITE)
		whiteCount++;
	    else if (board[m.x1][m.y1-1]==BLACK)
		blackCount++;
	}
	
	if(m.y1 < 7) {
	    if(board[m.x1][m.y1+1]==WHITE)
		whiteCount++;
	    else if (board[m.x1][m.y1+1]==BLACK)
		blackCount++;
	}
	if(color==WHITE)
	    return whiteCount;
	if(color==BLACK)
	    return blackCount;
	return 0;
    } 


    // this method determines whether a neighboring peice already has a
    // neighbor of the same color
    //
    protected boolean isGoodNeighbor(Move m, int color) {
	
	// an SList(has ints x and y) is made of coordinates of each 
	// neighboring cell of this color, so the counts of its neighbors
	// can be considered     
	//                            
         XyList likeNeighbors = new XyList();
	
	if(m.x1 > 0) {
	    if((m.y1 > 0)&&(board[m.x1-1][m.y1-1]==color)) {
		likeNeighbors.insertEnd(m.x1-1, m.y1-1);
	    }
	    if(board[m.x1-1][m.y1]==color) {
		likeNeighbors.insertEnd(m.x1-1, m.y1);
	    }
	    if((m.y1 < 7)&&(board[m.x1-1][m.y1+1]==color)) {
		likeNeighbors.insertEnd(m.x1-1, m.y1+1);
	    }
	}

	if((m.y1 > 0)&&(board[m.x1][m.y1-1]==color)) {
	    likeNeighbors.insertEnd(m.x1, m.y1-1);
	}

	if((m.y1 < 7)&&(board[m.x1][m.y1+1]==color)) {
	    likeNeighbors.insertEnd(m.x1, m.y1+1);
	}

	if(m.x1 < 7) {
	    if((m.y1 > 0)&&(board[m.x1+1][m.y1-1]==color)) {
		likeNeighbors.insertEnd(m.x1+1, m.y1-1);
	    }
	    if(board[m.x1+1][m.y1]==color) {
		likeNeighbors.insertEnd(m.x1+1, m.y1);
	    }
	    if((m.y1 < 7)&&(board[m.x1+1][m.y1+1]==color)) {
		likeNeighbors.insertEnd(m.x1+1, m.y1+1);
	    }
	}
	
	int count = 0;
	XyListNode current = new XyListNode(0,0);
	current = likeNeighbors.head;
	
	for(int i=0; i < likeNeighbors.length(); i++) {
	    
	    Move maybe = new Move();
	    maybe.x1 = current.x;
	    maybe.y1 = current.y;
	    count = count + neighbors(maybe, color);
	    current = current.next;
	}
	
	if(count > 0)
	    return false;
	else
	    return true;
    }


/**
 * legalMovesRemain() determines if given color has legal add moves remaining.
 */
    protected boolean legalMovesRemain(int color) {
     for( int i = 0; i <= 7; i++ ) {
	 for( int j = 0; j <= 7; j++ ) {
	     Move m = new Move(i, j);
	     if( isLegal(m, color) ) {
		 return true;
	     }
	 }
     }
     return false;
    }

/************************************************************/	



/**ChooseMove module**/
/************************************************************/ 
/**
countPairs() aims to count how many pairs of chips on the board can see each    other.
@param int color
@return int pair
**/
    protected int countPairs(int color){
	Chip[] chipArray = new Chip[10]; 
        int chipIndex = 0;
	int pair = 0;
	Chip currChip;
	for(int j =0;j<8;j++){
	    for(int i=0;i<8;i++){
		if(board[i][j]==color){
		    chipArray[chipIndex]=new Chip(color,i,j);
		    chipIndex++;
		}
	    }
	}
	for(int h=0;h<chipIndex;h++){
	    currChip = chipArray[h];
	    
	    for(int horiz = -1; horiz <= 1; horiz++ ) {
		for(int vert = -1; vert <= 1; vert++ ) {
		    int x = currChip.getX();
		    int y = currChip.getY();
		    x += horiz;
		    y += vert; 
		    if((currChip.getColor()==BLACK)&&((y==0)||(y==7))){
			if(vert==0){continue;}
		    }
		    if((currChip.getColor()==WHITE)&&((x==0)||(x==7))){
			if(horiz==0){continue;}
		    }
		    if( (horiz == 0) && (vert == 0) ) { continue; }
		    whileloop:
		    while( (0 <= x) && (x <= 7) && (0 <= y) && (y <= 7) ) {
			
			if(board[x][y]==EMPTY){
			    x +=horiz;
			    y +=vert;
			    continue;
			}
			else if(board[x][y]==color){
			    
			    for(int z=0;z<h;z++){
				if((chipArray[z].getX()==x)
				   &&(chipArray[z].getY()==y)){
				    if(chipArray[z].getVisited()){
					break whileloop;
				    }
				}
			    }
			    if(((currChip.getX()==0)&&(x==7))
			       || ((currChip.getX()==7)&&(x==0))
			       || ((currChip.getY()==0)&&(y==7))
			       || ((currChip.getY()==7)&&(y==0)))
				{break;}
			    pair ++;
			    break;
			}
			else{
			    break;
			}
		    }
		}
	    }
	    currChip.setVisited(true);
	}
	return pair;
    }  
/*****************************************************/
 
 public static void main (String arg[]) throws Exception{
        Move currMoves;
	/** test Rule 1**/ 
	Board a = new Board();
	Move r11 = new Move(3,0); a.makeMove(r11,BLACK);
        Move r12 = new Move(2,1); a.makeMove(r12,BLACK);
	Move r13 = new Move(5,2); a.makeMove(r13,BLACK);
	Move r14 = new Move(5,3); a.makeMove(r14,BLACK);
	Move r15 = new Move(2,4); a.makeMove(r15,BLACK);
	Move r16 = new Move(6,5); a.makeMove(r16,BLACK);
	Move r17 = new Move(1,6); a.makeMove(r17,BLACK);
        Move r18 = new Move(4,7); a.makeMove(r18,BLACK);
        Move r19 = new Move(2,7); a.makeMove(r19,BLACK);
	Move r20 = new Move(3,3); a.makeMove(r20,BLACK);
	a.dumpBoard();
	Move s1 = new Move(2,6,2,7);a.makeMove(s1,BLACK);
	a.dumpBoard();

        Board b = new Board();
        Move move = new Move(0,0); b.makeMove(move,BLACK);
        Move m1 = new Move(1,1); b.makeMove(m1, BLACK);
        Move m2 = new Move(1,2); b.makeMove(m2, BLACK);
        Move m3 = new Move(2,3); b.makeMove(m3, BLACK);
        Move m4 = new Move(7,1); b.makeMove(m4, BLACK);
        b.dumpBoard();
       
	Board c = new Board();
	c.makeFirstMove(BLACK);
	c.dumpBoard();
	c.makeFirstMove(BLACK);
	c.dumpBoard();
}


 }