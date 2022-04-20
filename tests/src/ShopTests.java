import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.impl.shop.shop1;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class ShopTests {

    @Test
    public void createShop(){
        Sprite langwithCollegeSprite = new Sprite(new Texture("models/langwith_castle.png"));
        World world = new World(new Vector2(0, 0), false);

        new shop1(langwithCollegeSprite, new Location(1,1),1f, world);
        assertEquals(1, world.getBodyCount());
    }
    @Test
    public void rangeShop(){
        Sprite langwithCollegeSprite = new Sprite(new Texture("models/langwith_castle.png"));
        World world = new World(new Vector2(0, 0), false);
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        new shop1(langwithCollegeSprite, new Location(1,1),1f, world);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(1,1), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        shop1.rangeCheck(ship);


        assertTrue(Ship.buyMenuRange);
    }




}