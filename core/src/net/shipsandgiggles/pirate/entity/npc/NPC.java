package net.shipsandgiggles.pirate.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityAi;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.EntityType;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * NPC data that allows us to perform animations / fights more easily.
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public abstract class NPC extends EntityAi {
    // Main data store
    public Body body;
    public Rectangle hitBox;
    public float counter = 0;
    public boolean dead = false;
    public boolean removed = false;
    public Texture healthBar = new Texture("models/bar.png");
    public Sprite cannonBallSprite =  new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));

    // Timers
    public float timer = 0f;
    public float cooldownTimer = 1f;

    /**
     * Instantiates the NPC type
     *
     * @param boundingRadius : Radius of the AI detection
     * @param texture : Image used for the object
     * @param location : Position of the object in the world
     * @param maximumHealth : Maximum health of the entity
     * @param height : Height of the NPC
     * @param width : Width of the NPC
     * */
    public NPC(Body body,float boundingRadius, Sprite texture, Location location, int maximumHealth, int height, int width) {
        super(body,boundingRadius, texture, maximumHealth,  location, width, height);
    }

    /** Fetches the body of the NPC */
    public Body getBody(){
        return this.body;
    }

    public void death() {
        // Terminate method if already dead
        if(this.dead) return;

        // Gives instant money and score if the player decides to kill them
        Currency.get().give(Currency.Type.POINTS, 25);
        Currency.get().give(Currency.Type.GOLD, 50);
        this.dead = true;
    }
}

