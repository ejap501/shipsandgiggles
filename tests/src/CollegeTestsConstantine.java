import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.entity.BallsManager;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * College Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class CollegeTestsConstantine {

    @Test
    public void testDamageCollege(){

        Sprite texture = new Sprite();
        Location location = new Location(100f,100f);
        float maximumHealth = 100;

        ConstantineCollege college = new ConstantineCollege(texture,location,maximumHealth,new World(new Vector2(0, 0), false));
        college.damage(10);
        assertEquals(maximumHealth - 10,college.health);
    }

    @Test
    public void testDestroyCollege(){

        Sprite texture = new Sprite();
        Location location = new Location(100f,100f);
        float maximumHealth = 2;
        World world = new World(new Vector2(0, 0), false);

        ConstantineCollege college = new ConstantineCollege(texture,location,maximumHealth,world);
        college.damage(2);
        college.removeFromWorld();
        assertEquals(0, world.getBodyCount());
    }

    @Test
    public void testDamagedDestroyedCollege(){

        Sprite texture = new Sprite();
        Location location = new Location(100f,100f);
        float maximumHealth = 2;
        World world = new World(new Vector2(0, 0), false);

        ConstantineCollege college = new ConstantineCollege(texture,location,maximumHealth,world);
        college.damage(2);
        college.removeFromWorld();
        college.damage(2);
        assertEquals(maximumHealth - 2,college.health);

    }

    @Test
    public void CollegeHealthBarColour(){

        Sprite ConstantineCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
        Location location = new Location(100f,100f);
        float maximumHealth = 100;
        World world = new World(new Vector2(0, 0), false);

        ConstantineCollege college = new ConstantineCollege(ConstantineCollegeSprite,location,maximumHealth,world);
        college.damage(20);
        assertEquals("Green",Color.GREEN,college.healthBarColor());

        college.damage(29);
        assertEquals("Orange",Color.ORANGE,college.healthBarColor());

        college.damage(50);
        assertEquals("Red",Color.RED,college.healthBarColor());

    }

    @Test
    public void CollegeHealthBarWidth(){

        Sprite ConstantineCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
        Location location = new Location(100f,100f);
        float maximumHealth = 100;
        World world = new World(new Vector2(0, 0), false);

        ConstantineCollege college = new ConstantineCollege(ConstantineCollegeSprite,location,maximumHealth,world);
        college.damage(20);
        assertEquals("Green",88.4f,college.healthBarWidth());


    }
    @Test
    public void CollegeShoot(){

        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);
        Location location = new Location(100f,100f);


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        Sprite ConstantineCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
        float maximumHealth = 100;

        ConstantineCollege college = new ConstantineCollege(ConstantineCollegeSprite,location,maximumHealth,world);

        college.shootPlayer(ship);
        assertEquals(1,BallsManager.listOfBalls.size());
        BallsManager.listOfBalls.clear();


    }

    @Test
    public void CollegeShootWithTimer(){

        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);
        Location location = new Location(100f,100f);


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        Sprite ConstantineCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
        float maximumHealth = 100;

        ConstantineCollege college = new ConstantineCollege(ConstantineCollegeSprite,location,maximumHealth,world);
        college.timer = 7;
        college.shootPlayer(ship);
        assertEquals(0,BallsManager.listOfBalls.size());


    }

    @Test
    public void CollegeShootNoContact(){

        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);
        Location location = new Location(100f,100f);


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);
        ship.createBody();

        Sprite ConstantineCollegeSprite = new Sprite(new Texture("models/alcuin_castle.png"));
        float maximumHealth = 100;

        ConstantineCollege college = new ConstantineCollege(ConstantineCollegeSprite,new Location(10000f,10000f),maximumHealth,world);
        college.shootPlayer(ship);
        assertEquals(0,BallsManager.listOfBalls.size());


    }





}
