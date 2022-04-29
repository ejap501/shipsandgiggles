import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import net.shipsandgiggles.pirate.screen.impl.InformationScreen;
import net.shipsandgiggles.pirate.screen.impl.ShopScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class ShopScreenTests {

    @Test
    public void screenCreation(){
        InformationScreen.createParts();
        assertEquals(38,InformationScreen.table.getRows() );
    }

    @Test
    public void cooldownListenerTest(){


        float beforeCooldown = Ship.burstCoolDown;
        Currency.get().give(Currency.Type.GOLD, ShopScreen.cooldownCost);
        ShopScreen.coolDownListener(); // Should run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.coolDownButton.getText().charAt(ShopScreen.coolDownButton.getText().length() -1));
        assertEquals(95,ShopScreen.cooldownCost);
        assertEquals(beforeCooldown -0.25f, Ship.burstCoolDown,0.01f);

        ShopScreen.coolDownListener();// Should not run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.coolDownButton.getText().charAt(ShopScreen.coolDownButton.getText().length() -1));
        assertEquals(95,ShopScreen.cooldownCost);
        assertEquals(beforeCooldown -0.25f, Ship.burstCoolDown,0.01f);

        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
    }

    @Test
    public void speedListenerTest(){

        float speedBefore = GameScreen.maxSpeed;
        Currency.get().give(Currency.Type.GOLD, ShopScreen.speedCost);
        ShopScreen.speedListener(); // Should run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.speedButton.getText().charAt(ShopScreen.speedButton.getText().length() -1));
        assertEquals(75,ShopScreen.speedCost);
        assertEquals(speedBefore * 1.25, GameScreen.currentSpeed, 0.01f);

        ShopScreen.speedListener();// Should not run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.speedButton.getText().charAt(ShopScreen.speedButton.getText().length() -1));
        assertEquals(75,ShopScreen.speedCost);
        assertEquals(speedBefore * 1.25, GameScreen.currentSpeed, 0.01f);

        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
    }

    @Test
    public void multiplierListenerTest(){

        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 1;
        GameScreen.createSprites();
        GameScreen.createEntities(bobBody,world,camera);

        float multiBefore = GameScreen.playerShips.getCoinMulti();
        Currency.get().give(Currency.Type.GOLD, ShopScreen.multiCost);
        ShopScreen.multiplierListener();; // Should run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.multiButton.getText().charAt(ShopScreen.multiButton.getText().length() -1));
        assertEquals(85,ShopScreen.multiCost);
        assertEquals(multiBefore * 2, GameScreen.playerShips.getCoinMulti(), 0.01f);

        ShopScreen.multiplierListener();// Should not run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.multiButton.getText().charAt(ShopScreen.multiButton.getText().length() -1));
        assertEquals(85,ShopScreen.multiCost);
        assertEquals(multiBefore * 2, GameScreen.playerShips.getCoinMulti(), 0.01f);

        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
    }

    @Test
    public void healthListenerTest(){

        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 1;
        GameScreen.createSprites();

        GameScreen.createEntities(bobBody,world,camera);
        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));

        double healthBefore = Ship.maxHealth;
        Currency.get().give(Currency.Type.GOLD, ShopScreen.healthCost);
        ShopScreen.healthListener();; // Should run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.healthButton.getText().charAt(ShopScreen.healthButton.getText().length() -1));
        assertEquals(105,ShopScreen.healthCost);
        assertEquals(healthBefore +  20, Ship.maxHealth, 0.01f);

        ShopScreen.healthListener();// Should not run since enough coins
        assertEquals(0,Currency.get().balance(Currency.Type.GOLD));
        assertEquals('2',ShopScreen.healthButton.getText().charAt(ShopScreen.healthButton.getText().length() -1));
        assertEquals(105,ShopScreen.healthCost);
        assertEquals(healthBefore + 20, Ship.maxHealth, 0.01f);

        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
    }




}