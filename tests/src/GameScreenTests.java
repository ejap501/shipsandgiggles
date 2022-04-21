import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Gamescreen Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class GameScreenTests {

    @Test
    public void testSpriteCreation(){
        assertTrue(GameScreen.createSprites());
    }

    @Test
    public void spawnMethodTest(){
        World world = new World(new Vector2(0, 0), false);
        GameScreen.createSprites();

        GameScreen.spawn(10,10,10,10,10,world);
        assertEquals(50,world.getBodyCount());

    }

    @Test
    public void spawnMethodNoOverlap(){
        World world = new World(new Vector2(0, 0), false);
        GameScreen.createSprites();

        GameScreen.spawn(10,10,10,10,10,world);
        assertEquals(50,world.getBodyCount());

    }


    @Test
    public void testEntityCreation(){
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);

        GameScreen.createSprites();
        GameScreen.createEntities(bobBody,world,camera);
        assertEquals(537,world.getBodyCount());
    }

}