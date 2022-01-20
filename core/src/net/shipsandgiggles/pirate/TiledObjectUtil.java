package net.shipsandgiggles.pirate;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;

public class TiledObjectUtil {
    public static void parseTiledObjectLayer(World world, MapObjects objects){
        for(MapObject object : objects){
            Shape shape;

            if(object instanceof PolylineMapObject){
                shape = createPolyLine((PolylineMapObject) object);
            } else {
                continue;
            }

            Body body;
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(def);
            body.createFixture(shape, 1);
            shape.dispose();
        }
    }

    private static ChainShape createPolyLine(PolylineMapObject polyline){
        float[] verticies = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVerticies = new Vector2[verticies.length / 2];

        for (int i = 0; i < worldVerticies.length; i++){
            worldVerticies[i] = new Vector2(verticies[i*2]/ Configuration.PixelPerMeter, verticies[i * 2 + 1] / Configuration.PixelPerMeter);
        }

        ChainShape cs = new ChainShape();
        cs.createChain(worldVerticies);
        return cs;
    }
}
