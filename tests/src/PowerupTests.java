import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.impl.collectible.powerUp;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Power-up Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class PowerupTests {

    @Test
    public void createPowerup() {
        World world = new World(new Vector2(0, 0), false);
        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));

        new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);

        assertEquals(1, world.getBodyCount());

        new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        new powerUp(invincibilityModel, new Location(500f,600f), "Invincible", world);
        new powerUp(invincibilityModel, new Location(400f,600f), "Invincible", world);


        assertEquals(4, world.getBodyCount());

    }
    @Test
    public void killPowerup() {
        World world = new World(new Vector2(0, 0), false);
        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));

        powerUp test = new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);

        test.death();

        assertEquals(0, world.getBodyCount());

        powerUp test2 =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        powerUp test3 =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        powerUp test4 =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);

        test2.death();
        test3.death();
        test4.death();

        assertEquals(0, world.getBodyCount());


    }
    @Test

    public void collisionPowerup() {
        World world = new World(new Vector2(0, 0), false);
        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        powerUp test =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(600f, 600f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        assertTrue(test.rangeCheck(ship));

    }

    @Test
    public void nocollisionPowerup() {
        World world = new World(new Vector2(0, 0), false);
        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        powerUp test =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(600f, 800f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        assertFalse(test.rangeCheck(ship));

    }
    @Test
    public void typeReturn(){

        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
        World world = new World(new Vector2(0, 0), false);
        powerUp test =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        Assert.assertEquals("Invincible",test.getPowerUpType());
    }

    @Test
    public void invincibilityTest(){

        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(1, 1), playerModel.getHeight(), playerModel.getWidth(), camera, world);
        ship.createBody();

        ship.setInvincible(true);
        ship.takeDamage(10);
        assertEquals(200f, Ship.health);
    }

    // All other power up mechanics are tested elsewhere


}