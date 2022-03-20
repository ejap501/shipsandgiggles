package net.shipsandgiggles.pirate.entity.collectible;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.Entity;
import net.shipsandgiggles.pirate.entity.EntityType;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

import java.util.UUID;


public class Plunder extends Entity {
    public Body entityBody;
    public Sprite texture;
    public World world;
    public boolean dead = false;
    public Vector2 deathPosition = new Vector2(0,0);
    public Rectangle hitBox;

    public Plunder(Sprite texture, Location location, float height, float width) {
        super(UUID.randomUUID(), texture, location, EntityType.PLUNDER, 1, height, width);
        /** constructor*/
        this.texture = texture;

        /**Creation of Body */
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(location.getX(), location.getY());
        bodyDef.fixedRotation = true;


        this.entityBody = GameScreen.world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2) / Configuration.PIXEL_PER_METER, (height / 2) / Configuration.PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef. density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Player; // Change this
        shape.dispose();
        this.hitBox = new Rectangle(location.getX(), location.getY(), texture.getWidth(), texture.getHeight());
        this.world = GameScreen.world;
    }

    @Override
    public void draw(Batch batch) {
        if(dead) {return;}
        batch.begin();
        this.getTexture().draw(batch);
        batch.end();
        this.hitBox.setPosition(this.getEntityBody().getPosition());
    }

    public void death() { /** checks if plunder is collected if not then sets them as collected and gets their last position*/
        if(this.dead) return;
        this.deathPosition.x = this.getEntityBody().getPosition().x;
        this.deathPosition.y = this.getEntityBody().getPosition().y;
        this.dead = true;
    }

    /** gets the position of the coin*/
    public Vector2 getPosition() {
        Vector2 position = new Vector2();
        position.x = super.getLocation().getX();
        position.y = super.getLocation().getY();
        return position;
    }

    public Body getEntityBody() {
        return this.entityBody;
    }

    public Sprite getTexture(){return this.texture;}

    public void setTexture(Sprite texture){
        this.texture = texture;
    }

    @Override
    public void shootPlayer(Ship player) {}
}
