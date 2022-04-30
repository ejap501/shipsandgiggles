import com.badlogic.gdx.graphics.Color;
import net.shipsandgiggles.pirate.HUDmanager;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;
import net.shipsandgiggles.pirate.screen.impl.InformationScreen;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class HUDTest {

    @Test
    public void HUDCreation(){
        HUDmanager.createParts();
        assertEquals(1,HUDmanager.topLeftTable.getRows());
        assertEquals(5,HUDmanager.topRightTable.getRows() );
        assertEquals(4,HUDmanager.timerLabels.getRows() );
        assertEquals(1,HUDmanager.bottomLeftTable.getRows() );
        assertEquals(0,HUDmanager.abalities.getRows() );
    }

    @Test
    public void HUDHealthBar(){
        Ship.maxHealth = 200f;
        Ship.health = 150f;
        assertEquals(Color.GREEN,HUDmanager.healthBarChecker());
        Ship.health = 90f;
        assertEquals(Color.ORANGE,HUDmanager.healthBarChecker());
        Ship.health = 10f;
        assertEquals(Color.RED,HUDmanager.healthBarChecker());

    }

    @Test
    public void HUDUpdates(){
        HUDmanager.timeCounter = 2;
        Ship.burstTimer = 2;
        GameScreen.coinTimer = 2;
        GameScreen.pointTimer = 2;
        GameScreen.damageTimer = 2;
        GameScreen.speedTimer = 2;
        GameScreen.invincibilityTimer = 2;
        int startPoints = Currency.get().balance(Currency.Type.POINTS);

        HUDmanager.createParts();
        HUDmanager.variableUpdates();

        assertEquals(startPoints + 1,Currency.get().balance(Currency.Type.POINTS));
        assertEquals(HUDmanager.gold,Currency.get().balance(Currency.Type.GOLD));
        assertEquals(HUDmanager.coolDownTimerTime,Float.parseFloat(HUDmanager.cooldownTimer.getText().substring(4)),0.01f);
        assertEquals(GameScreen.coinTimer,Float.parseFloat(HUDmanager.coinTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.pointTimer,Float.parseFloat(HUDmanager.pointTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.damageTimer,Float.parseFloat(HUDmanager.damTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.speedTimer,Float.parseFloat(HUDmanager.speedTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.invincibilityTimer,Float.parseFloat(HUDmanager.invTimer.getText().substring(0)),0.01f);

        HUDmanager.timeCounter = 0;
        Ship.burstTimer = 0;
        GameScreen.coinTimer = 0;
        GameScreen.pointTimer = 0;
        GameScreen.damageTimer = 0;
        GameScreen.speedTimer = 0;
        GameScreen.invincibilityTimer = 0;

        HUDmanager.variableUpdates();

        assertEquals(startPoints + 1,Currency.get().balance(Currency.Type.POINTS));
        assertEquals(GameScreen.coinTimer,Float.parseFloat(HUDmanager.coinTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.pointTimer,Float.parseFloat(HUDmanager.pointTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.damageTimer,Float.parseFloat(HUDmanager.damTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.speedTimer,Float.parseFloat(HUDmanager.speedTimer.getText().substring(0)),0.01f);
        assertEquals(GameScreen.invincibilityTimer,Float.parseFloat(HUDmanager.invTimer.getText().substring(0)),0.01f);
    }



    }


