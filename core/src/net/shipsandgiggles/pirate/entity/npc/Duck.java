package net.shipsandgiggles.pirate.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.BallsManager;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class Duck extends NPC{
    public World world;

    /** this is the class to control the ducks*/



    /** construction of college*/

    public Duck(Sprite texture, Location location, float maximumHealth, World world) {
        super(UUID.randomUUID(), Type.NPC, texture, location, maximumHealth, texture.getHeight(), texture.getWidth());

        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(location.getX(), location.getY());

        /** creation of the body*/

        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Enemy; /**telling it what category it is */
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        this.world = world;


    }

    public void removeFromWorld(){
        if(dead && !removed){
            world.destroyBody(this.body);
            removed = true;
        }
    }

    @Override
    public boolean perform() {
        return false;
    }

    @Override
    public void draw(Batch batch) {
        if(dead && !removed){
            removeFromWorld();
            return;
        }
        else if (dead && removed){
            return;
        }
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the ship */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();

        this.getSkin().draw(batch);//draws college

        /**changes the npc health colour based on the amount it has */
        if(this.getHealth() > (this.getMaximumHealth() * 0.51)){
            batch.setColor(Color.GREEN);
        }
        else if(this.getHealth() > (this.getMaximumHealth() * 0.25)){
            batch.setColor(Color.ORANGE);
        }
        else{
            batch.setColor(Color.RED);
        }

        batch.draw(healthBar, this.body.getPosition().x - this.getSkin().getWidth() ,this.body.getPosition().y + this.getSkin().getHeight()/2 + 10, (float) (this.getSkin().getWidth() * 2 * (this.getHealth() /this.getMaximumHealth())), 5);
        batch.setColor(Color.WHITE);
        batch.end();

    }

    @Override
    public void shootPlayer(Ship player) {

    }

}
