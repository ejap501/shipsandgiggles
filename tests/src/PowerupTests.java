import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.impl.collectible.powerUp;

import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
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
    public void powerUpRangeChecksTest(){
        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(600f, 600f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        powerUp testInv =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        powerUp testSpeed =new powerUp(invincibilityModel, new Location(600f,600f), "Speed Up", world);
        powerUp testPoint =new powerUp(invincibilityModel, new Location(600f,600f), "Point Multiplier", world);
        powerUp testCoin =new powerUp(invincibilityModel, new Location(600f,600f), "Coin Multiplier", world);
        powerUp testDamage =new powerUp(invincibilityModel, new Location(600f,600f), "Damage Increase", world);

        GameScreen.powerUpChecks(testInv,ship);
        Assert.assertEquals(10,GameScreen.invincibilityTimer,0.001f);

        GameScreen.powerUpChecks(testSpeed,ship);
        Assert.assertEquals(10,GameScreen.speedTimer,0.001f);

        GameScreen.powerUpChecks(testPoint,ship);
        Assert.assertEquals(10,GameScreen.pointTimer,0.001f);

        GameScreen.powerUpChecks(testCoin,ship);
        Assert.assertEquals(10,GameScreen.coinTimer,0.001f);

        GameScreen.powerUpChecks(testDamage,ship);
        Assert.assertEquals(10,GameScreen.damageTimer,0.001f);

        Assert.assertEquals(1,world.getBodyCount(),0.001f);
    }


    @Test
    public void powerUpUpdatesTests(){
        Sprite invincibilityModel = new Sprite(new Texture(Gdx.files.internal("models/invincibility.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(600f, 600f), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        powerUp testInv =new powerUp(invincibilityModel, new Location(600f,600f), "Invincible", world);
        powerUp testSpeed =new powerUp(invincibilityModel, new Location(600f,600f), "Speed Up", world);
        powerUp testPoint =new powerUp(invincibilityModel, new Location(600f,600f), "Point Multiplier", world);
        powerUp testCoin =new powerUp(invincibilityModel, new Location(600f,600f), "Coin Multiplier", world);
        powerUp testDamage =new powerUp(invincibilityModel, new Location(600f,600f), "Damage Increase", world);

        GameScreen.powerUpChecks(testInv,ship);
        GameScreen.powerUpChecks(testSpeed,ship);
        GameScreen.powerUpChecks(testPoint,ship);
        GameScreen.powerUpChecks(testCoin,ship);
        GameScreen.powerUpChecks(testDamage,ship);

        GameScreen.powerUpUpdates(ship);
        Assert.assertEquals(9.98,GameScreen.invincibilityTimer,0.01f);
        Assert.assertEquals(9.98,GameScreen.speedTimer,0.01f);
        Assert.assertEquals(9.98,GameScreen.pointTimer,0.01f);
        Assert.assertEquals(9.98,GameScreen.coinTimer,0.01f);
        Assert.assertEquals(9.98,GameScreen.damageTimer,0.01f);

        assertTrue(ship.invincible);
        Assert.assertEquals(GameScreen.maxSpeed * GameScreen.speedMul,GameScreen.currentSpeed,0.01f);
        Assert.assertEquals(3,ship.getPointMulti(),0.01f);
        Assert.assertEquals(2,ship.getCoinMulti(),0.01f);
        Assert.assertEquals(2,ship.damageMulti,0.01f);


        GameScreen.invincibilityTimer = -0.5f;
        GameScreen.speedTimer = -0.5f;
        GameScreen.pointTimer = -0.5f;
        GameScreen.coinTimer = -0.5f;
        GameScreen.damageTimer = -0.5f;

        GameScreen.powerUpUpdates(ship);

        Assert.assertEquals(-1,GameScreen.invincibilityTimer,0.01f);
        Assert.assertEquals(-1,GameScreen.speedTimer,0.01f);
        Assert.assertEquals(-1,GameScreen.pointTimer,0.01f);
        Assert.assertEquals(-1,GameScreen.coinTimer,0.01f);
        Assert.assertEquals(-1,GameScreen.damageTimer,0.01f);

        assertFalse(ship.invincible);
        Assert.assertEquals(GameScreen.maxSpeed,GameScreen.currentSpeed,0.01f);
        Assert.assertEquals(1,ship.getPointMulti(),0.01f);
        Assert.assertEquals(1,ship.getCoinMulti(),0.01f);
        Assert.assertEquals(1,ship.damageMulti,0.01f);

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