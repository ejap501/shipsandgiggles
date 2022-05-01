package net.shipsandgiggles.pirate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

/**
 * Ball Manager
 * Manages all the combined cannonball firing data
 *
 * @author Team 23
 * @version 1.0
 */
public class BallsManager {
    // Main data store
    public static int i = 0;
    public static ArrayList<CannonBall> listOfBalls = new ArrayList<CannonBall>();

    /** Increments removal counter */
    public static void removeNext(){i++;}

    /**
     * Creates a new cannonball
     *
     * @param world : World data
     * @param position : Ball location of the object in the world
     * @param target : Target location of the object in the world
     * @param multiplier : Damage multiplier
     * @param cannonBallSprite : Image used for the object
     * @param categoryBits : Category of the object
     * @param maskBit : Object mask
     * @param groupIndex : Position in group
     */
    public static void createBall(World world, Vector2 position, Vector2 target, int multiplier, Sprite cannonBallSprite , short categoryBits, short maskBit, short groupIndex){
        // Creates a cannonball at an angle towards the target
        CannonBall ball = new CannonBall(world, cannonBallSprite, multiplier, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), position, target, categoryBits, maskBit, groupIndex); /**creates a new cannonball class */

        // Adds the cannonball to the array of cannonballs to manage them
        listOfBalls.add(ball);
    }

    /**
     * Creates a new cannonball
     *
     * @param world : World data
     * @param position : Ball location of the object in the world
     * @param angle : Angle fired
     * @param cannonBallSprite : Image used for the object
     * @param categoryBits : Category of the object
     * @param maskBit : Object mask
     * @param groupIndex : Position in group
     */
    public static void createBallAtAngle(World world, Vector2 position, int multiplier, float angle, Sprite cannonBallSprite , short categoryBits, short maskBit, short groupIndex){
        // Creates cannon ball at a given angle
        CannonBall ball = new CannonBall(world, cannonBallSprite, multiplier, (int) cannonBallSprite.getWidth(), (int) cannonBallSprite.getHeight(), position, angle, categoryBits, maskBit, groupIndex);

        // Adds the cannonball to the array of cannonballs to manage them
        listOfBalls.add(ball);
    }

    /**
     * Updates the state of each cannon ball
     *
     * @param batch : The batch of spite data
     */
    public static void updateBalls(Batch batch){
        // Updates each ball
        listOfBalls.forEach((balls) -> balls.update(batch));

        // Checks if the ball has reached its maximum range and removes it
        for (int d = i; d > 0; d--){
            listOfBalls.remove(0);
        }
        i = 0;
    }
}
