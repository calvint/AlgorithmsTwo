package calvinCheckers;

import java.util.ArrayList;

//author: Gary Kalmanovich; rights reserved

public class CheckersPositionCalvin implements InterfacePosition {

    // This implementation is designed for at most 8 columns by 8 rows
    // It packs the entire position into a single long
    
    // Rightmost 51=cap(log2(3^32)) bits are used to store a 32 digit trinary (0..2) number
    // Leftmost 1 bit stores the player 1 or 2

    private long position = 0;
    private int nC = 0;
    private int nR = 0;
    static private long[] powerOfThree = new long[33]; // 32  +1(for player)

    CheckersPositionCalvin( int nC, int nR) {
        //position = 0;
        reset();
        this.nC = nC;
        this.nR = nR;
        if (powerOfThree[0] != 1) setPowerOfThree();
    }

    CheckersPositionCalvin( InterfacePosition pos ) {
        position = pos.getRawPosition();
        nC       = pos.nC();
        nR       = pos.nR();
        if (powerOfThree[0] != 1) setPowerOfThree();
    }
    
    private void setPowerOfThree() { 
        powerOfThree[0] = 1;
        for( int iPow = 1 ; iPow < 33 ; iPow++ ) {
            powerOfThree[iPow] = 3*powerOfThree[iPow-1];
        }
    }

    @Override public int nC() { return nC; }
    @Override public int nR() { return nR; }

    @Override public long getRawPosition() { return position; }

    @Override
    public int getColor( InterfaceIterator iPos ) { // 0 if transparent, 1 if white, 2 if black
        return getColor( iPos.iC(), iPos.iR() );
    }

    public int getColor( int iC, int iR ) { // 0 if transparent, 1 if white, 2 if black
        int retVal = (int) ( ( position / powerOfThree[(8*iR+iC)/2] ) % 3 );
        return (retVal < 0) ? retVal+3 : retVal;
    }

    @Override
    public void setColor( InterfaceIterator iPos, int color ) { // color is 1 if red, 2 if yellow
        // Add the checker to the new square
        setColor( iPos.dC(), iPos.dR(), color ); 
        // Remove the checker from the source square
        setColor(iPos.iC(), iPos.iR(), 0);
        // if move is a jump
        if (Math.abs(iPos.dC()-iPos.iC()) > 1) {
        	//remove the opponents piece in between
        	int columnBetween = iPos.iC() + ((iPos.dC()-iPos.iC())/2);
    		int rowBetween = iPos.iR() + ((iPos.dR()-iPos.iR())/2);
    		setColor(columnBetween, rowBetween, 0);
        }
        
    }

    private void setColor( int iC, int iR, int color ) { // 0 if transparent, 1 if white, 2 if black
        int oldColor = getColor(iC,iR);
        position += (color-oldColor) * powerOfThree[(8*iR+iC)/2];
    }

    @Override
    public void setPlayer(int iPlayer) { // Only 1 or 2 are valid
        if ( !(0<iPlayer && iPlayer<3) ) {
            System.err.println("Error(Connect4Position::setPlayer): iPlayer ("+iPlayer+") out of bounds!!!");
        } else {
        	setColor(64,0,iPlayer);
        }
    }

    @Override
    public int getPlayer() {
    	return getColor(64,0);
    }

    @Override
    public int isWinner(InterfaceIterator iPos) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int isWinner() {
        final int topRow = 0;
        final int bottomRow = nR - 1;

        // Check if Player 1 won
        for (int iC = 1; iC < nC; iC+=2) {
            if (getColor(iC, topRow) == 1)  return 1; // If player 1 made it to the top row, they win
        }

        // Check if Player 2 won
        for (int iC = 0; iC < nC; iC+=2) {
            if (getColor(iC, bottomRow) == 2) return 2; // If player 2 made it to the bottom row, they win
        }
        
        // Find if there is only one player's chips left
        boolean player1ChipsExist = false;
        boolean player2ChipsExist = false;
        
        for (InterfaceIterator iPos = new CheckersIterator(nC, nR); iPos.isInBounds(); iPos.increment()) {
            if (!player1ChipsExist || !player2ChipsExist) {
                if (getColor(iPos) == 1) player1ChipsExist = true;
                else if (getColor(iPos) == 2) player2ChipsExist = true;
            }
        }
        
        if (player1ChipsExist && !player2ChipsExist) {
            // Player 1 wins
            return 1;
        } else if (player2ChipsExist && !player1ChipsExist) {
            // Player 2 wins
            return 2;
        }
        
        // Check for possible moves
        for (InterfaceIterator iPos = new CheckersIterator(nC, nR); iPos.isInBounds(); iPos.increment()) {
            if (possibleMove(iPos)) return -1; // Otherwise the game is still going  
        }
        
        return 0;
    }

