package connect4MinimaxIndividual4x4;

//author: Gary Kalmanovich; rights reserved

public interface InterfaceStrategy {
    void getBestMove(InterfacePosition position, InterfaceSearchInfo context); // Return info is in context
    void setContext( InterfaceSearchInfo strategyContext );
    InterfaceSearchInfo getContext();
}
