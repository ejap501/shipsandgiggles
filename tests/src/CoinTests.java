import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.impl.collectible.Coin;

import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;

/**
 * Coin Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class CoinTests {

    @Test
    public void createCoin() {
        World world = new World(new Vector2(0, 0), false);
        Sprite coinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));

        new Coin(coinModel, new Location(600f,600f),"Gold", world);

        assertEquals(1, world.getBodyCount());

        new Coin(coinModel, new Location(600f,600f),"Gold", world);
        new Coin(coinModel, new Location(500f,600f), "Gold", world);
        new Coin(coinModel, new Location(400f,600f), "Gold", world);
        //Not checking to see if spawning on same tile is ok here as tested in GameScreen

        assertEquals(4, world.getBodyCount());

    }
    @Test
    public void killCoin() {
        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        LoadingScreen.loadedGame = false;
        /////////////////////////

        World world = new World(new Vector2(0, 0), false);
        Sprite coinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));

        Coin test = new Coin(coinModel, new Location(600f,600f),"Gold", world);

        test.death();

        assertEquals(0, world.getBodyCount());

        Coin test2 =new Coin(coinModel, new Location(600f,600f),"Gold", world);
        Coin test3 =new Coin(coinModel, new Location(600f,600f),"Silver", world);
        Coin test4 =new Coin(coinModel, new Location(600f,600f),"Copper", world);

        test2.death();
        test3.death();
        test4.death();

        assertEquals(0, world.getBodyCount());

        assertEquals("Invalid reward number",26, Currency.get().balance(Currency.Type.GOLD));

    }
    @Test

    public void collisionCoin() {
        World world = new World(new Vector2(0, 0), false);
        Sprite coinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Coin test = new Coin(coinModel, new Location(600f, 600f),"Gold", world);
        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(600f, 600f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        assertTrue(test.rangeCheck(ship));

    }

    @Test
    public void nocollisionCoin() {
        World world = new World(new Vector2(0, 0), false);
        Sprite coinModel = new Sprite(new Texture(Gdx.files.internal("models/gold_coin.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Coin test = new Coin(coinModel, new Location(600f, 600f),"Gold", world);
        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(600f, 800f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        assertFalse(test.rangeCheck(ship));

    }

}