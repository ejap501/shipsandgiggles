import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class DifficultyScreenTests {

        @Test
        public void screenCreation(){
            DifficultyScreen.createParts();
            assertEquals(4,DifficultyScreen.table.getRows() );
        }



    }


