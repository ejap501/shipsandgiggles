import com.badlogic.gdx.Gdx;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class CurrencyTests {


    @Test
    public void makeCurrencyGold(){


        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(0, gold);
    }

    @Test
    public void subtractCurrencyGold(){
        Ship.coinMulti = 1;
        Currency.get().give(Currency.Type.GOLD, 100);
        Currency.get().take(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyGold(){
        Currency.get().give(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(100, gold);
        Currency.get().take(Currency.Type.GOLD, 100);
    }

    @Test
    public void subtractToNegativeCurrencyGold(){
        Currency.get().take(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyGoldWithMulti(){
        Ship.coinMulti = 2;
        Currency.get().give(Currency.Type.GOLD, 100);
        int gold = Currency.get().balance(Currency.Type.GOLD);

        assertEquals(200, gold);
        Currency.get().take(Currency.Type.GOLD, 200);
        Ship.coinMulti = 1;
    }
    public void makeCurrencyPoints(){


        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(0, gold);
    }

    @Test
    public void subtractCurrencyPoints(){
        Ship.pointMulti = 1;
        Currency.get().give(Currency.Type.POINTS, 100);
        Currency.get().take(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyPoints(){
        Currency.get().give(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(100, gold);
        Currency.get().take(Currency.Type.POINTS, 100);
    }

    @Test
    public void subtractToNegativeCurrencyPoints(){
        Currency.get().take(Currency.Type.POINTS, 100);
        int gold = Currency.get().balance(Currency.Type.POINTS);

        assertEquals(0, gold);
    }

    @Test
    public void addCurrencyGoldWithPoints(){
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