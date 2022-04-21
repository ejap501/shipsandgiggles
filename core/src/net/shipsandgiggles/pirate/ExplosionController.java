package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture;

/**
 * Explosion controller
 * Controller for all explosion and creation of them
 *
 * @author Team 23
 * */
public class ExplosionController {
    // Main data store
    Vector2 position;
    float stateTime;
    public boolean remove = false;
    public static final int SIZE = 20;
    public static final int OFFSIZE = 2;
    private static Animation anim = null;
    public static final float FRAME_LENGTH = 0.1f;

    /**
     * Constructor of explosion
     *
     * @param position : Location of explosion
     * */
    public ExplosionController(Vector2 position){
        this.position = new Vector2(position.x - OFFSIZE, position.y - OFFSIZE); /** setting position */
        stateTime = 0;

        if(anim == null) anim = new Animation(FRAME_LENGTH, TextureRegion.split(new Texture("models/Explosion.png"), SIZE, SIZE)[0]); /** setting textures */
    }

    /** Updates explosion */
    public void update(){
        stateTime += Gdx.graphics.getDeltaTime();
        if(anim.isAnimationFinished(stateTime)) remove = true;
    }

    /**
     * Draws explosion
     *
     * @param batch : The batch of sprite data
     * */
    public void draw(Batch batch){
        batch.begin();
        batch.draw((TextureRegion) anim.getKeyFrame(stateTime), position.x, position.y);
        batch.end();
    }
}
