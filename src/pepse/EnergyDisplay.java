package pepse;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * Represents an energy display in the game, visually showing an energy level as a percentage.
 * Extends {@link GameObject} to allow easy integration into the game's rendering and update system.
 */
public class EnergyDisplay extends GameObject {

    private TextRenderable textRenderable; // Renderable component to display text on the screen.

    /**
     * Constructor for creating an instance of EnergyDisplay.
     *
     * @param topLeftCorner The top-left corner where the energy display should be positioned on the screen.
     * @param dimensions The size of the energy display.
     * @param energy The initial energy level to be displayed, expressed as a percentage.
     */
    public EnergyDisplay(Vector2 topLeftCorner, Vector2 dimensions, int energy) {
        // Calls the parent GameObject constructor, initializing this object with a TextRenderable.
        super(topLeftCorner, dimensions, new TextRenderable(energy + "%"));
        // Store the TextRenderable for later updates.
        this.textRenderable = new TextRenderable(energy + "%");
    }

    /**
     * Updates the displayed energy level.
     *
     * @param energy The new energy level to be displayed, expressed as a percentage.
     *               This method updates the text to reflect the new energy level.
     */
    public void updateEnergy(int energy) {
        // Update the textRenderable component to display the new energy level.
        textRenderable.setString(energy + "%");
        // Update the renderer to use the updated textRenderable, ensuring the displayed text is current.
        renderer().setRenderable(this.textRenderable);
    }
}
