package connect4individual7x6;

//author: Gary Kalmanovich; rights reserved

public class CheckersIterator implements InterfaceIterator {
    
    // NOTE, this iterator just iterates the black squares
    // One would need to write a different iterator (possibly, 
    // but not necessarily based on this one) to iterate over 
    // possible moves

    private int   iterator  = 0; // Starts at 1 and skips every other (+=2)
    private int nC;
    private int nR;
    
    CheckersIterator(int nC, int nR) { this.nC = nC; this.nR = nR; resetBack();} 
    
    public int dC(){ 
    	if (iterator%8 <= 4) {
    		if (iterator%8 <=2) {
    			return (this.iR() +2) * -1;
    		} else {
    			return this.iR() +2;
    		}
    	} else {
    		if (iterator%8 <=2) {
    			return (this.iR() +1) * -1;
    		} else {
    			return this.iR() +1;
    		}
    	}
    }
    
    public int dR(){ 
    	if (iterator%8 <= 4) {
    		if ((iterator%8)%2 == 0) {
    			return (this.iR() +2) * -1;
    		} else {
    			return this.iR() +2;
    		}
    	} else {
    		if ((iterator%8)%2 == 0) {
    			return (this.iR() +1) * -1;
    		} else {
    			return this.iR() +1;
    		}
    	}
    }
    @Override public int          iC() { return (((iterator/8)*2)+1)%nC; }
    @Override public int          iR() { return (((iterator/8)*2)+1)/nC; }
    @Override public int          nC() { return          nC; }
    @Override public int          nR() { return          nR; }
    @Override public void  increment() {       iterator ++; }
    @Override public void  resetBack() {       iterator = 0; }
    @Override public void set( InterfaceIterator iter ) { iterator = ((CheckersIterator)iter).iterator; }
    @Override public void set(int iC, int iR) { assert (iR+iC)%2==1; iterator = nC*iR+iC; }
    @Override public boolean isInBounds() { return 0<= (((iterator/8)*2)+1) && (((iterator/8)*2)+1) < nC*nR; }
    
    
}
