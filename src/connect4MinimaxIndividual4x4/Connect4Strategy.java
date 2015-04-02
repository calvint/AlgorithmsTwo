package connect4MinimaxIndividual4x4;

//author: Gary Kalmanovich; rights reserved

public class Connect4Strategy implements InterfaceStrategy {
    @Override
    public void getBestMove(InterfacePosition position, InterfaceSearchInfo context) {
        // Note, return information is embedded in context

        int player   = position.getPlayer();
        int opponent = 3-player; // There are two players, 1 and 2.
        
        //TODO Implement strategy. Note, this is not that different from TicTacToeStrategy that is now visible.
        // Nonetheless, if you want to be efficient, you may want to make it somewhat different.
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

class Connect4SearchInfo implements InterfaceSearchInfo {

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
        bestMoveSoFar  = new Connect4Iterator(newMove);
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

    @Override
    public int getClassStateCompacted() {
        // Not used in this strategy
        return 0/0;
    }

    @Override
    public void setClassStateFromCompacted(int compacted) {
        // Not used in this strategy
    }

}
