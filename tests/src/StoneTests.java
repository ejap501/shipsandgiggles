import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.impl.obstacles.Stone;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class StoneTests {

    @Test
    public void createStone(){
        Sprite stoneModelA = new Sprite(new Texture(Gdx.files.internal("models/stone_1.png")));
        World world = new World(new Vector2(0, 0), false);

        new Stone(stoneModelA, new Location(1,1), world);
        assertEquals(1, world.getBodyCount());
    }





}