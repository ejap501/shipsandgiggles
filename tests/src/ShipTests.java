import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;

import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.graalvm.compiler.lir.amd64.vector.AMD64VectorMove;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Ship Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class ShipTests {

    @Test
    public void testMoveLeftNotMoving() {

        int x = 100;
        int y = 100;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera, world);
        ship.createBody();
        Vector2 startPos = ship.getPosition();

        ship.setTurnDirection(2);

        assertEquals(ship.getPosition(), startPos);

    }

    @Test
    public void shoot() {

        int x = 100;
        int y = 100;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera, world);
        ship.createBody();
        Vector2 startPos = ship.getPosition();
        Sprite cannonBall = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

        ship.shoot(world,cannonBall,camera,(short) 1,(short)1,(short)1);

        Assert.assertEquals(2,world.getBodyCount());

    }

    @Test
    public void burstShoot() {

        int x = 100;
        int y = 100;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera, world);
        ship.createBody();
        Vector2 startPos = ship.getPosition();
        Sprite cannonBall = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

        ship.burstShoot(world,cannonBall,camera,(short) 1,(short)1,(short)1);
        for(int i = 0; i <= ship.numberOfShotsLeft + 2; i ++) { //Runs for 4 times but only fires for 3 lots of balls
            ship.rapidShot(world, cannonBall, camera, (short) 1, (short) 1, (short) 1);
            ship.timeBetweenRapidShots = 0;
        }

        Assert.assertEquals(9,world.getBodyCount());

    }

   //@Test
   //public void testMoveLeftMoving(){
   //    int x = 160;
   //    int y = 161;
   //    Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
   //    OrthographicCamera camera = new OrthographicCamera();
   //    World world = new World(new Vector2(0, 0), false);
   //     Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera, world);
   //    ship.createBody();
   //    Vector2 startPos = ship.getPosition();
   //    ship.getEntityBody().setLinearDamping(0.5f);
   //    ship.setMaxSpeed(100000f, 1);
   //    ship.setSpeed(500f, 500f);
   //    ship.setTurnDirection(2);
   //    ship.setDriveDirection(1);
   //    ship.getEntityBody().setLinearVelocity(new Vector2(100,100));
//
   //    for (int i = 0; i < 20; i++) {
//
   //        GameScreen.processInput(ship);
//
   //        //ship.getSprite().setPosition(ship.getEntityBody().getPosition().x * PIXEL_PER_METER - (ship.getSkin().getWidth() / 2f), ship.getEntityBody().getPosition().y * PIXEL_PER_METER - (ship.getSkin().getHeight() / 2f));
   //        //ship.getSprite().setRotation((float) Math.toDegrees(ship.getEntityBody().getAngle()));
   //        //Vector2 forwardSpeed = ship.getForwardVelocity();
   //        //Vector2 lateralSpeed = ship.getLateralVelocity();
   //        //ship.getEntityBody().setLinearVelocity(forwardSpeed.x + lateralSpeed.x * ship.getDriftFactor(), forwardSpeed.y + lateralSpeed.y * ship.getDriftFactor());
   //    }
   //    assertEquals("NOT WORKING", ship.getPosition(), new Vector2(0, 0));
//
   //}
}



