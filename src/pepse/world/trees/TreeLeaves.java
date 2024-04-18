package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Manages a collection of leaf objects on a tree, providing functionality for their generation
 * and animation to simulate natural movements.
 */
public class TreeLeaves implements Iterable<GameObject> {
    private List<GameObject> leaves; // Collection of leaf objects.
    // Random number generator for leaf placement and animation delays.
    private static final Random random = new Random();

    /**
     * Constructor for the TreeLeaves class.
     * Generates leaves at random positions within a specified area around the given position.
     *
     * @param position The central position around which leaves will be generated.
     * @param size The size of each leaf.
     * @param leavesNumber The intended number of leaves (influences generation probability).
     * @param windowController Controller for window-related functionalities,
     *                        not used here but could be necessary for extensions.
     */
    TreeLeaves(Vector2 position, float size, int leavesNumber, WindowController windowController) {
        leaves = new ArrayList<>();

        // Calculate bounds for leaf generation.
        int leftXOffset = (int) position.subtract(new Vector2(60, 60)).x();
        int rightXOffset = (int) position.add(new Vector2(70, 70)).x();
        int yDownOffset = (int) position.subtract(new Vector2(70, 70)).y();
        int yUpOffset = (int) position.add(new Vector2(70, 70)).y();

        // Generate leaves within the specified bounds.
        for (int i = leftXOffset; i < rightXOffset; i += 10) {
            for (int j = yDownOffset; j < yUpOffset; j += 10) {
                if (random.nextFloat() < 0.1f) { // 10% chance to place a leaf at each position.
                    float delay = (float) (Math.random() * 3); // Random delay for starting leaf animation.
                    Leaf leaf = new Leaf(new Vector2(i, j), size); // Create new leaf object.
                    leaves.add(leaf); // Add leaf to collection.
                    // Schedule a task to start leaf movement after a random delay.
                    new ScheduledTask(leaf, delay, false, () -> startLeafMovement(leaf, size));
                }
            }
        }
    }

    /**
     * Initiates animation for a leaf, including rotation and
     * slight size variation to simulate natural movement.
     *
     * @param leaf The leaf object to animate.
     * @param size The base size of the leaf, used for scaling animation.
     */
    private void startLeafMovement(GameObject leaf, float size) {
        // Transition for rotating the leaf.
        new Transition<Float>(leaf,
                (angle) -> leaf.renderer().setRenderableAngle(angle),
                0f, 360f, Transition.LINEAR_INTERPOLATOR_FLOAT, 20,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        // Transition for changing the leaf's size.
        new Transition<Vector2>(leaf,
                (dimensions) -> leaf.setDimensions(dimensions),
                new Vector2(size, size), new Vector2(size + 2, size + 2),
                Transition.CUBIC_INTERPOLATOR_VECTOR, 5,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Provides an iterator over the collection of leaf objects.
     *
     * @return An Iterator for the leaf objects.
     */
    @Override
    public Iterator<GameObject> iterator() {
        return leaves.iterator();
    }

    /**
     * Gets the list of all leaf objects in the collection.
     *
     * @return A List of leaf GameObjects.
     */
    public List<GameObject> getLeaves() {
        return leaves;
    }
}
