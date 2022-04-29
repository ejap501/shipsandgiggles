import net.shipsandgiggles.pirate.screen.impl.InformationScreen;
import net.shipsandgiggles.pirate.screen.impl.PreferenceScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class preferenceScreenTest {

        @Test
        public void screenCreation(){
            PreferenceScreen.createParts();
            assertEquals(8,PreferenceScreen.table.getRows() );
        }



    }


