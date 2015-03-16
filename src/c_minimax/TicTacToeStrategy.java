package c_minimax;

import java.util.ArrayList;
import java.util.Collections;

//author: Gary Kalmanovich; rights reserved

public class TicTacToeStrategy implements InterfaceStrategy {
    @Override
    public void getBestMove(InterfacePosition position, InterfaceSearchInfo context) {
        // Note, return information is embedded in context

        int player = position.getPlayer();
        int opponent = 3-player; // There are two players, 1 and 2.
        for ( InterfaceIterator iPos = new TicTacToeIterator(); iPos.isInBounds(); iPos.increment() ) {
            InterfacePosition posNew = new TicTacToePosition(position);
            if (posNew.getColor(iPos) == 0) { // This is a free spot
                posNew.setColor(iPos, player);
                int isWin = posNew.isWinner();
                float score = 0;
                if (isWin == player) {
                	score = 1;
                } else if (isWin > 0) {
                	score = -1;
                } else if (isWin == 0) {
                	score = 0;
                } else {
                	score = this.minimax(posNew, player);
                }
                if (context.getBestScoreSoFar() <  score ) {
                    context.setBestMoveSoFar(iPos, score );
                }
            }
        }
    }

    private float minimax(InterfacePosition position, int player) {
    	int multiplier = -1;
    	if (player == 1) {
    		multiplier = 1;
    	}
    	int opponent = 3-player;
    	int isWin = position.isWinner();
    	if (isWin == player) {
    		return 1*multiplier;
    	} else if (isWin == opponent) {
    		return -1*multiplier;
    	} else if (isWin == 0) {
    		return 0;
    	} else {
    		ArrayList<Float> possibleScores = new ArrayList<Float>();
    		for ( InterfaceIterator iPos = new TicTacToeIterator(); iPos.isInBounds(); iPos.increment() ) {
                InterfacePosition posNew = new TicTacToePosition(position);
                if (posNew.getColor(iPos) == 0) { // This is a free spot
                	posNew.setColor(iPos, opponent);
                	possibleScores.add(minimax(posNew, opponent));
                }
            }
    		if (player == 1) {
    			return Collections.min(possibleScores);
    		} else {
    			return Collections.max(possibleScores);
    		}
    	}
	}

	@Override
    public void setContext(InterfaceSearchInfo strategyContext) {
        // Not used in this strategy
    }

    @Override
    public InterfaceSearchInfo getContext() {
        // Not used in this strategy
        return null;
    }
}


class TicTacToeSearchInfo implements InterfaceSearchInfo {

    InterfaceIterator bestMoveSoFar  = null;
    float             bestScoreSoFar = Float.NEGATIVE_INFINITY;

    @Override
    public InterfaceIterator getBestMoveSoFar() {
        return bestMoveSoFar;
    }

    @Override
    public float getBestScoreSoFar() {
        return bestScoreSoFar;
    }

    @Override
    public void setBestMoveSoFar(InterfaceIterator newMove, float newScore) {
        bestMoveSoFar  = new TicTacToeIterator(newMove);
        bestScoreSoFar = newScore;
    }

    @Override
    public int getMinDepthSearchForThisPos() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setMinDepthSearchForThisPos(int minDepth) {
        // Not used in this strategy
    }

    @Override
    public int getMaxDepthSearchForThisPos() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setMaxDepthSearchForThisPos(int minDepth) {
        // Not used in this strategy
    }

    @Override
    public int getMaxSearchTimeForThisPos() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setMaxSearchTimeForThisPos(int maxTime) {
        // Not used in this strategy
    }

    @Override
    public float getOpponentBestScoreOnPreviousMoveSoFar() {
        // Not used in this strategy
        return 0;
    }

    @Override
    public void setOpponentBestScoreOnPreviousMoveSoFar(float scoreToBeat) {
        // Not used in this strategy
    }

}
