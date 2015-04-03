package connect4MinimaxIndividual4x4;

//author: Gary Kalmanovich; rights reserved

public class Connect4Position implements InterfacePosition {
    // This implementation is designed for at most 7 columns by 6 rows
    // It packs the entire position into a single long
    // Though, there is some sparseness to the packing
    
    // Rightmost 21=3*7 bits are for storing column sizes. (3 bits accommodates 0..7)
    // Next, going to the left 42=6*7*1 bits are binary for colors. (Either red or yellow) 
    // Finally, the left most bit is for the player
    

    private long position = 0;
    private int nC = 0;
    private int nR = 0;

    Connect4Position( int nC, int nR) {
        position = 0;
        this.nC = nC;
        this.nR = nR;
    }

    Connect4Position( InterfacePosition pos ) {
        position = pos.getRawPosition();
        nC       = pos.nC();
        nR       = pos.nR();
    }

    private int getColumnChipCount( InterfaceIterator iPos ) { // Number of chips in column iC
        return getColumnChipCount( iPos.iC() );
    }
    
    private int getColumnChipCount( int iC ) { // Number of chips in column iC
        // fill this in based on:
        // Rightmost 21=3*7 bits are for storing column sizes. (3 bits accommodates 0..7)
        // Next, going to the left 42=6*7*1 bits are binary for colors. (Either red or yellow) 
        // Finally, the left most bit is for the player
    	return (int) (position & (5L << 3*iC) >>> (3*iC));
    }
    
    @Override public int nC() { return nC; }
    @Override public int nR() { return nR; }

    @Override
    public long getRawPosition() { 
        return position;
    }

    @Override
    public int getColor( InterfaceIterator iPos ) { // 0 if transparent, 1 if red, 2 if yellow
        int  iR_ = iPos.nR()-iPos.iR()-1; // This numbers the rows from the bottom up
        return getColor( iPos.iC(), iR_, getColumnChipCount(iPos) );
    }

    private int getColor( int iC, int iR_, int nColumnChipCount ) { // 0 if transparent, 1 if red, 2 if yellow
        // fill this in based on:
        // Rightmost 21=3*7 bits are for storing column sizes. (3 bits accommodates 0..7)
        // Next, going to the left 42=6*7*1 bits are binary for colors. (Either red or yellow) 
        // Finally, the left most bit is for the player
    	if (iR_ > nColumnChipCount) {
    		return 0;
    	} else {
	    	int posShift = 21;
	    	for (int i = 0; i <= iC; i++) {
	    		posShift += getColumnChipCount(i);
	    	}
	        return (int) ((position & (1L << posShift)) >>> posShift) + 1;
    	}
    }

    @Override
    public void setColor( InterfaceIterator iPos, int color ) { // color is 1 if red, 2 if yellow
        int  iC  = iPos.iC();
        int  iR  = iPos.iR();
        int  iR_ = iPos.nR()-iR-1; // This numbers the rows from the bottom up
        if (        iR_ > getColumnChipCount(iPos)) { 
            System.err.println("Error: This position ("+iC+","+iR+") cannot yet be filled.");
        } else if ( iR_ < getColumnChipCount(iPos)) { 
            System.err.println("Error: This position ("+iC+","+iR+") is already filled.");
        } else {
            // Increment columnSize
            // Set the color (default is color==1)
        	
            //TODO fill this in based on:
            // Rightmost 21=3*7 bits are for storing column sizes. (3 bits accommodates 0..7)
            // Next, going to the left 42=6*7*1 bits are binary for colors. (Either red or yellow) 
            // Finally, the left most bit is for the player
        	int count = getColumnChipCount(iPos);
        	//zero column bits
        	position &= ~(7 << (3*iC));
        	//set new count for that column
        	position &= (count << (3*iC));
        	//change color slot to correct color
        	if (color == 2) {
        		int posShift = 21;
    	    	for (int i = 0; i <= iC; i++) {
    	    		posShift += getColumnChipCount(i);
    	    	}
    	    	position |= (1 << posShift);
        	}
        	
        }
    }

