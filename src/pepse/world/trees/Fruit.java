package pepse.world.trees;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.WindowController;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.awt.*;
/**
 * Represents a fruit object within the game world, which can interact with other game objects.
 * Fruits are designed to change color upon interaction, such as a jump action from the player's avatar.
 */
public class Fruit extends GameObject {
    private static final int ENERGY_GAIN = 10; // The amount of
    // energy an avatar can gain from interacting with the fruit.
    private GameObjectCollection gameObjects; // Reference to the
    // collection of all game objects, for potential interactions.
    private Vector2 position; // The position of the fruit in the game world.
    private float size; // The size of the fruit.
    private Color color; // The current color of the fruit.

    /**
     * Constructor for creating a new Fruit object.
     *
     * @param position The initial position of the fruit in the game world.
     * @param size The size of the fruit.
     * @param gameObjects Reference to the collection of game objects for interaction purposes.
     */
    public Fruit(Vector2 position, float size, GameObjectCollection gameObjects) {
        // Call to the GameObject constructor to initialize
        // the fruit with a position, size, and an oval shape colored red.
        super(position, new Vector2(size, size), new OvalRenderable(Color.RED));
        this.gameObjects = gameObjects;
        this.position = position;
        this.size = size;
        this.color = Color.RED; // Initial color of the fruit is set to red.
    }

    /**
     * Gets the current position of the fruit.
     *
     * @return The current position of the fruit.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Gets the size of the fruit.
     *
     * @return The size of the fruit.
     */
    public float getSize() {
        return size;
    }

    /**
     * Reacts to a jump interaction by changing the color of the fruit.
     * If the fruit is red, it changes to yellow, and vice versa.
     */
    public void reactToJump() {
        // Check the current color of the fruit and switch it.
        if (this.color == Color.RED) {
            this.renderer().setRenderable(
                    new OvalRenderable(Color.YELLOW)); // Change to yellow if currently red.
            this.color = Color.YELLOW;
        } else {
            this.renderer().setRenderable(
                    new OvalRenderable(Color.RED)); // Change back to red otherwise.
            this.color = Color.RED;
        }
    }
}


