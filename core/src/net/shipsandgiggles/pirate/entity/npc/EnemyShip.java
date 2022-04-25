package net.shipsandgiggles.pirate.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.BallsManager;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Enemy Ship
 * This is the class to control the enemy ship entities.
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public class EnemyShip extends NPC{
    // World data
    public World world;

    /**
     * This is the class to control the construction of enemy ships
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param maximumHealth : Maximum health of the college, used for combat
     * @param world : World data
     * */
    public EnemyShip(Body body, Sprite texture, float boundingRadius, Location location, int maximumHealth, World world) {
        super(body,boundingRadius, texture, location, maximumHealth, (int) texture.getHeight(), (int) texture.getWidth());

        // Instantiating a body
        this.body = body;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(location.getX(), location.getY());

        // Creation of the fixture and body
        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Enemy; // Telling it what category it is
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        setBody(body);
        this.cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        this.world = world;

        // Creation of a hitbox detector
        this.hitBox = new Rectangle((int)location.getX(),(int)location.getY(), 600, 600);
    }

    /** Kills the ship body */
    public void removeFromWorld(){
        // Kills off the body
        if(dead && !removed){
            world.destroyBody(this.body);
            removed = true;
        }
    }
}
