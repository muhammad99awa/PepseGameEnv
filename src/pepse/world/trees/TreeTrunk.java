package pepse.world.trees;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

/**
 * Represents the trunk part of a tree in the game environment.
 * It is characterized by its position, height, width, and color.
 * The trunk is immovable and can change color to simulate various effects or interactions.
 */
public class TreeTrunk extends GameObject {
    private static final Random random = new Random(); // Random object for generating color variations.
    private Vector2 position; // The position of the trunk in the game world.
    private float height; // The height of the trunk.
    private float width; // The width of the trunk.

    /**
     * Constructor for creating a TreeTrunk object.
     *
     * @param position         The initial position of the trunk in the game world.
     * @param height           The height of the trunk.
     * @param width            The width of the trunk.
     * @param windowController Controller for window-related functionalities,
     *                        not directly used but available for extensions.
     */
    public TreeTrunk(Vector2 position, float height, float width, WindowController windowController) {
        super(position, new Vector2(width, height),
                new RectangleRenderable(new Color(100, 50, 20))); // Initializes with a dark brown color.
        // Prevents other objects from intersecting with the trunk.
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        // Sets the mass to an immovable mass to ensure the trunk stays stationary.
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        this.height = height;
        this.width = width;
        this.position = position;
    }

    /**
     * Changes the color of the trunk to a random shade of brown.
     * This could simulate effects like seasons changing, damage, or other interactions.
     */
    public void changeColor() {
        // Generate a random brown shade by varying the red and green components.
        int red = 100 + random.nextInt(30); // Ensures a brownish red.
        int green = 50 + random.nextInt(20); // Adds some green for a natural wood color.
        int blue = 0; // Minimal blue component.
        Color newColor = new Color(red, green, blue);
        // Apply the new color to the trunk.
        this.renderer().setRenderable(new RectangleRenderable(newColor));
    }
}