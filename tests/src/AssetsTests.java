import com.badlogic.gdx.Gdx;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetsTests {

    @Test
    public void testShipAssetExists(){
        assertTrue(true);
        assertTrue("Ship1.png exists", Gdx.files.internal("models/ship1.png").exists());
    }



}
