package c_minimax;

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

    public int getColor( int iC, int iR ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        // TODO Return a color at iC, iR from position (i.e., decode position)
        // position is simply an int that encodes the position and whose move it is.
        return 0;
    }

    @Override
    public void setColor( InterfaceIterator iPos, int color ) { // 0 if empty, 1 if x(cross), 2 if o(nought)
        int  iC  = iPos.iC();
        int  iR  = iPos.iR();
        if ( getColor(iC,iR) != 0 ) { 
            System.err.println("Error: This position ("+iC+","+iR+") is already filled.");
        } else {
            // TODO add a "color" to the current position at iC, iR.
            // position is simply an int that encodes the position and whose move it is.
        }
    }

    @Override
    public int isWinner() {
        //      If winner, determine that and return winner, ("winner" is the color enumeration of the winner)
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1

        // TODO using the getColor(0..2,0..2) method, determine if there is a winner
        // a draw, or neither.
        // Return the "color" of the winner if there is a winner; 0 if draw; -1 if in play
        
        return -1;
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

}
