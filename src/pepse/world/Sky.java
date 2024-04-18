package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * Utility class for creating a sky background in the game world.
 * The sky is represented as a colored rectangle that spans the entire game window.
 * It is fixed to the camera's coordinates, ensuring it remains constant and doesn't scroll
 * with the camera movement, simulating a realistic sky.
 */
public class Sky {
    // The default sky color, a light blue.
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Creates and returns a sky GameObject that covers the entire game window.
     *
     * @param windowDimensions The dimensions of the game window, used to size the sky appropriately.
     * @return A GameObject representing the sky, spanning the entire window.
     */
    public static GameObject create(Vector2 windowDimensions){
        // Create a new GameObject positioned at (0,0) with size equal to the window dimensions.
        GameObject sky = new GameObject(
                Vector2.ZERO, // The top-left corner of the sky, set to the origin.
                windowDimensions, // The size of the sky, set to fill the entire game window.
                new RectangleRenderable(BASIC_SKY_COLOR)); // The renderable
        // component, a rectangle filled with the sky color.

        // Set the coordinate space to CAMERA_COORDINATES to ensure the sky moves with the camera,
        // simulating a consistent backdrop that doesn't scroll with game objects.
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Optionally set a tag for the sky GameObject for identification or filtering purposes.
        sky.setTag("sky");

        return sky;
    }
}
