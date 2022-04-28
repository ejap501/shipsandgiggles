import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.impl.collectible.Coin;
import net.shipsandgiggles.pirate.screen.impl.DifficultyScreen;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import net.shipsandgiggles.pirate.screen.impl.LoadingScreen;
import net.shipsandgiggles.pirate.screen.impl.ShopScreen;
import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class loadingSavingTests {

    @Test
    public void savingTest(){
        Currency.get().give(Currency.Type.GOLD,10);
        Currency.get().give(Currency.Type.POINTS,10);
        LoadingScreen.loadedGame = false;

        World world = new World(new Vector2(0, 0), false);
        OrthographicCamera camera = new OrthographicCamera();
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 3;
        GameScreen.createSprites();
        GameScreen.createEntities(bobBody,world,camera);

        ShopScreen.speedCost = 1;
        ShopScreen.multiCost = 2;
        ShopScreen.healthCost = 3;
        ShopScreen.cooldownCost = 4;
        ShopScreen.speedTier = 5;
        ShopScreen.multiTier = 6;
        ShopScreen.healthTier = 7;
        ShopScreen.cooldownTier = 8;
        GameScreen.closeAndSave();
        assertTrue(Gdx.files.internal("saves/bobSaveFile.json").exists());
        LoadingScreen.loadedGame = false;

    }

    @Test
    public void zloadingTest(){
        OrthographicCamera camera = new OrthographicCamera();
        World world = new World(new Vector2(0, 0), false);
        Body bobBody = GameScreen.createEnemy(false, new Vector2(100,100),world);
        DifficultyScreen.difficulty = 3;

        GameScreen.createSprites();
        LoadingScreen.loadedGame = true;
        GameScreen.coinData = new ArrayList<>();
        GameScreen.stoneData = new ArrayList<>();
        GameScreen.powerUpData = new ArrayList<>();
        GameScreen.hostileShips = new ArrayList<>();
        GameScreen.ducks = new ArrayList<>();

        FileHandle coinFile1 = Gdx.files.local("saves/coinSaveFile.json");
        String coinText1 = coinFile1.readString();
        String[] coinList1 = coinText1.split(",");


        FileHandle difficultyFile1 = Gdx.files.local("saves/difficultySaveFile.json");
        String diffText1 = difficultyFile1.readString();

        FileHandle playerFile1 = Gdx.files.local("saves/playerSaveFile.json");
        String playerText1 = playerFile1.readString();
        String[] playerList1 = playerText1.split(",");

        GameScreen.createEntities(bobBody,world,camera);
        GameScreen.closeAndSave();

        FileHandle coinFile2 = Gdx.files.local("saves/coinSaveFile.json");
        String coinText2 = coinFile2.readString();
        String[] coinList2 = coinText2.split(",");

        FileHandle difficultyFile2 = Gdx.files.local("saves/difficultySaveFile.json");
        String diffText2 = difficultyFile2.readString();

        FileHandle playerFile2 = Gdx.files.local("saves/playerSaveFile.json");
        String playerText2 = playerFile2.readString();
        String[] playerList2 = playerText2.split(",");

        assertEquals(diffText1, diffText2);
        assertEquals(Arrays.toString(coinList1), Arrays.toString(coinList2));
        assertEquals(Arrays.toString(playerList1), Arrays.toString(playerList2));

        LoadingScreen.loadedGame = false;

    }
}
