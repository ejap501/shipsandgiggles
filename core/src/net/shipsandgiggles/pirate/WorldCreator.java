package net.shipsandgiggles.pirate;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import net.shipsandgiggles.pirate.conf.Configuration;
import net.shipsandgiggles.pirate.screen.impl.GameScreen;

/**
 * This is the class where all boundaries and collisions are created for the map.
 * @author Ethan Alabaster
 * @version 1.0
 */
public class WorldCreator {
    /**
     * Starts the creation of the boundaries
     *
     * @param screen the screen that the boundaries are relevant for
     */
    public WorldCreator(GameScreen screen) {
        TiledMap map = screen.getMap();
        World world = screen.getWorld();
        Body body;
        Fixture fixture;

        // Object class is islands, stuff for boat to collide with
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            BodyDef bDef = new BodyDef();
            FixtureDef fDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();

            //Set position
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 , rect.getHeight() / 2);
            fDef.shape = shape;
            fDef.filter.categoryBits = Configuration.Cat_walls;
            fixture = body.createFixture(fDef);

    }
}
}
