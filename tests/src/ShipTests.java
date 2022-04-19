import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;


@RunWith(GdxTestRunner.class)
public class ShipTests {

    @Test
    public void testMoveLeftNotMoving(){

        int x = 10;
        int y = 10;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera);
        Vector2 startPos = ship.getPosition();
        ship.setTurnDirection(2);

        assertEquals(ship.getPosition(),startPos);

    }

    public void testMoveLeftMoving(){

        int x = 10;
        int y = 10;


        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(x, y), playerModel.getHeight(), playerModel.getWidth(), camera);
        Vector2 startPos = ship.getPosition();
        ship.setTurnDirection(2);

        assertEquals(ship.getPosition(),startPos);

    }


}
