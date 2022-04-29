import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;
import net.shipsandgiggles.pirate.screen.impl.PreferenceScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class LoadingScreenTests {

        @Test
        public void screenCreation(){
            LoadingScreen.createParts();
            assertEquals(3,LoadingScreen.table.getRows() );
        }



    }


