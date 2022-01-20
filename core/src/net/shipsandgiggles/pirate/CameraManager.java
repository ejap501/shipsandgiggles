package net.shipsandgiggles.pirate;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import static net.shipsandgiggles.pirate.conf.Configuration.PixelPerMeter;

public class CameraManager {
    public static void lockOn(Camera camera, Vector2 target){
        Vector3 cameraPosition = camera.position;
        cameraPosition.x = target.x * PixelPerMeter;
        cameraPosition.y = target.y * PixelPerMeter;
        camera.position.set(cameraPosition);
        camera.update();
    }

    public static void lerpOn(Camera camera, Vector2 target, float lerpValue){
        Vector3 cameraPosition = camera.position;
        cameraPosition.x = cameraPosition.x + (target.x - cameraPosition.x) * lerpValue * PixelPerMeter;
        cameraPosition.y = cameraPosition.y + (target.y - cameraPosition.y) * lerpValue * PixelPerMeter;
        camera.position.set(cameraPosition);
        camera.update();
    }
}