    @Override
    public int isWinner() {
        //      if winner, determine that and return winner, 
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1

        //TODO implement this one for the individual assignment
    	//check verticals for win
    	//for each column
    	for (int columnIndex =0; columnIndex < nC; columnIndex++) {
    		//for each piece in that column with at least three pieces above it
    		int maxHeight = getColumnChipCount(columnIndex);
    		for (int height = 0; maxHeight - height >= 3; height ++) {
    			if (getColor(columnIndex, height, maxHeight) == getColor(columnIndex, height + 1, maxHeight) & 
    					getColor(columnIndex, height, maxHeight) == getColor(columnIndex, height + 2, maxHeight)  &
    					getColor(columnIndex, height, maxHeight) == getColor(columnIndex, height + 3, maxHeight)) {
    				return getColor(columnIndex, height, maxHeight);
    			}
    		}
    	}
    	//check horizontals
    	for (int rowIndex =0; rowIndex < nR; rowIndex++) {
    		for( int columnIndex = 0; columnIndex + 3 < nC; columnIndex++ ) {
    			if (getColor(columnIndex, rowIndex, getColumnChipCount(columnIndex)) != 0) {
    				int maxHeight = getColumnChipCount(columnIndex);
    				if (getColor(columnIndex, rowIndex, maxHeight) == getColor(columnIndex + 1, rowIndex, maxHeight) & 
        					getColor(columnIndex, rowIndex, maxHeight) == getColor(columnIndex + 2, rowIndex, maxHeight)  &
        					getColor(columnIndex, rowIndex, maxHeight) == getColor(columnIndex + 3, rowIndex, maxHeight)) {
        				return getColor(columnIndex, rowIndex, maxHeight);
        			}
    			}
    		}
    	}
    	//check diagonals
    	if (getColor(0,0,getColumnChipCount(0)) == getColor(1, 1, getColumnChipCount(1)) &
    			getColor(0, 0, getColumnChipCount(0)) == getColor(2, 2, getColumnChipCount(2))  &
				getColor(0, 0, getColumnChipCount(0)) == getColor(3, 3, getColumnChipCount(3))) {
    		return getColor(0,0,getColumnChipCount(0));
    	}
    	if (getColor(0,3,getColumnChipCount(0)) == getColor(1, 2, getColumnChipCount(1)) &
    			getColor(0, 3, getColumnChipCount(0)) == getColor(2, 1, getColumnChipCount(2))  &
				getColor(0, 3, getColumnChipCount(0)) == getColor(3, 0, getColumnChipCount(3))) {
    		return getColor(0,3,getColumnChipCount(0));
    	}
    	if ( ((position << 43) >> 43) == 2097152) {
    		return 0;
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
            System.err.println("Error(Connect4Position::setPlayer): iPlayer ("+iPlayer+") out of bounds!!!");
        } else {
            int  currentPlayer = getPlayer();
            if ( currentPlayer != iPlayer ) {
                position ^= 1L << 63;
            }
        }
    }

    @Override
    public int getPlayer() {
        return ((int)(position>>>63))+1;
    }

    @Override
    public int getChipCount() {
        int chipCount = 0;
        for ( int iC = 0; iC < nC(); iC++ ) chipCount += getColumnChipCount(iC);
        return chipCount;
    }

    @Override
    public int isWinner(InterfaceIterator iPos) {
        // Not yet used (You may want to implement/use this for the group assignment)
        return 0/0;
    }

    @Override
    public float valuePosition() {
        // Not yet used
        return 0/0;
    }

    @Override
    public int getChipCount(InterfaceIterator iPos) {
        // Not used yet
        return 0/0;
    }
    
    public void printBoard() {
    	for (int i = nR - 1; i >= 0; i--) {
    		for (int j = 0; j < nC; i++) {
    			System.out.print(getColor(j, i, getColumnChipCount(j)));
    		}
    		System.out.println();
    	}
    	System.out.println("current player:" + Integer.toString(getPlayer()));
    }
}