    @Override
    public float valuePosition() {
    	float p1PieceCount = 0;
    	float p2PieceCount = 0;
    	InterfaceIterator iPos = new CheckersIterator(8,8);
    	while (iPos.isInBounds()) {
    		int player = this.getColor(iPos);
    		if (player == 1) {
    			p1PieceCount++;
    		} else if (player == 2) {
    			p2PieceCount++;
    		}
        	for (int i = 0; i < 8; i++) {
        		iPos.increment();
        	}
    	}
    	if ((p1PieceCount + p2PieceCount) < 13) {
    		float playerOneWins = 0;
        	float playerTwoWins = 0;
        	float draws = 0;
            for (int i=0; i < 5; i++) {
            	int winner = this.playRandomGame(new CheckersIterator(8, 8), new CheckersPositionCalvin(this));
            	if (winner == 1 ) {
            		playerOneWins++;
            	} else if (winner == 2 ) {
            		playerTwoWins++;
            	} else {
            		draws++;
            	}
            }
            if (playerOneWins==0 & playerTwoWins==0) {
            	return 0;
            } else if (this.getPlayer() == 1) {
            	return playerOneWins/(playerTwoWins + playerOneWins);
            } else {
            	return playerTwoWins/(playerTwoWins + playerOneWins);
            }
    	} else {
	    	if (this.getPlayer() == 1) {
	    		return p1PieceCount/(p1PieceCount+p2PieceCount);
	    	} else {
	    		return p2PieceCount/(p1PieceCount+p2PieceCount);
	    	}
    	}
    }

    private int playRandomGame(CheckersIterator checkersIterator,
			CheckersPositionCalvin checkersPosition) {
		int winner = -1;
		while (winner < 0 ) {
			//find a random move
			boolean jumpPossible = checkersPosition.jumpPossible();
			ArrayList<CheckersIterator> possibleMoves = new ArrayList<CheckersIterator>();
			for ( InterfaceIterator iPos = new CheckersIterator(8,8); iPos.isInBounds(); iPos.increment() ) {
				if (checkersPosition.validMove(iPos, jumpPossible)) {
					CheckersIterator move = new CheckersIterator(8, 8);
					move.set(iPos);
					possibleMoves.add(move);
				}
			}
			CheckersIterator finalMove = possibleMoves.get((int) (Math.random()*possibleMoves.size()) );
			//make the move happen
			checkersPosition.setColor(finalMove, checkersPosition.getPlayer());
			checkersPosition.setPlayer(3-checkersPosition.getPlayer());
			//check to see if the game is over
			winner = checkersPosition.isWinner();
		}
		return winner;
	}

	@Override
    public void reset() {
        // TODO Auto-generated method stub
        position = 2779528540417001L;
    }

    @Override
    public int getChipCount() {
        int chipCount = 0;
        for (int iR = 0; iR < 8; iR++) {
            for (int iC = (iR+1)%2; iC < 8; iC+=2) {
                if (getColor(iC, iR) > 0) chipCount++;
            }
        }
        return chipCount;
    }

    @Override
    public int getChipCount(InterfaceIterator iPos) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public boolean jumpPossible() {
    	// loop over possible moves
    	for ( InterfaceIterator iPos = new CheckersIterator(nC,nR); iPos.isInBounds(); iPos.increment() ) {
    		if (possibleMove(iPos)) {
    			//if move is a jump
    			if (Math.abs(iPos.dC()-iPos.iC()) > 1) {
    				return true;
    			}
    		}		
    	}
		return false;
	}
    
