package c_minimax;

import java.util.ArrayList;
import java.util.Arrays;

//author: Gary Kalmanovich; rights reserved

public class TicTacToePosition implements InterfacePosition {
    // This implementation packs the position into an int
    // The int is between 0 and 4^9-1. This is not very compact, but efficient
    // "Color" is convention, only integers are returned or set.
    // 0-empty, 1-x (cross), 2-o (nought)
    
    // Rightmost 18=9*2 store color (each 2 bits stores 0,1,2; so it is a little lossy)
    // Leftmost 1 bit stores player (1 or 2)

    private int position;
    
    TicTacToePosition() {
        position = 0;
    }

    TicTacToePosition( InterfacePosition pos ) {
        position = (int) pos.getRawPosition();
    }

    @Override public int nC() { return 3; }
    @Override public int nR() { return 3; }

    @Override
    public long getRawPosition() { 
        return position;
    }

    @Override
    public int getColor( InterfaceIterator iPos ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        int  iC  = iPos.iC();
        int  iR  = iPos.iR();
        return getColor( iC, iR);
    }

    public int getColor( int iC, int iR ) { // 0 if empty, 1 if x(cross), 2 if o(donut)
        // Return a color at iC, iR from position (i.e., decode position)
        // position is simply an int that encodes the position and whose move it is.
    	
    	//Take iC and iR and turn it into a number 1-9
    	int squareNum = (3*iR) + iC;
    	
        return (int) (((position>>>((squareNum)*2)) | ~3L) & 3L);
    }
    
    public int getColor( int squareNum ) { // 0 if empty, 1 if x(cross), 2 if o(donut)
        // Return a color at square referenced by an integer from 0-8 from position (i.e., decode position)
        // position is simply an int that encodes the position and whose move it is.
    	
    	return (int) (((position>>>((squareNum)*2)) | ~3L) & 3L);
    }

    @Override
    public void setColor( InterfaceIterator iPos, int color ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        int  iC  = iPos.iC();
        int  iR  = iPos.iR();
        if ( getColor(iC,iR) != 0 ) { 
            System.err.println("Error: This position ("+iC+","+iR+") is already filled.");
        } else {
            // add a "color" to the current position at iC, iR.
            // position is simply an int that encodes the position and whose move it is.
        	int squareNum = (3*iR) + iC;
        	position |= (((long) color) << (squareNum)*2);
        }
    }

    @Override
    public int isWinner() {
        //      If winner, determine that and return winner, ("winner" is the color enumeration of the winner)
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1

        // using the getColor(0..2,0..2) method, determine if there is a winner
        // a draw, or neither.
        // Return the "color" of the winner if there is a winner; 0 if draw; -1 if in play
        
    	ArrayList<int[]> winningSquaresSets = new ArrayList<int[]>(Arrays.asList(
    			new int[]{0,1,2},
    			new int[]{0,3,6},
    			new int[]{0,4,8},
    			new int[]{6,7,8},
    			new int[]{2,5,8},
    			new int[]{2,4,6}, 
    			new int[]{1,4,7},
    			new int[]{3,4,5}
    			));
    	//Check if there is a winner
    	for (int i=1; i<3; i++) {
    		for (int[] winningSquares: winningSquaresSets) {
    			if (getColor(winningSquares[0]) == i && getColor(winningSquares[1]) == i && getColor(winningSquares[2]) == i) {
    				return i;
    			}
    		}
    	}
    	//check if there is a draw
    	boolean draw = true;
    	for (int i=0; i<9; i++) {
    		if (getColor(i) == 0) {
    			draw = false;
    		}
    	}
    	if (draw == true) {
    		return 0;
    	//else return -1 if game is ongoing
    	} else {
    		return -1;
    	}
    }

    @Override
    public void reset() {
        position = 0;
    }

    @Override
    public void setPlayer(int iPlayer) { // Only 1 or 2 are valid
        if ( !(0<iPlayer && iPlayer<3) ) {
            System.err.println("Error(TicTacToePosition::setPlayer): iPlayer ("+iPlayer+") out of bounds!!!");
        } else {
            int  currentPlayer = getPlayer();
            if ( currentPlayer != iPlayer ) {
                position ^= 1L << 31;
            }
        	System.out.println("current player is player number:" + Integer.toString(getPlayer()));
        }
    }

    @Override
    public int getPlayer() {
        return ((int)(position>>>31))+1;
    }

    @Override
    public float valuePosition() {
        // Not yet used
        return 0;
    }
    
    public String toString() {
    	String state = 	getColor(0) + "|" + getColor(1) + "|" +getColor(2) + "\n" +
    					"------------" +"\n" +
    					getColor(3) + "|" + getColor(4) + "|" +getColor(5) + "\n" +
    					"------------" +"\n" +
    					getColor(6) + "|" + getColor(7) + "|" +getColor(8) + "\n";
		return state;
    	
    }
}
