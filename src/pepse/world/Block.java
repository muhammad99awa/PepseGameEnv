package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single block in the game world, typically used to construct terrain or static obstacles.
 * Blocks are immovable and serve as fundamental units of the game environment.
 */
public class Block extends GameObject {
    /**
     * constants that are used through the class
     */
    public static final int SIZE = 30;

    /**
     * Constructor for creating a Block object.
     *
     * @param topLeftCorner The top-left corner position of the block in the game world.
     * @param renderable The visual representation of the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        // Initialize the GameObject with a position, size
        // (defined by the static SIZE constant), and a renderable.
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);

        // Configure physics to prevent other objects from intersecting this block.
        physics().preventIntersectionsFromDirection(Vector2.ZERO);

        // Set the mass of the block to IMMOVABLE_MASS
        // to ensure it does not move or get affected by physics forces.
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
