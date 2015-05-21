package calvinCheckers;

import java.util.Random;


//author: Gary Kalmanovich; rights reserved

public class CheckersStrategyCalvin implements InterfaceStrategy {
    Random rand = new Random(); // One can seed with a parameter variable here
    @Override
    public InterfaceSearchResult getBestMove(InterfacePosition position, InterfaceSearchContext context) {
        InterfaceSearchResult result = negamax(position, context, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        context.setMinDepthSearchForThisPos(5);
        return result;
    }
    
    private InterfaceSearchResult negamax(InterfacePosition position, InterfaceSearchContext context, float alpha, float beta) {
    	InterfaceSearchResult searchResult = new CheckersSearchResultCalvin(); // Return information

        int player   = position.getPlayer();
        if (player == 0) {
            System.out.println("Problemo");
        	player = 1;
        }
        int opponent = 3-player; // There are two players, 1 and 2.
        float uncertaintyPenalty = .1f;
        boolean jumpPossible = ((CheckersPositionCalvin)position).jumpPossible();
        
        for ( InterfaceIterator iPos = new CheckersIterator(8,8); iPos.isInBounds(); iPos.increment() ) {
            InterfacePosition posNew = new CheckersPositionCalvin( position );
            if (((CheckersPositionCalvin)posNew).validMove( iPos, jumpPossible)) { // The column is not yet full
                posNew.setColor(iPos, player);
                int isWin = posNew.isWinner(); // iPos
                float score;
                if        ( isWin ==   player ) { score =  1f;  // Win
                } else if ( isWin ==        0 ) { score =  0f;  // Draw
                } else if ( isWin == opponent ) { score = -1f;  // Loss
                } else { // Game is not over, so check further down the game
                    if ( context.getCurrentDepth()   < context.getMaxDepthSearchForThisPos() &&     // No more than max
                         context.getCurrentDepth()   < context.getMinDepthSearchForThisPos()    ) { // No more than min
                        posNew.setPlayer(opponent);
                        context.setCurrentDepth(context.getCurrentDepth()+1);
                        InterfaceSearchResult opponentResult = negamax(posNew,context,-beta, -alpha); // Return information is in opponentContext
                        context.setCurrentDepth(context.getCurrentDepth()-1);
                        score = -opponentResult.getBestScoreSoFar();
                        // Note, for player, opponent's best move has negative worth
                        //   That is because, score = ((probability of win) - (probability of loss))
                        
                        if ( opponentResult.isResultFinal() == false ) { // if the result is not final, reverse penalty
                            searchResult.setIsResultFinal(false);
                            score -= 2*uncertaintyPenalty;
                        }
                    } else { 
                        // We cannot recurse further down the minimax search
                        score = posNew.valuePosition()-uncertaintyPenalty;
//                    	score = -uncertaintyPenalty;
                        searchResult.setIsResultFinal(false);
                    }
                }

                if (searchResult.getBestScoreSoFar() <  score ) {
                    searchResult.setBestMoveSoFar(iPos, score );
                    if ( score == 1f ) break; // No need to search further if one can definitely win
                }
                alpha = Math.max(alpha, score);
                if (alpha >= beta) {
                    break;
                }
            }
            long timeNow = System.nanoTime();
            if ( context.getMaxSearchTimeForThisPos() - timeNow <= 0 ) {
                System.out.println("CheckersStrategyCalvin:getBestMove(): ran out of time: maxTime("
                        +context.getMaxSearchTimeForThisPos()+") :time("
                        +timeNow+"): recDepth("+context.getCurrentDepth()+")");
                break; // Need to make any move now
            }
        }
        // if we haven't run out of time yet, then increase the depth
 		long timeLeftInNanoSeconds = context.getMaxSearchTimeForThisPos() - System.nanoTime();
 		if (context.getCurrentDepth() == 0 && !searchResult.isResultFinal() && timeLeftInNanoSeconds > ((CheckersSearchContext) context).getOriginalTimeLimit() * 9 / 10) { // TODO:
 																																											// add
 																																											// to
 																																											// interface
 			System.out.print("CheckersStrategyCalvin: Depth limit of " + context.getMinDepthSearchForThisPos() + " -> ");
 			context.setMinDepthSearchForThisPos(context.getMinDepthSearchForThisPos() + 1);
 			System.out.println(context.getMinDepthSearchForThisPos());
 			InterfaceSearchResult anotherResult = getBestMove(position, context);
 			if (anotherResult.getBestScoreSoFar() > searchResult.getBestScoreSoFar()) {
 				searchResult.setBestMoveSoFar(anotherResult.getBestMoveSoFar(), anotherResult.getBestScoreSoFar());
 				searchResult.setIsResultFinal(anotherResult.isResultFinal());
 			}
 		}
        return searchResult;
    }
    
	@Override
    public void setContext(InterfaceSearchContext strategyContext) {
        // Not used in this strategy
    }

    @Override
    public InterfaceSearchContext getContext() {
        // Not used in this strategy
        return null;
    }
}


