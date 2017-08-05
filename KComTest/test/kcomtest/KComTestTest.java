package kcomtest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static kcomtest.KComTest.log;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris Milburn
 */
public class KComTestTest {

    
    String testCoinBankFileName = "test-coin-inventory.properties";
    /**
     * Test of getOptimalChangeFor method, of class KComTest.
     */
    @Test
    public void testGetOptimalChangeFor() {
        System.out.println("getOptimalChangeFor");
        int pence = 366;
        KComTest instance = new KComTest();
        Collection<Coin> expResult = new ArrayList<>();
        expResult.add(new Coin(CoinType.POUND,  3 ));
        expResult.add(new Coin(CoinType.FIFTY,  1 ));
        expResult.add(new Coin(CoinType.TWENTY, 0 ));
        expResult.add(new Coin(CoinType.TEN,    1 ));
        expResult.add(new Coin(CoinType.FIVE,   1 ));
        expResult.add(new Coin(CoinType.TWO,    0 ));
        expResult.add(new Coin(CoinType.ONE,    1 ));
        Collection<Coin> result = instance.getOptimalChangeFor( pence );
        assertTrue(expResult.equals( result ));
    }
    
    /**
     * Test of getOptimalChangeFor method, of class KComTest.
     */
    @Test
    public void testGetOptimalChangeFor_ZeroInput() {
        System.out.println("getOptimalChangeFor");
        int pence = 0;
        KComTest instance = new KComTest();
        Collection<Coin> expResult = new ArrayList<>();
        expResult.add(new Coin(CoinType.POUND,  0 ));
        expResult.add(new Coin(CoinType.FIFTY,  0 ));
        expResult.add(new Coin(CoinType.TWENTY, 0 ));
        expResult.add(new Coin(CoinType.TEN,    0 ));
        expResult.add(new Coin(CoinType.FIVE,   0 ));
        expResult.add(new Coin(CoinType.TWO,    0 ));
        expResult.add(new Coin(CoinType.ONE,    0 ));
        Collection<Coin> result = instance.getOptimalChangeFor( pence );
        assertTrue(expResult.equals( result ));
    }    

    /**
     * Test of run method, of class KComTest.
     */
 /*
    @Test
    public void testRun() {

        String[] args = {"366"};
        KComTest instance = new KComTest();
        try {
            instance.run( args );
        } catch (Exception ex) {
            assert( false );
            Logger.getLogger(KComTestTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assert( false );
    }
*/
    /**
     * Test of getChangeFor method where there is sufficent of each coin available.
     */
    @Test
    public void testGetChangeFor() {
        int pence = 366;
        setupTestCoinBank();
        KComTest kcomTest = new KComTest();
        kcomTest.setCoinBankFileName( testCoinBankFileName );
        Collection<Coin> expResult = new ArrayList<>();
        expResult.add(new Coin(CoinType.POUND,  3 ));
        expResult.add(new Coin(CoinType.FIFTY,  1 ));
        expResult.add(new Coin(CoinType.TWENTY, 0 ));
        expResult.add(new Coin(CoinType.TEN,    1 ));
        expResult.add(new Coin(CoinType.FIVE,   1 ));
        expResult.add(new Coin(CoinType.TWO,    0 ));
        expResult.add(new Coin(CoinType.ONE,    1 ));
        
        Collection<Coin> result;
        try {
            result = kcomTest.getChangeFor( pence );
            assertTrue(expResult.equals( result ));
            // Load in the properties file and check its been changed.
        } catch (Exception ex) {
            Logger.getLogger(KComTestTest.class.getName()).log(Level.SEVERE, null, ex);
            assert(false);
        }                
    }

