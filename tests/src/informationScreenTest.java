import com.badlogic.gdx.Gdx;
import net.shipsandgiggles.pirate.screen.impl.InformationScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@RunWith(GdxTestRunner.class)
public class informationScreenTest {

        @Test
        public void screenCreation(){
            InformationScreen.createParts();
            assertEquals(37,InformationScreen.table.getRows() );
        }



    }


