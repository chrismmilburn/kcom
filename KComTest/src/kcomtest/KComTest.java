
package kcomtest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chris Milburn
 */
public class KComTest {

    static final Logger log = Logger.getGlobal();

    Properties coinBank;
    static private String coinBankFileName = "coin-inventory.properties";    
    /**
     * @param args the command line arguments
     */
    public static void main(String... args){
        
        if( args.length > 0) {
            KComTest kcomTest = new KComTest();

            try {
               Integer inputPence = Integer.parseInt( args[0] );
               if( inputPence >= 0 ){
                // First calculate the optimal coins.
                Collection<Coin> optimalCoins = kcomTest.getOptimalChangeFor( inputPence );
                System.out.println("Pence entered = "+inputPence);
                System.out.println("Optimal distribution");
                optimalCoins.stream().forEach( System.out::println );
                // Now calculate using the coins available.
                Collection<Coin> coinsUsingCoinBank = kcomTest.getChangeFor( inputPence ); 
                System.out.println("Distribution with coins available");            
                coinsUsingCoinBank.stream().forEach( System.out::println );             
               }
           } catch( NumberFormatException nfe) {                            
               log.log(Level.SEVERE, "Invalid number entered. ", args[0]);
           } catch( Exception ex) {
               log.log(Level.SEVERE, ex.getMessage());               
           }
        } else {
            log.log(Level.SEVERE, "No number entered. ");            
        }
    }
    
    /*
     Split the number down into the optimum coin set.
    */
    public Collection<Coin> getOptimalChangeFor(int pence) {

        List<Coin> coins = new ArrayList<>();
      
        // Iterate over the available coin denominations.
        for(CoinType coinType : CoinType.values() ){
            int numberOfCoins = pence / coinType.getDenomination();
            coins.add(new Coin(coinType, numberOfCoins));
            pence -= numberOfCoins * coinType.getDenomination();
        }      
        return coins;
    }  
    /*
     Split the number of pence down into the optimum set using the coins we have available.
    */
    public Collection<Coin> getChangeFor(int pence) throws Exception  {
        List<Coin> coins = new ArrayList<>();
      
        // Load up the initial coins available.
        loadCoinBank(); 
        
        // Iterate over the available coin denominations.
        for(CoinType coinType : CoinType.values() ){
            
            // Get the number of coins of this type available in the bank.
            int coinsAvailable = Integer.parseInt( coinBank.getProperty( Integer.toString( coinType.getDenomination()) ) );
                                        
            int coinsRequired = pence / coinType.getDenomination();

            // See if these coins are available, if not take what we've got.                
            if( coinsRequired <= coinsAvailable) {
                coins.add(new Coin(coinType, coinsRequired));  
                pence -= coinsRequired * coinType.getDenomination(); 
                // and remove that many  coins from the coin bank.
                coinBank.setProperty( String.valueOf( coinType.getDenomination() ), String.valueOf( coinsAvailable - coinsRequired ));                      
            } else {
                // Not enough coins are available so take what we've got.
                coins.add(new Coin(coinType, coinsAvailable));
                pence -= coinsAvailable * coinType.getDenomination();  
                // and remove all the coins from the coin bank.
                coinBank.setProperty( String.valueOf( coinType.getDenomination() ), "0");  
            }               
        }
        
        if( pence > 0) {
            // We dont have enough change so throw exeception as per the spec.
            throw new Exception("Insufficent coins available");
        } else {
            // We had enough coins so save the depleted coinBank away.
            saveCoinBank( );    
        }       
        
        return coins;
    }
    
    /*
      Load the coin bank from file if it hasnt been loaded yet.
    */   
    public void loadCoinBank() {
 
        try {
            if(coinBank == null) {
                coinBank = new Properties();
                coinBank.load(  Files.newInputStream(Paths.get( coinBankFileName )));
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error loading "+coinBankFileName );
        }         
    }
    
    private void saveCoinBank() {
        
        OutputStream output = null;
                
        try {
            output = new FileOutputStream( coinBankFileName );
            coinBank.store(output, "Remaining coins");            
        } catch (IOException ex) {
            Logger.getLogger(KComTest.class.getName()).log(Level.SEVERE, "Error saving "+coinBankFileName, ex);            
        } 
    }

    // Allow us to set the coin bank file for testing.
    public void setCoinBankFileName(String coinBankFileName) {
        this.coinBankFileName = coinBankFileName;
    }
    
}
