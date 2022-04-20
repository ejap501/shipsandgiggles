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
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import net.shipsandgiggles.pirate.entity.npc.Duck;
import net.shipsandgiggles.pirate.entity.npc.EnemyShip;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(GdxTestRunner.class)
public class EnemyShipsTest {

    @Test
    public void createBoat(){
        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Location location = new Location(100f,100f);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);

        new EnemyShip(enemyModelA, new Location(1,1),1f, world,ship);
        assertEquals(1, world.getBodyCount());
    }

    @Test
    public void enemyShipHealthBarColour(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Location location = new Location(100f,100f);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);

        EnemyShip badShip = new EnemyShip(enemyModelA, new Location(1,1),100f, world,ship);
        badShip.damage(20);
        assertEquals("Green", Color.GREEN,badShip.healthBarColor());

        badShip.damage(29);
        assertEquals("Orange",Color.ORANGE,badShip.healthBarColor());

        badShip.damage(50);
        assertEquals("Red",Color.RED,badShip.healthBarColor());

    }
    @Test
    public void EnemyShipBarWidth(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Location location = new Location(100f,100f);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);

        EnemyShip badShip = new EnemyShip(enemyModelA, new Location(1,1),100f, world,ship);
        badShip.damage(20);
        assertEquals("Green",35.2f,badShip.healthBarWidth());


    }
    @Test
    public void EnemyShipShoot(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Location location = new Location(100f,100f);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);

        ship.createBody();
        EnemyShip badShip = new EnemyShip(enemyModelA, new Location(1,1),100f, world,ship);

        badShip.shootPlayer(ship);
        assertEquals(1, BallsManager.listOfBalls.size());


    }
    @Test
    public void EnemyShipShootTimer(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Location location = new Location(100f,100f);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);

        ship.createBody();
        EnemyShip badShip = new EnemyShip(enemyModelA, new Location(1,1),100f, world,ship);

        badShip.timer = 7;
        badShip.shootPlayer(ship);
        assertEquals(0, BallsManager.listOfBalls.size());


    }
    @Test
    public void EnemyShipShootNoContact(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(10000f,10000f), playerModel.getHeight(), playerModel.getWidth(), camera,world);

        ship.createBody();
        EnemyShip badShip = new EnemyShip(enemyModelA, new Location(1,1),100f, world,ship);

        badShip.shootPlayer(ship);
        assertEquals(0, BallsManager.listOfBalls.size());


    }




}