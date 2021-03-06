import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import net.shipsandgiggles.pirate.entity.BallsManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static net.shipsandgiggles.pirate.entity.BallsManager.createBallAtAngle;

/**
 * Balls Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class BallsManagerTest {

    /** Through other tests, it is clear the method createBall works **/

    @Test
    public void createBallAngleTest(){
        Sprite cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        World world = new World(new Vector2(0, 0), false);

        createBallAtAngle(world, new Vector2(1,1), 1, 5,cannonBallSprite,(short)5,(short)6,(short)7);

        assertEquals(1, BallsManager.listOfBalls.size());

        BallsManager.listOfBalls.clear();
    }

    /** Cant test update since it needs batch**/



}