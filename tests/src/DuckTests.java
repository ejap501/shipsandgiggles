import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.badlogic.gdx.physics.box2d.World;

import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.npc.Duck;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

/**
 * Duck Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class DuckTests {

   // @Test
    //public void createDuck(){
    //    Sprite duckModel = new Sprite(new Texture(Gdx.files.internal("models/duck_v1.png")));
    //    World world = new World(new Vector2(0, 0), false);
//
    //    new Duck(duckModel, 300f, new Location(1,1),1, world);
    //    assertEquals(1, world.getBodyCount());
    //}
//
    //@Test
    //public void DuckHealthBarColour(){
//
    //    Sprite duckModel = new Sprite(new Texture(Gdx.files.internal("models/duck_v1.png")));
    //    Location location = new Location(100f,100f);
    //    float maximumHealth = 100;
    //    World world = new World(new Vector2(0, 0), false);
//
    //    Duck duck = new Duck(duckModel, 300f, location, (int) maximumHealth,world);
    //    duck.damage(20);
    //    assertEquals("Green", Color.GREEN,duck.healthBarColor());
//
    //    duck.damage(29);
    //    assertEquals("Orange",Color.ORANGE,duck.healthBarColor());
//
    //    duck.damage(50);
    //    assertEquals("Red",Color.RED,duck.healthBarColor());
//
    //}
    //@Test
    //public void DuckHealthBarWidth(){
//
    //    Sprite duckModel = new Sprite(new Texture(Gdx.files.internal("models/duck_v1.png")));
    //    Location location = new Location(100f,100f);
    //    float maximumHealth = 100;
    //    World world = new World(new Vector2(0, 0), false);
//
    //    Duck duck = new Duck(duckModel, 300f, location, (int) maximumHealth,world);
    //    duck.damage(20);
    //    assertEquals("Green",51.2f,duck.healthBarWidth());
//
//
    //}





}