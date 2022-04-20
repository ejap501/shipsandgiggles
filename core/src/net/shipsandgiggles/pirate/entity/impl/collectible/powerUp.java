package net.shipsandgiggles.pirate.entity.impl.collectible;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.currency.Currency;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.collectible.Plunder;

import java.util.UUID;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

public class powerUp extends Plunder {
    public World world;
    public static String powerUpType;
    /** this is the class to control the coin*/



    /** construction of coins*/

    public powerUp(Sprite texture, Location location, String type, World world) {
        super(UUID.randomUUID(), Plunder.Type.COINS, texture, location, texture.getHeight(), texture.getWidth());
        powerUpType = type;
        Body body;
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(location.getX(), location.getY());

        /** creation of the body*/

        def.fixedRotation = true;
        body = world.createBody(def);
        this.body = body;
        this.world = world;

        this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX() / PIXEL_PER_METER,(int)location.getY() / PIXEL_PER_METER, ((texture.getWidth()) / PIXEL_PER_METER) + 4, ((texture.getHeight()) / PIXEL_PER_METER) + 4);
    }

    @Override
    public boolean perform() {
        return false;
    }

    @Override
    public void draw(Batch batch) {
        if(dead){return;}
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the college */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();
        this.getSkin().draw(batch);
        batch.end();

    }

    @Override
    public void shootPlayer(Ship player) {

    }

    @Override
    public void death(){
        if(this.dead) return;
        world.destroyBody(body);
        this.dead = true;
    }
    public String getPowerUpType(){return powerUpType;}
    public boolean rangeCheck(Ship player){
        return player.hitBox.overlaps(this.hitBox);
    }
}
