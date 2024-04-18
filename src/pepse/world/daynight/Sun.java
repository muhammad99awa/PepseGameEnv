package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the creation and animation of the sun within the game world
 * , contributing to the day-night cycle.
 * The sun is represented as a yellow oval that moves
 * in an arc across the game window to simulate its path across the sky.
 */
public class Sun {

    private static final Color SUN_COLOR = Color.YELLOW; // Color of the sun.

    /**
     * Creates and returns a GameObject representing the sun, complete with its movement across the sky.
     *
     * @param windowDimensions The dimensions of the game window. Used to position and move the sun.
     * @param cycleLength      The length of the day-night cycle, influencing how quickly the sun moves.
     * @return A GameObject configured to represent and animate the sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Initial position of the sun at the start of the day.
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() * 1 / 4);
        // The center point of the sun's cycle, used to calculate its movement arc.
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() * 2 / 3);
        // Create the sun GameObject with an oval shape and yellow color.
        GameObject sun = new GameObject(initialSunCenter, new Vector2(60, 60),
                new OvalRenderable(SUN_COLOR));

        // Ensure the sun remains in a fixed position relative to the camera's coordinates.
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        // Tag the object for easy identification.
        sun.setTag("sun");

        // Initialize a Transition to animate the sun's movement in an arc across the sky.
        new Transition<>(sun, // The GameObject being animated.
                angle -> sun.setCenter(initialSunCenter.subtract(cycleCenter) // Calculate the new
                        // position based on rotation angle.
                        .rotated(angle).add(cycleCenter)), 0f, // Start the transition
                // at 0 degrees rotation.
                360f, // End the transition at 360 degrees rotation, completing a full circle.
                Transition.LINEAR_INTERPOLATOR_FLOAT, // Use a linear interpolator
                // for steady movement.
                cycleLength, // The duration of the sun's movement, representing a
                // full day-night cycle.
                Transition.TransitionType.TRANSITION_LOOP, // Loop the transition to
                // continuously simulate the sun's movement.
                null // No completion callback.
        );
        return sun; // Return the configured sun GameObject.
    }
}
