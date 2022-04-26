import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.BallsManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import net.shipsandgiggles.pirate.entity.npc.EnemyShip;

import static junit.framework.TestCase.assertEquals;
import static net.shipsandgiggles.pirate.screen.impl.GameScreen.createEnemy;

/**
 * Enemy Ships Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class EnemyShipsTest {

   @Test
   public void createBoat(){
       Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

       World world = new World(new Vector2(0, 0), false);

       Body body = createEnemy(false, new Vector2(100, 100),world);


       new EnemyShip(body,enemyModelA, 300f, new Location(100,100), 100, world);
       assertEquals(1, world.getBodyCount());
   }

   @Test
   public void enemyShipHealthBarColour(){

       Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

       World world = new World(new Vector2(0, 0), false);
       Body body = createEnemy(false, new Vector2(100, 100),world);

       EnemyShip badShip = new EnemyShip(body,enemyModelA, 300f, new Location(100,100), 100, world);
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
       World world = new World(new Vector2(0, 0), false);
       Body body = createEnemy(false, new Vector2(100, 100),world);

       EnemyShip badShip =  new EnemyShip(body,enemyModelA, 300f, new Location(100,100), 100, world);
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
       Body body = createEnemy(false, new Vector2(100, 100),world);

       Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);
       ship.createBody();
       EnemyShip badShip = new EnemyShip(body,enemyModelA, 300f, new Location(100,100), 100, world);

       badShip.shootPlayer(ship,world);
       assertEquals(1, BallsManager.listOfBalls.size());
       BallsManager.listOfBalls.clear();


   }
    @Test
    public void EnemyShipShootTimer(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Location location = new Location(100f,100f);
        Body body = createEnemy(false, new Vector2(100, 100),world);


        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, location, playerModel.getHeight(), playerModel.getWidth(), camera,world);

        ship.createBody();
        EnemyShip badShip = new EnemyShip(body,enemyModelA, 300f, new Location(100,100), 100, world);

        badShip.timer = 7;
        badShip.shootPlayer(ship,world);
        assertEquals(0, BallsManager.listOfBalls.size());


    }
    @Test
    public void EnemyShipShootNoContact(){

        Sprite enemyModelA = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body body = createEnemy(false, new Vector2(100, 100),world);

        Ship ship = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(10000f,10000f), playerModel.getHeight(), playerModel.getWidth(), camera,world);

        ship.createBody();
        EnemyShip badShip = new EnemyShip(body,enemyModelA, 300f, new Location(100,100), 100, world);

        badShip.shootPlayer(ship,world);
        assertEquals(0, BallsManager.listOfBalls.size());



    }




}