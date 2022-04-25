import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
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
    public void testEntityCreationEasy(){
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 1;

        GameScreen.createSprites();
        GameScreen.createEntities(bobBody,world,camera);
        assertEquals(347,world.getBodyCount());
    }
    @Test
    public void testEntityCreationMedium(){
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 2;

        GameScreen.createSprites();
        GameScreen.createEntities(bobBody,world,camera);
        assertEquals(302,world.getBodyCount());
    }
    @Test
    public void testEntityCreationHard(){
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 3;

        GameScreen.createSprites();
        GameScreen.createEntities(bobBody,world,camera);
        assertEquals(257,world.getBodyCount());
    }

}