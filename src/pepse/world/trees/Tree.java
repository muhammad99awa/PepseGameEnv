package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.util.Vector2;
/**
 * Represents a tree within the game world, including its trunk, leaves, and fruits.
 * This class manages the composition of the tree and its visual representation.
 */
public class Tree extends GameObject {
    private final TreeLeaves treeLeaves; // The leaves part of the tree.
    private final TreeTrunk trunk; // The trunk part of the tree.
    private Fruits fruits; // The fruits associated with the tree.
    private GameObjectCollection gameObjects; // Collection of game objects for adding fruits.

    /**
     * Constructs a Tree object with specified characteristics and components.
     *
     * @param basePosition The base position of the tree in the world.
     * @param treeHeight The height of the tree.
     * @param treeWidth The width of the tree's trunk.
     * @param leafSize The size of each leaf on the tree.
     * @param leavesNumber The number of leaves to be generated for the tree.
     * @param windowController A controller for window-related functionalities.
     * @param gameObjects A collection of game objects for managing the tree's components.
     */
    public Tree(Vector2 basePosition, float treeHeight, float treeWidth,
                float leafSize, int leavesNumber, WindowController windowController,
                GameObjectCollection gameObjects) {
        super(basePosition, new Vector2(treeWidth, treeHeight), null); // Initialize
        // with base position. The tree itself has no direct renderable.

        // Trunk is positioned based on the base, with its height adjusted to start from the base upwards.
        Vector2 trunkPosition = new Vector2(basePosition.x(), basePosition.y() - treeHeight);
        this.trunk = new TreeTrunk(trunkPosition, treeHeight, treeWidth, windowController);

        // Leaves are assumed to be placed at the top-center of the trunk.
        Vector2 leavesBasePosition = new Vector2(basePosition.x(), basePosition.y() - treeHeight);
        this.treeLeaves = new TreeLeaves(leavesBasePosition, leafSize, leavesNumber, windowController);

        // Fruits are generated around the leaves base position.
        this.fruits = new Fruits(leavesBasePosition, 20, 7, gameObjects);
        this.gameObjects = gameObjects; // Store reference for
        // potentially adding more components in the future.
    }

    /**
     * Gets the Fruits component of the tree.
     *
     * @return The Fruits object associated with this tree.
     */
    public Fruits getFruits() {
        return fruits;
    }

    /**
     * Gets the TreeLeaves component of the tree.
     *
     * @return The TreeLeaves object representing the tree's leaves.
     */
    public TreeLeaves getTreeLeaves() {
        return treeLeaves;
    }

    /**
     * Gets the TreeTrunk component of the tree.
     *
     * @return The TreeTrunk object representing the tree's trunk.
     */
    public TreeTrunk getTrunk() {
        return trunk;
    }
}