    /**
     * Test of getChangeFor method where there is sufficent of each coin available.
     */
    @Test
    public void testGetChangeFor_IsWrittenToCoinFile() {
        int pence = 366;
        setupTestCoinBank();
        KComTest kcomTest = new KComTest();
        kcomTest.setCoinBankFileName( testCoinBankFileName );
//        kcomTest.loadCoinBank();
      
        Collection<Coin> result;
        try {
            result = kcomTest.getChangeFor( pence );
            // Load in the coin properties file and check its been changed.
            
            try {        
                Properties newCoinBank = new Properties();                        
                newCoinBank.load(  Files.newInputStream(Paths.get( testCoinBankFileName )));
                // Create what should be left in the coin bank...
                Properties expectedCoinBank = new Properties();
                // Lets start off with 3 of all the coins.
                expectedCoinBank.setProperty( String.valueOf( CoinType.POUND.getDenomination() ),"0");
                expectedCoinBank.setProperty( String.valueOf( CoinType.FIFTY.getDenomination()), "2");
                expectedCoinBank.setProperty( String.valueOf( CoinType.TWENTY.getDenomination()),"3");
                expectedCoinBank.setProperty( String.valueOf( CoinType.TEN.getDenomination()),   "2");
                expectedCoinBank.setProperty( String.valueOf( CoinType.FIVE.getDenomination()),  "2");
                expectedCoinBank.setProperty( String.valueOf( CoinType.TWO.getDenomination()),   "3");
                expectedCoinBank.setProperty( String.valueOf( CoinType.ONE.getDenomination()),   "2");
                
                // and check that is whats left.
                assertTrue( expectedCoinBank.equals( newCoinBank ) );

            } catch (IOException e) {
                log.log(Level.SEVERE, "Error loading "+testCoinBankFileName );
            }                                    
        } catch (Exception ex) {
            Logger.getLogger(KComTestTest.class.getName()).log(Level.SEVERE, null, ex);
            assert(false);            
        }                
    }
    
   /**
     * Test of getChangeFor method where there is sufficient but not of each coin available.
     */
    @Test
    public void testGetChangeFor_WithCoinSubstitution() {
        int pence = 499;
        setupTestCoinBank();
        KComTest kcomTest = new KComTest();
        kcomTest.setCoinBankFileName( testCoinBankFileName );
//        kcomTest.loadCoinBank();
        Collection<Coin> expResult = new ArrayList<>();
        expResult.add(new Coin(CoinType.POUND,  3 ));
        expResult.add(new Coin(CoinType.FIFTY,  3 ));
        expResult.add(new Coin(CoinType.TWENTY, 2 ));
        expResult.add(new Coin(CoinType.TEN,    0 ));
        expResult.add(new Coin(CoinType.FIVE,   1 ));
        expResult.add(new Coin(CoinType.TWO,    2 ));
        expResult.add(new Coin(CoinType.ONE,    0 ));
        
        Collection<Coin> result;
        try {
            result = kcomTest.getChangeFor( pence );
            assertTrue(expResult.equals( result ));
            // Load in the properties file and check its been changed.
        } catch (Exception ex) {
            Logger.getLogger(KComTestTest.class.getName()).log(Level.SEVERE, null, ex);
            assert(false);            
        }                
    }
        
      
   /**
     * Test of getChangeFor method where there is not sufficient coins available.
     */
    @Test
    public void testGetChangeFor_BlowingTheBank() {
        int pence = 1000;
        setupTestCoinBank();
        KComTest kcomTest = new KComTest();
        kcomTest.setCoinBankFileName( testCoinBankFileName );
//        kcomTest.loadCoinBank();
        
        Collection<Coin> result;
        try {
            result = kcomTest.getChangeFor( pence );            

        } catch (Exception ex) {
            assert( ex.getMessage().equals("Insufficent coins available"));
            Logger.getLogger(KComTestTest.class.getName()).log(Level.SEVERE, null, ex);
        }           
    }        

    /*
     Create a test coin bank so we dont overwrite the real thing in tests.
    */
    private void setupTestCoinBank() {

        Properties coinBank = new Properties();
        // Lets start off with 3 of all the coins.
        coinBank.setProperty( String.valueOf( CoinType.POUND.getDenomination() ),"3");
        coinBank.setProperty( String.valueOf( CoinType.FIFTY.getDenomination()), "3");
        coinBank.setProperty( String.valueOf( CoinType.TWENTY.getDenomination()),"3");
        coinBank.setProperty( String.valueOf( CoinType.TEN.getDenomination()),   "3");
        coinBank.setProperty( String.valueOf( CoinType.FIVE.getDenomination()),  "3");
        coinBank.setProperty( String.valueOf( CoinType.TWO.getDenomination()),   "3");
        coinBank.setProperty( String.valueOf( CoinType.ONE.getDenomination()),   "3");        
        
        try {
            OutputStream output = new FileOutputStream( testCoinBankFileName );
            coinBank.store(output, "Test coin bank");
        } catch (IOException ex) {
            Logger.getLogger(KComTest.class.getName()).log(Level.SEVERE, "Error saving "+testCoinBankFileName, ex);            
        }        
    }   
}
