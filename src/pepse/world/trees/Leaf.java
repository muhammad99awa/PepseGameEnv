package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;
/**
 * Represents a leaf object in the game, typically part of a tree.
 * Leaves have a simple visual representation and can react to interactions such as jumping.
 */
public class Leaf extends GameObject {

    /**
     * Constructor for creating a Leaf object.
     *
     * @param position The position of the leaf in the game world.
     * @param size The size of the leaf. Assumes a square shape, so both width and height are the same.
     */
    public Leaf(Vector2 position, float size) {
        // Initialize the GameObject with the specified position, size,
        // and a green rectangle as its visual representation.
        super(position, new Vector2(size, size),
                new RectangleRenderable(new Color(50, 200, 30)));
    }

    /**
     * Reacts to a jump interaction by rotating the leaf.
     * This method is intended to be called, for example, when the player's avatar jumps,
     * causing the leaves on the tree to rotate as a visual effect.
     */
    public void reactToJump() {
        // Retrieve the current rotation angle of the leaf.
        float currentAngle = this.renderer().getRenderableAngle();
        // Increase the rotation angle by 90 degrees to simulate a reaction to the jump.
        this.renderer().setRenderableAngle(currentAngle + 90);
    }
}