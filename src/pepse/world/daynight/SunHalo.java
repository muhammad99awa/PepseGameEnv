package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Facilitates the creation of a sun halo effect in the game world,
 * enhancing the visual appearance of the sun.
 * The halo is represented as a semi-transparent, yellow oval surrounding the sun, creating a glow effect.
 */
public class SunHalo {
    // The color of the halo, including alpha for transparency.
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    /**
     * Creates and returns a GameObject representing the sun's halo.
     * The halo is positioned and sized relative to the sun, creating a glowing effect around it.
     *
     * @param sun The GameObject representing the sun around which the halo is created.
     * @return A GameObject configured to represent the sun's halo.
     */
    public static GameObject create(GameObject sun) {
        // Create the sunHalo GameObject with a position relative to the sun, increased size,
        // and an oval shape with the specified halo color.
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(), new Vector2(120, 120),
                new OvalRenderable(HALO_COLOR));

        // Ensure the halo remains in a fixed position relative to the camera's coordinates,
        // like the sun.
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Tag the object for easy identification.
        sunHalo.setTag("sun halo");

        return sunHalo; // Return the configured sun halo GameObject.
    }
}

