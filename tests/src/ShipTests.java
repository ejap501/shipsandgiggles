import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * Ship Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class ShipTests {

    @Test
    public void testMoveLeftNotMoving(){

        int x = 100;
        int y = 100;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();
        Vector2 startPos = ship.getPosition();

        ship.setTurnDirection(2);

        assertEquals(ship.getPosition(),startPos);

    }
    @Test
    public void testMoveLeftMoving(){

        int x = 160;
        int y = 160;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();
        Vector2 startPos = ship.getPosition();
        ship.setSpeed(50f,50f);
        ship.setTurnDirection(2);
        ship.setDriveDirection(1);



        assertEquals("NOT WORKING",ship.getPosition(),startPos);

    }


}
