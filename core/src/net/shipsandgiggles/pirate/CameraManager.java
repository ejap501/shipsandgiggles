package net.shipsandgiggles.pirate;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Camera;

import static net.shipsandgiggles.pirate.conf.Configuration.PIXEL_PER_METER;

/**
 * Camera Manager
 * Manages the camera
 *
 * @author Team 23
 * */
public class CameraManager {
	/**
	 * Locks on to something with no smoothing
	 *
	 * @param camera : The viewable screen
	 * @param target : Target coordinates in world
	 */
	public static void lockOn(Camera camera, Vector2 target) {
		Vector3 cameraPosition = camera.position;
		cameraPosition.x = target.x * PIXEL_PER_METER;
		cameraPosition.y = target.y * PIXEL_PER_METER;
		camera.position.set(cameraPosition);
		camera.update();
	}

	/**
	 * Applies smoothing to camera "lerping"
	 *
	 * @param camera : The viewable screen
	 * @param target : Target coordinates in world
	 * @param lerpValue : Lerp weight
	 */
	public static void lerpOn(Camera camera, Vector2 target, float lerpValue) {
		Vector3 cameraPosition = camera.position;
		cameraPosition.x = cameraPosition.x + (target.x - cameraPosition.x) * lerpValue * PIXEL_PER_METER;
		cameraPosition.y = cameraPosition.y + (target.y - cameraPosition.y) * lerpValue * PIXEL_PER_METER;
		camera.position.set(cameraPosition);
		camera.update();
	}
}
