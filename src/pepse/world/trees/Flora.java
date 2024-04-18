package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the generation and placement of flora (trees) within the game world, based on terrain.
 */
public class Flora {
    private final WindowController windowController; // Controller for managing window-related operations.
    private final Random random = new Random(); // Random number generator for determining flora placement.
    private Terrain terrain; // Reference to the terrain, used for placing flora based on ground height.
    GameObjectCollection gameObjects; // Collection of all game objects for adding new flora objects.

    /**
     * Constructor for creating a Flora object.
     *
     * @param windowController The controller for the game window.
     * @param terrain The terrain object, used for determining ground heights for tree placement.
     * @param gameObjects Collection where new flora objects will be added.
     */
    public Flora(WindowController windowController, Terrain terrain, GameObjectCollection gameObjects) {
        this.windowController = windowController;
        this.terrain = terrain;
        this.gameObjects = gameObjects;
    }

    /**
     * Generates trees within a specified range along the x-axis of the game world.
     * Trees are placed at random intervals based on a predefined chance.
     *
     * @param minX The minimum x-coordinate (inclusive) where trees can start being placed.
     * @param maxX The maximum x-coordinate (inclusive) for tree placement.
     * @return A list of GameObjects representing the created trees.
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        List<GameObject> trees = new ArrayList<>();
        // Iterate through the range in increments of the block size to check for potential tree locations.
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            // There's a 10% chance to plant a tree at each step.
            if (random.nextFloat() < 0.1) {
                // Calculate the ground height at this x-coordinate,
                // aligning it to the grid defined by Block.SIZE.
                float groundHeight = (float) Math.floor(
                        terrain.groundHeightAt(x) / Block.SIZE) * Block.SIZE;
                // Create a new Tree object at the calculated
                // position with predefined dimensions and leaf size.
                Tree tree = new Tree(new Vector2(x, groundHeight)
                        , 180, 25, 25, 4,
                        windowController, gameObjects);
                // Add the created tree to the list of trees.
                trees.add(tree);
            }
        }
        return trees; // Return the list of trees placed in the range.
    }
}