    private boolean possibleMove(InterfaceIterator iPos) {
    	//check if the destination is on the board and return false if it is
    	if (iPos.dC() >= 8 || iPos.dR() >= 8 || iPos.dC() < 0 || iPos.dR() < 0) {
    		return false;
    	}
    	//check if there is a piece at the initial square and that the piece is owned by the current player
    	if (getColor(iPos) == 0 || getColor(iPos) != getPlayer() ) {
    		return false;
    	}
    	//check if the destination is going in the correct direction
    	int multiplier;
    	if (getPlayer() == 2) {
    		multiplier = -1;
    	} else {
    		multiplier = 1;
    	}
    	if ((iPos.dR()-iPos.iR()) * multiplier > 0) {
    		return false;
    	}
    	//check if the destination is already filled and return false if it is
    	if (getColor(iPos.dC(), iPos.dR()) != 0) {
    		return false;
    	}
    	//check to see if iPos represents a jump
    	if (Math.abs(iPos.dC()-iPos.iC()) > 1) {
        	//if it is a jump
        	//check to see if there is an opponent's piece to jump in between initial and destination squares
    		int columnBetween = iPos.iC() + ((iPos.dC()-iPos.iC())/2);
    		int rowBetween = iPos.iR() + ((iPos.dR()-iPos.iR())/2);
    		int opponent = 3-getPlayer();
    		if (getColor(columnBetween, rowBetween) == opponent) {
    			return true;
    		} else {
    			return false;
    		}
    	} else {
	    	//if the move is not a jump
    		return true;
    	}
    }
    
    public boolean validMove( InterfaceIterator iPos, boolean jumpPossible) {
    	if (possibleMove(iPos)) {
    		//check to see if iPos represents a jump
        	if (Math.abs(iPos.dC()-iPos.iC()) > 1) {
            	//if it is a jump
            	return true;
        	} else {
    	    	//if the move is not a jump
        		if (jumpPossible) {
        			return false;
        		} else {
        			return true;
        		}
        	}
    	} else {
    		return false;
    	}
	}
    
    public static void main(String[] args) {// testMe(String[] args) {// Unit test (incomplete)
        CheckersPositionCalvin position = new CheckersPositionCalvin(8,8);
        
        for (int iR = 0; iR < 8; iR++) {
            for (int iC = (iR+1)%2; iC < 8; iC+=2) {
                System.out.println("(" + iC + ", " + iR + ") = " + position.getColor(iC, iR));
            }
        }
        System.out.println("-------------------------");

//        position.setColor(0, 0, 1);
//        position.setColor(1, 0, 0);
//        position.setColor(3, 0, 1);
//        position.setColor(4, 1, 1);
//        position.setPlayer(1);
        for (int iR = 0; iR < 8; iR++) {
            for (int iC = (iR+1)%2; iC < 8; iC+=2) {
                System.out.println("(" + iC + ", " + iR + ") = " + position.getColor(iC, iR));
            }
        }
        System.out.println("player: " + position.getPlayer());
        System.out.println("-------------------------");

        position.setColor(1, 0, 2);
        position.setColor(3, 0, 2);
        position.setColor(5, 0, 2);
        position.setColor(7, 0, 2);
        position.setColor(0, 1, 2);
        position.setColor(2, 1, 2);
        position.setColor(4, 1, 2);
        position.setColor(6, 1, 2);
        position.setColor(1, 2, 2);
        position.setColor(3, 2, 2);
        position.setColor(5, 2, 2);
        position.setColor(7, 2, 2);
        position.setColor(0, 5, 1);
        position.setColor(2, 5, 1);
        position.setColor(4, 5, 1);
        position.setColor(6, 5, 1);
        position.setColor(1, 6, 1);
        position.setColor(3, 6, 1);
        position.setColor(5, 6, 1);
        position.setColor(7, 6, 1);
        position.setColor(0, 7, 1);
        position.setColor(2, 7, 1);
        position.setColor(4, 7, 1);
        position.setColor(6, 7, 1);
        //position.setPlayer(2);
        for (int iR = 0; iR < 8; iR++) {
            for (int iC = (iR+1)%2; iC < 8; iC+=2) {
                System.out.println("(" + iC + ", " + iR + ") = " + position.getColor(iC, iR));
            }
        }
        //position.setPlayer(1);
        System.out.println("player: " + position.getPlayer());
        System.out.println("position: " + position.getRawPosition());
    }
}
