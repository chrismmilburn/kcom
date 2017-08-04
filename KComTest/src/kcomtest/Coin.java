
package kcomtest;

/**
 *
 * @author Chris Milburn
 */
public class Coin {
    private CoinType coinType;
    private int numberOfCoins;
    
    public Coin( CoinType coinType, int numberOfCoins){
        this.coinType = coinType;
        this.numberOfCoins = numberOfCoins;
    }
    
    public int getDenomination() {
        return coinType.getDenomination(); 
    }
    public int getNumberOfCoins() {
        return numberOfCoins;
    }
   
    public String toString() {
        return "For denomination "+coinType.name()+" "+numberOfCoins+" coins.";
    }
   
    @Override 
    public boolean equals(Object other) {
    boolean result = false;
        if(this == other){
            result = true;
        } else if( other != null) {
                if (other instanceof Coin) {
                    Coin that = (Coin) other;
                    result = (this.getDenomination() == that.getDenomination() && 
                              this.getNumberOfCoins() == that.getNumberOfCoins());
            }
        }
        
        return result;
    }
    
    @Override
    public int hashCode() {
        
        return coinType.getDenomination() * numberOfCoins * 37;
    }
    
}
