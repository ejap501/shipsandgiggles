import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.physics.box2d.World;

import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.CannonBall;

import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
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

    @Test
    public void cannonBallDoDamage(){
        Sprite cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        World world = new World(new Vector2(0, 0), false);

        CannonBall ball = new CannonBall(world, cannonBallSprite, 1, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), new Vector2(100,100), new Vector2(200,200), (short)7,Configuration.Cat_Player,(short)7);

        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(1, 1), playerModel.getHeight(), playerModel.getWidth(), camera, world);
        ship.createBody();
        DifficultyScreen.difficulty = 3;
        ship.takeDamage(ball.getDamageDelt());

        assertEquals(160f, Ship.health);

        Ship.health = Ship.maxHealth;
        DifficultyScreen.difficulty = 2;
        ship.takeDamage(ball.getDamageDelt());

        assertEquals(180f, Ship.health);

        Ship.health = Ship.maxHealth;
        DifficultyScreen.difficulty = 1;
        ship.takeDamage(ball.getDamageDelt());

        assertEquals(190f, Ship.health);

    }




}