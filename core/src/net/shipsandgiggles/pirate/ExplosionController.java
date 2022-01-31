package net.shipsandgiggles.pirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

public class ExplosionController {
    public static final float FRAME_LENGTH = 0.1f;
    public static final int OFFSIZE = 2;
    public static final int SIZE = 20;

    private static Animation anim = null;
    Vector2 position;
    float stateTime;

    public boolean remove = false;

    public ExplosionController(Vector2 position){
        System.out.println("dd");
        this.position = new Vector2(position.x - OFFSIZE, position.y - OFFSIZE);
        stateTime = 0;

        if(anim == null) anim = new Animation(FRAME_LENGTH, TextureRegion.split(new Texture("models/Explosion.png"), SIZE, SIZE)[0]);
    }

    public void update(){
        stateTime += Gdx.graphics.getDeltaTime();
        if(anim.isAnimationFinished(stateTime)) remove = true;
    }

    public void draw(Batch batch){
        batch.begin();
        batch.draw((TextureRegion) anim.getKeyFrame(stateTime), position.x, position.y);
        batch.end();
    }
}
