package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class ballsManager {

    public static int i = 0;

    public static void removeNext(){i++;}

    static ArrayList<createNewBall> listOfBalls = new ArrayList<createNewBall>();
    public static void createBall(World world, Vector2 position, Vector2 target, Sprite cannonBallSprite , short categoryBits, short maskBit, short groupIndex){
        createNewBall ball = new createNewBall(world, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), position, target, categoryBits, maskBit, groupIndex);
        listOfBalls.add(ball);
    }

    public static void updateBalls(){
        listOfBalls.forEach((balls) -> balls.update());
        for (int d = i; d > 0; d--){
            listOfBalls.remove(0);
        }
        i = 0;
    }
}
