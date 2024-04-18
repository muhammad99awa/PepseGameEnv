package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Provides functionality to simulate the effect of night in the game world.
 * It does this by creating a semi-transparent overlay that covers the game window,
 * with the opacity level changing to simulate the night's progression.
 */
public class Night {
    // The color of the night overlay.
    private static final Color NIGHT_COLOR = Color.decode("#000000");
    // The maximum opacity for the night effect, representing midnight.
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates and returns a GameObject representing the night effect.
     * The GameObject's opacity transitions back and forth to simulate the changing night intensity.
     *
     * @param windowDimensions The dimensions of the game window. Used to size the night overlay.
     * @param cycleLength The length of a full day-night cycle in the game,
     *                   influencing how quickly the night effect transitions.
     * @return A GameObject configured to represent the night effect over a day-night cycle.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        // Create a new GameObject positioned at (0,0) with size equal to
        // the window dimensions and colored to represent night.
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(NIGHT_COLOR));
        // Ensure the night overlay remains fixed relative to the camera's view,
        // covering the entire game window.
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        // Tag the object for easy identification if needed.
        night.setTag("night");
        // Initialize a Transition for the opacity of the night overlay.
        new Transition<>(
                night, // The GameObject being modified (the night overlay).
                night.renderer()::setOpaqueness, // The method to adjust (setting the opacity).
                0f, // Initial value: fully transparent at the beginning of the cycle.
                MIDNIGHT_OPACITY, // Final value: semi-transparent at "midnight".
                Transition.CUBIC_INTERPOLATOR_FLOAT, // Use a cubic interpolator for smooth transition.
                cycleLength, // Duration of the transition for half the cycle length, then it reverses.
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Transition type
                // to oscillate between day and night.
                null // No completion callback needed.
        );

        return night; // Return the configured GameObject.
    }
}
