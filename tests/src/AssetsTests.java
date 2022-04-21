import com.badlogic.gdx.Gdx;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Asset Test
 *
 * @author Team 22 : Sam Pearson
 * @version 1.0
 */
@RunWith(GdxTestRunner.class)
public class AssetsTests {

    @Test
    public void testShipAssetExists(){
        assertTrue(true);
        assertTrue("Ship1.png exists", Gdx.files.internal("models/ship1.png").exists());
    }



}
