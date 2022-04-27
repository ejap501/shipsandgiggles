package net.shipsandgiggles.pirate.entity.impl.obstacles;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.UUID;

import net.shipsandgiggles.pirate.entity.Ship;
import net.shipsandgiggles.pirate.entity.Location;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.entity.impl.collectible.Coin;
import net.shipsandgiggles.pirate.entity.impl.collectible.powerUp;
import net.shipsandgiggles.pirate.entity.impl.college.AlcuinCollege;
import net.shipsandgiggles.pirate.entity.impl.college.ConstantineCollege;
import net.shipsandgiggles.pirate.entity.impl.college.GoodrickeCollege;
import net.shipsandgiggles.pirate.entity.impl.college.LangwithCollege;
import net.shipsandgiggles.pirate.entity.shop.Shop;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Stone
 * Class to configure data relevant to stone obstacles
 * New based on existing layout from other class
 *
 * @author Team 23 : Modified (Some methods copied from other classes)
 * @author Team 22 : Edward Poulter
 * @version 1.0
 */
public class Stone extends Solid{
    // World data
    public World world;

    /**
     * This is the class to control the stone obstacles
     * Used for the construction of stone objects
     *
     * @param texture : Sprite image data
     * @param location : Coordinate data for position in world
     * @param world : World data
     * */
    public Stone(Sprite texture, Location location, World world) {
        super(UUID.randomUUID(), Solid.Type.STONE, texture, location, texture.getHeight(), texture.getWidth());

        // Instantiating a body
        Body body;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(location.getX(), location.getY());


        // Creation of the body
        def.fixedRotation = true;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((texture.getWidth() / 2f) / PIXEL_PER_METER, (texture.getHeight() / 2f) / PIXEL_PER_METER);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = Configuration.Cat_Collect; // Telling it what category it is
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        this.body = body;
        this.world = world;

        //Creation of a hitbox detector
        this.hitBox = new com.badlogic.gdx.math.Rectangle((int)location.getX() / PIXEL_PER_METER - 4,(int)location.getY() / PIXEL_PER_METER - 4 , ((texture.getWidth()) / PIXEL_PER_METER) + 8, ((texture.getHeight()) / PIXEL_PER_METER) + 8);
    }

    /** Sets animation to none */
    @Override
    public boolean perform() {
        return false;
    }

    /**
     * Draws the stone object in the world
     * Data assigned to the batch
     *
     * @param batch : The batch of sprite data
     * */
    @Override
    public void draw(Batch batch) {
        // Draws stone
        this.getSkin().setPosition(this.getBody().getPosition().x * PIXEL_PER_METER - (this.getSkin().getWidth() / 2f), this.getBody().getPosition().y * PIXEL_PER_METER - (this.getSkin().getHeight() / 2f)); /**sets position of the stone */
        this.getSkin().setRotation((float) Math.toDegrees(this.getBody().getAngle()));
        batch.begin();
        this.getSkin().draw(batch);
        batch.end();
    }

    /**
     * Checks for contact with the alcuin body
     *
     * @param alcuin : The alcuin college body
     * */
    public boolean alcuinCheck(AlcuinCollege alcuin){
        return alcuin.hitBox.overlaps(this.hitBox) || alcuin.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the constantine body
     *
     * @param constantine : The constantine college body
     * */
    public boolean constantineCheck(ConstantineCollege constantine){
        return constantine.hitBox.overlaps(this.hitBox) || constantine.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the goodricke body
     *
     * @param goodricke : The goodricke college body
     * */
    public boolean goodrickeCheck(GoodrickeCollege goodricke){
        return goodricke.hitBox.overlaps(this.hitBox) || goodricke.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the langwith body
     *
     * @param langwith : The langwith college body
     * */
    public boolean langwithCheck(LangwithCollege langwith){
        return langwith.hitBox.overlaps(this.hitBox) || langwith.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the power-up body
     *
     * @param power : The power-up body
     * */
    public boolean powerUpCheck(powerUp power){
        return power.hitBox.overlaps(this.hitBox) || power.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the stone body
     *
     * @param stone : The stone body
     * */
    public boolean stoneCheck(Stone stone){
        return stone.hitBox.overlaps(this.hitBox) || stone.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the coin body
     *
     * @param coin : The coin body
     * */
    public boolean coinCheck(Coin coin){
        return coin.hitBox.overlaps(this.hitBox) || coin.hitBox.contains(this.hitBox);
    }

    /**
     * Checks for contact with the shop body
     *
     * @param shop : The shop body
     * */
    public boolean shopCheck(Shop shop){
        return shop.hitBox.overlaps(this.hitBox) || shop.hitBox.contains(this.hitBox);
    }

    /** Oversees the death of the stone body */
    @Override
    public void death(){
        // Does nothing if already dead
        if(this.dead) return;

        // Kills off the body
        world.destroyBody(body);
        this.dead = true;
    }

    @Override
    public void shootPlayer(Ship player) {

    }

}
