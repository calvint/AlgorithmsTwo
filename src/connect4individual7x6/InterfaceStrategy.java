package connect4individual7x6;

//author: Gary Kalmanovich; rights reserved

public interface InterfaceStrategy {
    InterfaceSearchResult getBestMove(InterfacePosition position, InterfaceSearchContext context); // Return info is in context
    void setContext( InterfaceSearchContext strategyContext );
    InterfaceSearchContext getContext();
}
