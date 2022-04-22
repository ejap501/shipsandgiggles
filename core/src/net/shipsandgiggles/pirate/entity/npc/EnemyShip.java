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
     * @param playerShips : The player body
     * */
    public EnemyShip(Sprite texture, Location location, float maximumHealth, World world,  Ship playerShips) {
        super(UUID.randomUUID(), Type.NPC, texture, location, maximumHealth, texture.getHeight(), texture.getWidth());

        // Instantiating a body
        Body body;
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
        this.body = body;
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

    /** Sets animation to none */
    @Override
    public boolean perform() {
        return false;
    }

    /**
     * Draws the ship object in the world
     * Data assigned to the batch
     *
     * @param batch : The batch of sprite data
     * */
    @Override
    public void draw(Batch batch) {
        // Terminates if dead
        if(dead){return;}

        // Draws ship
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the ship */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));

        // Draws health bar and ship
        batch.setColor(healthBarColor());
        batch.begin();
        this.getSkin().draw(batch);
        batch.draw(healthBar, this.body.getPosition().x - this.getSkin().getWidth() ,this.body.getPosition().y + this.getSkin().getHeight()/2 + 10, healthBarWidth(), 5);
        batch.setColor(Color.WHITE);
        batch.end();

    }

    /**
     * Controls the shooting of the player
     * Shoots if the player is close enough
     *
     * @param player : The player body
     */
    @Override
    public void shootPlayer(Ship player) {
        /*
         Checks if the ship is dead or not
         Increments gold and points if not dead
         */
        if(this.health == 1 && !this.dead && !player.dead){
            this.counter += Gdx.graphics.getDeltaTime();
            if(this.counter >= 1){
                Currency.get().give(Currency.Type.POINTS, 3);
                Currency.get().give(Currency.Type.GOLD, 5);
                this.counter = 0;
            }
        }

        // Creates shot and shoots
        if(this.hitBox.overlaps(player.hitBox) && timer <= 0 && !this.dead && this.health != 1 && !player.dead) {/** creates shot and shoots*/
            BallsManager.createBall(this.world, new Vector2(this.body.getPosition().x, this.body.getPosition().y), new Vector2(player.getEntityBody().getPosition().x, player.getEntityBody().getPosition().y), 1, cannonBallSprite, (short)(Configuration.Cat_Enemy | Configuration.Cat_College), Configuration.Cat_Player, (short) 0);
            this.timer = this.cooldownTimer;
        }

        // Ensures that there is a cool down between every shot
        else if(timer <= 0) this.timer = 0;
        else this.timer -= Gdx.graphics.getDeltaTime();
    }

    /**
     * Controls the colour of the health bar when alive
     * Changes between "GREEN" "ORANGE" "RED"
     */
    public Color healthBarColor(){
        // Determines the colour of the health bar
        if(this.getHealth() > (this.getMaximumHealth() * 0.51)){
            return Color.GREEN;
        } else if(this.getHealth() > (this.getMaximumHealth() * 0.25)){
            return Color.ORANGE;
        } else{
            return Color.RED;
        }
    }

    /**
     * Controls the width of the health bar when alive
     * Based on percentage of remaining health
     */
    public float healthBarWidth(){
        float value = (float) (this.getSkin().getWidth() * 2 * (this.getHealth() /this.getMaximumHealth()));
        return value;
    }
}
