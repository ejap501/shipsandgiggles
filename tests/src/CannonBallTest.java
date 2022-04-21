import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.physics.box2d.World;

import net.shipsandgiggles.pirate.entity.CannonBall;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * Cannonball Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class CannonBallTest {

    /** Through other tests, it is clear the constructor works **/

    @Test
    public void cannonBallMovement(){
        Sprite cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        World world = new World(new Vector2(0, 0), false);


        CannonBall ball = new CannonBall(world, cannonBallSprite, 1, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), new Vector2(100,100), new Vector2(200,200), (short)5,(short)6,(short)7);

        ball.body.getPosition();
        ball.movement();

        assertEquals(new Vector2(107.778175f,107.778175f), ball.body.getPosition());

    }

    @Test
    public void cannonBallDamage(){
        Sprite cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        World world = new World(new Vector2(0, 0), false);


        CannonBall ball = new CannonBall(world, cannonBallSprite, 3, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), new Vector2(100,100), new Vector2(200,200), (short)5,(short)6,(short)7);

        assertEquals(150f, ball.getDamageDelt());

    }




}