package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class BallsManager {

    public static int i = 0;

    public static void removeNext(){i++;}

    static ArrayList<CannonBall> listOfBalls = new ArrayList<CannonBall>();
    public static void createBall(World world, Vector2 position, Vector2 target, Sprite cannonBallSprite , short categoryBits, short maskBit, short groupIndex){
        cannonBallSprite = new Sprite(new Texture(Gdx.files.internal("models/cannonBall.png")));
        CannonBall ball = new CannonBall(world, cannonBallSprite, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), position, target, categoryBits, maskBit, groupIndex);
        listOfBalls.add(ball);
    }
    public static void createBallAtAngle(World world, Vector2 position, float angle, Sprite cannonBallSprite , short categoryBits, short maskBit, short groupIndex){
        CannonBall ball = new CannonBall(world, cannonBallSprite, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), position, angle, categoryBits, maskBit, groupIndex);
        listOfBalls.add(ball);
    }

    public static void updateBalls(Batch batch){
        listOfBalls.forEach((balls) -> balls.update(batch));
        for (int d = i; d > 0; d--){
            listOfBalls.remove(0);
        }
        i = 0;
    }
}
