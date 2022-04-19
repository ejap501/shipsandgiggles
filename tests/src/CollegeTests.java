import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(GdxTestRunner.class)
public class CollegeTests {

    @Test
    public void testDamageCollege(){

        Sprite texture = new Sprite();
        Location location = new Location(100f,100f);
        float maximumHealth = 100;

        AlcuinCollege college = new AlcuinCollege(texture,location,maximumHealth,new World(new Vector2(0, 0), false));
        college.damage(10);
        //assertEquals(maximumHealth - 10,college.health);
    }

    @Test
    public void testDestroyCollege(){

        Sprite texture = new Sprite();
        Location location = new Location(100f,100f);
        float maximumHealth = 2;
        World world = new World(new Vector2(0, 0), false);

        AlcuinCollege college = new AlcuinCollege(texture,location,maximumHealth,world);
        college.damage(2);
        college.removeFromWorld();
        //assertEquals(0, world.getBodyCount());
    }

    @Test
    public void testDamagedDestroyedCollege(){

        Sprite texture = new Sprite();
        Location location = new Location(100f,100f);
        float maximumHealth = 2;
        World world = new World(new Vector2(0, 0), false);

        AlcuinCollege college = new AlcuinCollege(texture,location,maximumHealth,world);
        college.damage(2);
        college.removeFromWorld();
        college.damage(2);
        //assertEquals(maximumHealth - 2,college.health);

    }


}
