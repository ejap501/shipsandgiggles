import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(GdxTestRunner.class)
public class EntityAITests {

    @Test
    public void createEntityAiEnemyShip() {
        World world = new World(new Vector2(0, 0), false);

        Body body = GameScreen.createEnemy(false, new Vector2(100, 100),world);
        Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

        //EntityAi entity = new EntityAi(body,300f,bobsSprite,(int)bobsSprite.getWidth(),(int)bobsSprite.getHeight() );

        assertEquals(1,world.getBodyCount());

    }

    @Test
    public void createEntityAiPlayer() {
        World world = new World(new Vector2(0, 0), false);
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Ship player = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(100, 100), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        player.createBody();

        EntityAi playerEntity = new EntityAi(player.getEntityBody(),300f);

        assertEquals(1,world.getBodyCount());

    }
    @Test
    public void EntityAiDeath() {
        World world = new World(new Vector2(0, 0), false);
        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Ship player = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(100, 100), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        player.createBody();

        EntityAi playerEntity = new EntityAi(player.getEntityBody(),300f);

        playerEntity.death(world);
        assertEquals(0,world.getBodyCount());

        Currency.get().take(Currency.Type.GOLD, Currency.get().balance(Currency.Type.GOLD));
        Currency.get().take(Currency.Type.POINTS, Currency.get().balance(Currency.Type.POINTS));

    }





    @Test
    public void EntityAiEnemyShipNoMovement() {
        World world = new World(new Vector2(0, 0), false);

        Body body = GameScreen.createEnemy(false, new Vector2(100, 100),world);
        Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));

        //EntityAi entity = new EntityAi(body,300f,bobsSprite,(int)bobsSprite.getWidth(),(int)bobsSprite.getHeight() );

        Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
        OrthographicCamera camera = new OrthographicCamera();

        Ship player = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(100, 100), playerModel.getHeight(), playerModel.getWidth(), camera,world);
        player.createBody();

        //entity.setTarget(player.getEntityBody());
        //Vector2 start = entity.body.getPosition();
        //entity.applySteering(new SteeringAcceleration<Vector2> (new Vector2(0,0),0),1);

        //assertEquals(start,entity.body.getPosition());

    }

    //@Test
    //public void EntityAiEnemyShipMovementLinear() {
    //    World world = new World(new Vector2(0, 0), false);
//
    //    Body body = GameScreen.createEnemy(false, new Vector2(100, 100),world);
    //    Sprite bobsSprite = new Sprite(new Texture(Gdx.files.internal("models/ship2.png")));
//
    //    Sprite playerModel = new Sprite(new Texture(Gdx.files.internal("models/player_ship.png")));
    //    OrthographicCamera camera = new OrthographicCamera();
//
    //    Ship player = new Ship(playerModel, 40000f, 100f, 0.3f, 1f, new Location(1000, 1000), playerModel.getHeight(), playerModel.getWidth(), camera,world);
    //    player.createBody();
//
//
    //    EntityAi entity = new EntityAi(body,300f,bobsSprite,(int)bobsSprite.getWidth(),(int)bobsSprite.getHeight() );
    //    entity.setTarget(player.getEntityBody());
//
    //    Vector2 start = entity.body.getPosition();
    //    for (int i =0; i <1000;i++) {
    //        entity.applySteering(new SteeringAcceleration<>(new Vector2(60, 60), 70), 0.001f);
    //    }
//
    //    assertEquals(start,entity.body.getPosition());
//
    //}
}