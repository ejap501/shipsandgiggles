import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.currency.Currency;

import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Currency Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class CurrencyTests {


    @Test
    public void makeCurrencyGold(){

        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        ////////////////////////////////


        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(0, gold);
    }

    @Test
    public void subtractCurrencyGold(){
        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        ////////////////////////////////


        Currency.get().give(Currency.Type.GOLD, 100);
        Currency.get().take(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyGold(){
        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        LoadingScreen.loadedGame = false;
        ////////////////////////////////


        Currency.get().give(Currency.Type.GOLD, 100);

        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(100, gold);
        Currency.get().take(Currency.Type.GOLD, 100);
    }

    @Test
    public void subtractToNegativeCurrencyGold(){
        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        ////////////////////////////////


        Currency.get().take(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyGoldWithMulti(){
        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        ////////////////////////////////


        Ship.coinMulti = 2;
        Currency.get().give(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(200, gold);
        Currency.get().take(Currency.Type.GOLD, 200);
        Ship.coinMulti = 1;
    }
    @Test
    public void makeCurrencyPoints(){

        Currency.get().take(Currency.Type.POINTS, Currency.get().balance(Currency.Type.POINTS));
        ////////////////////////////////


        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(0, gold);
    }

    @Test
    public void subtractCurrencyPoints(){
        Currency.get().take(Currency.Type.POINTS, Currency.get().balance(Currency.Type.POINTS));
        ////////////////////////////////


        Ship.pointMulti = 1;
        Currency.get().give(Currency.Type.POINTS, 100);
        Currency.get().take(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyPoints(){
        Currency.get().take(Currency.Type.POINTS, Currency.get().balance(Currency.Type.POINTS));
        ////////////////////////////////


        Currency.get().give(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(100, gold);
        Currency.get().take(Currency.Type.POINTS, 100);
    }

    @Test
    public void subtractToNegativeCurrencyPoints(){
        Currency.get().take(Currency.Type.POINTS, Currency.get().balance(Currency.Type.POINTS));
        ////////////////////////////////

        Currency.get().take(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyGoldWithPoints(){
        Currency.get().take(Currency.Type.POINTS, Currency.get().balance(Currency.Type.POINTS));
        ////////////////////////////////

        Ship.pointMulti = 2;
        Currency.get().give(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(200, gold);
        Currency.get().take(Currency.Type.POINTS, 200);
        Ship.pointMulti = 1;
    }

    @Test
    public void fancyNameCheck(){
        assertEquals("Gold",Currency.Type.GOLD.getFancyName());
    }




}