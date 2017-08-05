
package kcomtest;

/**
 *
 * @author Chris Milburn
 * Enum containing all the possible coin types available.
 */
public enum CoinType {
    POUND(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5),
    TWO(2),
    ONE(1);
    
   private final int pence;
    
   CoinType( int pence ) {
       this.pence = pence;
    } 
   public final int getDenomination() {
       return pence;
   }
}
