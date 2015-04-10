package connect4individual7x6;

import java.util.HashMap;
import java.util.TreeMap;

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
        return  getColumnChipCount( iPos.iC() );
    }
    
    private int getColumnChipCount( int iC ) { // Number of chips in column iC
        return (int) ((position >>> (3*iC)) & 7);
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
        if ( iR_ >= nColumnChipCount) return 0;
        else return (int) ((position >>> (7*(3+iR_)+iC)) & 1)+1;
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
            position += 1 << 3*iC; // Increment columnSize
            if (color==2) position |= 1L << 7*(3+iR_)+iC; // Now set the color (default is color==1)
        }
    }

    @Override
    public int isWinner(InterfaceIterator iPos) {
        //      if winner, determine that and return winner, 
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1
        InterfaceIterator bounds = new Connect4Iterator(nC,nR);
        int nC = bounds.nC();
        int nR = bounds.nR();
        int iC = iPos.iC();
        int iR = iPos.iR();
        int iR_= nR-iR-1; // iR_ are bottom up as opposed to the way it is displayed which is top down

        int nColumnChipCount = getColumnChipCount( iC );

        int color = getColor(iC,iR_,nColumnChipCount);

        // Check diagonal down-left then up-right
        int iDeltaA, iDeltaB;
        for ( iDeltaA = 1; iDeltaA < 4; iDeltaA++ ) {
            int iCnew  = iC -iDeltaA;
            int iR_new = iR_-iDeltaA;
            if(iCnew < 0 || iR_new < 0) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        for ( iDeltaB = 1; iDeltaB < 5-iDeltaA; iDeltaB++) {
            int iCnew  = iC +iDeltaB;
            int iR_new = iR_+iDeltaB;
            if(iCnew >= nC || iR_new >= nR) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        if ( iDeltaA+iDeltaB == 5 ) 
            return color; // We found a winner, so stop and return

        // Check diagonal down-right then up-left
        for ( iDeltaA = 1; iDeltaA < 4; iDeltaA++ ) {
            int iCnew  = iC +iDeltaA;
            int iR_new = iR_-iDeltaA;
            if(iCnew >= nC || iR_new < 0) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        for ( iDeltaB = 1; iDeltaB < 5-iDeltaA; iDeltaB++) {
            int iCnew  = iC -iDeltaB;
            int iR_new = iR_+iDeltaB;
            if(iCnew < 0 || iR_new >= nR) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        if ( iDeltaA+iDeltaB == 5 ) 
            return color; // We found a winner, so stop and return

        // Check directly left, then right
        for ( iDeltaA = 1; iDeltaA < 4; iDeltaA++ ) {
            int iCnew  = iC -iDeltaA;
            if(iCnew < 0) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        for ( iDeltaB = 1; iDeltaB < 5-iDeltaA; iDeltaB++) {
            int iCnew  = iC +iDeltaB;
            if(iCnew >= nC) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        if ( iDeltaA+iDeltaB == 5 ) 
            return color; // We found a winner, so stop and return

        // Check straight down
        if ( iR_-3 >= 0 ) { // I.e., have to have enough chips below
            boolean isWin = true;
            for ( int iDelta = 1; iDelta < 4; iDelta++ ) {
                int color2 = getColor(iC,iR_-iDelta,nColumnChipCount);
                if (color2!=color) {
                    isWin = false;
                    break; // don't check any further
                }
            }
            if ( isWin ) 
                return color; // We found a winner, so stop and return
        }

        // There are no winners yet. See if there is a tie
        if     (getChipCount()==nC*nR)   return          0; // Tie
        else                             return         -1;
    }
    
    private TreeMap<Integer, Float> getValueMap() {
    	TreeMap<Integer, Float> valueMap = new TreeMap<Integer, Float>();
    	int multiplierPlayerOne = this.getPlayer() == 1 ? 1 : -1;
    	int multiplierPlayerTwo = multiplierPlayerOne *-1;
    	// Value Definitions:
    	float threeInARow 	= 0.00001f;			// Three in a row with last place open
    	float twoInARow 	= 0.00000001f;		// Two in a row with remaining two spaces open
    	float oneInARow		= 0.00000000001f;	// One in a row with three open spaces
    	
    	// Three in a row cases:
    	// 0111
    	valueMap.put(21, threeInARow * multiplierPlayerOne);
    	// 1011
    	valueMap.put(69, threeInARow * multiplierPlayerOne);
    	// 1101
    	valueMap.put(81, threeInARow * multiplierPlayerOne);
    	// 1110
    	valueMap.put(84, threeInARow * multiplierPlayerOne);
    	// 0222
    	valueMap.put(42, threeInARow * multiplierPlayerTwo);
    	// 2022
    	valueMap.put(138, threeInARow * multiplierPlayerTwo);
    	// 2202
    	valueMap.put(162, threeInARow * multiplierPlayerTwo);
    	// 2220
    	valueMap.put(168, threeInARow * multiplierPlayerTwo);
    	
    	//Two in a row cases:
    	// 1100
    	valueMap.put(88, twoInARow * multiplierPlayerOne);
    	// 0110
    	valueMap.put(20, twoInARow * multiplierPlayerOne);
    	// 0011
    	valueMap.put(5, twoInARow * multiplierPlayerOne);
    	// 1010
    	valueMap.put(68, twoInARow * multiplierPlayerOne);
    	// 1001
    	valueMap.put(65, twoInARow * multiplierPlayerOne);
    	// 0101
    	valueMap.put(17, twoInARow * multiplierPlayerOne);
    	// 2200
    	valueMap.put(160, twoInARow * multiplierPlayerTwo);
    	// 0220
    	valueMap.put(40, twoInARow * multiplierPlayerTwo);
    	// 0022
    	valueMap.put(10, twoInARow * multiplierPlayerTwo);
    	// 2020
    	valueMap.put(136, twoInARow * multiplierPlayerTwo);
    	// 2002
    	valueMap.put(130, twoInARow * multiplierPlayerTwo);
    	// 0202
    	valueMap.put(34, twoInARow * multiplierPlayerTwo);
    	
    	// One in a row cases
    	// 1000
    	valueMap.put(64, oneInARow * multiplierPlayerOne);
    	// 0100
    	valueMap.put(16, oneInARow * multiplierPlayerOne);
    	// 0010
    	valueMap.put(4, oneInARow * multiplierPlayerOne);
    	// 0001
    	valueMap.put(1, oneInARow * multiplierPlayerOne);
    	// 2000
    	valueMap.put(128, oneInARow * multiplierPlayerTwo);
    	// 0200
    	valueMap.put(32, oneInARow * multiplierPlayerTwo);
    	// 0020
    	valueMap.put(8, oneInARow * multiplierPlayerTwo);
    	// 0002
    	valueMap.put(2, oneInARow * multiplierPlayerTwo);
    	
    	return valueMap;
    }
    
    public int getScore() {
        //      Returns a score between
    	//		0 and 1 that represents how good
    	//		of a position the current player is in
    	TreeMap<Integer, Float> valueMap = this.getValueMap();
    	int retVal = 0;
    	
    	//add diagonals slanting from bottom left to top right
    	for (int startX = 0; startX < 4; startX++) {
    		for (int startY = 0; startY < 3; startY++) {
    			int first 	= this.getColor(startX,   startY, this.getColumnChipCount(startX));
    			int second 	= this.getColor(startX+1, startY+1, this.getColumnChipCount(startX+1));
    			int third 	= this.getColor(startX+2, startY+2, this.getColumnChipCount(startX+2));
    			int fourth 	= this.getColor(startX+3, startY+3, this.getColumnChipCount(startX+3));
    			int rawPieceStringInt = ((((first) & (second <<2)) & (third <<4)) & (fourth <<6));
    			if (valueMap.containsKey(rawPieceStringInt)) {
    				retVal += valueMap.get(rawPieceStringInt);
    			}
    		}
    	}
    	//add diagonals slanting from bottom right to top left
    	for (int startX = 3; startX < 7; startX++) {
    		for (int startY = 0; startY < 3; startY++) {
    			int first 	= this.getColor(startX,   startY, this.getColumnChipCount(startX));
    			int second 	= this.getColor(startX-1, startY+1, this.getColumnChipCount(startX-1));
    			int third 	= this.getColor(startX-2, startY+2, this.getColumnChipCount(startX-2));
    			int fourth 	= this.getColor(startX-3, startY+3, this.getColumnChipCount(startX-3));
    			int rawPieceStringInt = ((((first) & (second <<2)) & (third <<4)) & (fourth <<6));
    			if (valueMap.containsKey(rawPieceStringInt)) {
    				retVal += valueMap.get(rawPieceStringInt);
    			}
    		}
    	}
    	//add verticals
    	for (int startX = 0; startX < 7; startX++) {
    		for (int startY = 0; startY < 3; startY++) {
    			int first 	= this.getColor(startX,   startY, this.getColumnChipCount(startX));
    			int second 	= this.getColor(startX, startY+1, this.getColumnChipCount(startX));
    			int third 	= this.getColor(startX, startY+2, this.getColumnChipCount(startX));
    			int fourth 	= this.getColor(startX, startY+3, this.getColumnChipCount(startX));
    			int rawPieceStringInt = ((((first) & (second <<2)) & (third <<4)) & (fourth <<6));
    			if (valueMap.containsKey(rawPieceStringInt)) {
    				retVal += valueMap.get(rawPieceStringInt);
    			}
    		}
    	}
    	//add horizontals
    	for (int startX = 0; startX < 4; startX++) {
    		for (int startY = 0; startY < 6; startY++) {
    			int first 	= this.getColor(startX,   startY, this.getColumnChipCount(startX));
    			int second 	= this.getColor(startX+1, startY, this.getColumnChipCount(startX+1));
    			int third 	= this.getColor(startX+2, startY, this.getColumnChipCount(startX+2));
    			int fourth 	= this.getColor(startX+3, startY, this.getColumnChipCount(startX+3));
    			int rawPieceStringInt = ((((first) & (second <<2)) & (third <<4)) & (fourth <<6));
    			if (valueMap.containsKey(rawPieceStringInt)) {
    				retVal += valueMap.get(rawPieceStringInt);
    			}
    		}
    	}
    	return retVal;
    }
    
    public int positionScore(InterfaceIterator iPos) {
        //      if winner, determine that and return winner, 
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1
        InterfaceIterator bounds = new Connect4Iterator(nC,nR);
        int nC = bounds.nC();
        int nR = bounds.nR();
        int iC = iPos.iC();
        int iR = iPos.iR();
        int iR_= nR-iR-1; // iR_ are bottom up as opposed to the way it is displayed which is top down

        int nColumnChipCount = getColumnChipCount( iC );

        int color = getColor(iC,iR_,nColumnChipCount);

        // Check diagonal down-left then up-right
        int iDeltaA, iDeltaB;
        for ( iDeltaA = 1; iDeltaA < 4; iDeltaA++ ) {
            int iCnew  = iC -iDeltaA;
            int iR_new = iR_-iDeltaA;
            if(iCnew < 0 || iR_new < 0) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        for ( iDeltaB = 1; iDeltaB < 5-iDeltaA; iDeltaB++) {
            int iCnew  = iC +iDeltaB;
            int iR_new = iR_+iDeltaB;
            if(iCnew >= nC || iR_new >= nR) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        if ( iDeltaA+iDeltaB == 5 ) 
            return color; // We found a winner, so stop and return

        // Check diagonal down-right then up-left
        for ( iDeltaA = 1; iDeltaA < 4; iDeltaA++ ) {
            int iCnew  = iC +iDeltaA;
            int iR_new = iR_-iDeltaA;
            if(iCnew >= nC || iR_new < 0) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        for ( iDeltaB = 1; iDeltaB < 5-iDeltaA; iDeltaB++) {
            int iCnew  = iC -iDeltaB;
            int iR_new = iR_+iDeltaB;
            if(iCnew < 0 || iR_new >= nR) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_new,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        if ( iDeltaA+iDeltaB == 5 ) 
            return color; // We found a winner, so stop and return

        // Check directly left, then right
        for ( iDeltaA = 1; iDeltaA < 4; iDeltaA++ ) {
            int iCnew  = iC -iDeltaA;
            if(iCnew < 0) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        for ( iDeltaB = 1; iDeltaB < 5-iDeltaA; iDeltaB++) {
            int iCnew  = iC +iDeltaB;
            if(iCnew >= nC) {
                break;
            } else { // We are still inbounds
                int color2 = getColor(iCnew,iR_,getColumnChipCount( iCnew ));
                if (color!=color2) break;
            }
        }
        if ( iDeltaA+iDeltaB == 5 ) 
            return color; // We found a winner, so stop and return

        // Check straight down
        if ( iR_-3 >= 0 ) { // I.e., have to have enough chips below
            boolean isWin = true;
            for ( int iDelta = 1; iDelta < 4; iDelta++ ) {
                int color2 = getColor(iC,iR_-iDelta,nColumnChipCount);
                if (color2!=color) {
                    isWin = false;
                    break; // don't check any further
                }
            }
            if ( isWin ) 
                return color; // We found a winner, so stop and return
        }

        // There are no winners yet. See if there is a tie
        if     (getChipCount()==nC*nR)   return          0; // Tie
        else                             return         -1;
    }
    
    @Override
    public int isWinner() {
        //      if winner, determine that and return winner, 
        //      else if draw, return 0
        //      else if neither winner nor draw, return -1
        boolean isFull = true;
        InterfaceIterator bounds = new Connect4Iterator(nC,nR);
        int nC = bounds.nC();
        int nR = bounds.nR();
        for (     int iC =0; iC <nC; iC++ ) {
            int nColumnChipCount = getColumnChipCount( iC );
            if (nColumnChipCount<nR) isFull = false;
            for ( int iR_=0; iR_<nColumnChipCount; iR_++ ) { // iR_ are bottom up as opposed to the way it is displayed which is top down
                int color = getColor(iC,iR_,nColumnChipCount);

                // Check diagonal up-left
                if ( iC >= 3 && iR_+3 < nR ) { // I.e., have to be at least in the fourth column and have enough space above
                    boolean isWin = true;
                    for ( int iDelta = 1; iDelta < 4; iDelta++ ) {
                        int color2 = getColor(iC-iDelta,iR_+iDelta,getColumnChipCount( iC-iDelta ));
                        if (color2!=color) {
                            isWin = false;
                            break; // don't check any further
                        }
                    }
                    if ( isWin ) 
                        return color; // We found a winner, so stop and return
                }

                // Check diagonal up-right
                if ( iC+3 < nC && iR_+3 < nR ) { // I.e., have three more columns on right and have enough space above
                    boolean isWin = true;
                    for ( int iDelta = 1; iDelta < 4; iDelta++ ) {
                        int color2 = getColor(iC+iDelta,iR_+iDelta,getColumnChipCount( iC+iDelta ));
                        if (color2!=color) {
                            isWin = false;
                            break; // don't check any further
                        }
                    }
                    if ( isWin ) 
                        return color; // We found a winner, so stop and return
                }

                // Check directly right
                if ( iC+3 < nC ) { // I.e., have three more columns on right
                    boolean isWin = true;
                    for ( int iDelta = 1; iDelta < 4; iDelta++ ) {
                        int color2 = getColor(iC+iDelta,iR_,getColumnChipCount( iC+iDelta ));
                        if (color2!=color) {
                            isWin = false;
                            break; // don't check any further
                        }
                    }
                    if ( isWin ) 
                        return color; // We found a winner, so stop and return
                }

                // Check straight up
                if ( iR_+3 < nColumnChipCount ) { // I.e., have to have enough chips above
                    boolean isWin = true;
                    for ( int iDelta = 1; iDelta < 4; iDelta++ ) {
                        int color2 = getColor(iC,iR_+iDelta,nColumnChipCount);
                        if (color2!=color) {
                            isWin = false;
                            break; // don't check any further
                        }
                    }
                    if ( isWin ) 
                        return color; // We found a winner, so stop and return
                }
            }
        }

        // There are no winners yet. See if there is a tie
        if     (isFull         )   return          0; // Tie
        else                       return         -1;
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
    public int getChipCount(InterfaceIterator iPos) { // Chip count in column
        int iC = iPos.iC();
        return getColumnChipCount(iC);
    }

    @Override
    public float valuePosition() {
        // Not yet used
        return 0;
    }

}
