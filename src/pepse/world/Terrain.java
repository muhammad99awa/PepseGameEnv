package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import danogl.gui.rendering.Renderable;
import pepse.ColorSupplier;
/**
 * Generates and manages terrain within the game world.
 * The terrain is created based on Perlin noise to ensure a natural-looking variation in height.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74); // Base color for the terrain.
    private static final int TERRAIN_DEPTH = 20; // The depth of the terrain, in blocks.
    private float groundHeightAtX0; // Initial ground height at the start (x=0) of the terrain.
    private NoiseGenerator noiseGenerator; // Utility for generating noise-based terrain heights.

    /**
     * Constructor for creating a Terrain object.
     *
     * @param windowDimensions The dimensions of the game window. Used to determine the initial ground height.
     * @param seed A seed for the noise generator to ensure reproducible terrain patterns.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        // Calculate the initial ground height as two-thirds the height of the game window.
        groundHeightAtX0 = windowDimensions.y() * 2 / 3;
        // Initialize the noise generator with the provided seed and a base ground height.
        noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Calculates the ground height at a given x-coordinate.
     *
     * @param x The x-coordinate at which to calculate the ground height.
     * @return The calculated ground height at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        // Adjust the base ground height based on the noise value at the given x-coordinate.
        return groundHeightAtX0 + (float) this.noiseGenerator.noise(x, Block.SIZE * 7);
    }

    /**
     * Creates a range of terrain blocks between two x-coordinates.
     *
     * @param minX The minimum x-coordinate (inclusive).
     * @param maxX The maximum x-coordinate (inclusive).
     * @return A list of Block objects representing the terrain between minX and maxX.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        // Iterate over the range in steps equal to the block size, to create one block per step.
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            // Calculate the ground height at this x-coordinate, rounding down to the nearest block size.
            float groundHeight = (float) Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE;
            // Create blocks from the ground height down to the terrain depth.
            for (int y = 0; y < TERRAIN_DEPTH; y++) {
                Vector2 blockPosition = new Vector2(x, groundHeight + y * Block.SIZE);
                // Create a new block at the calculated position,
                // coloring it with an approximation of the base ground color.
                Block block = new Block(blockPosition,
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                blocks.add(block);
            }
        }
        return blocks; // Return the list of created terrain blocks.
    }
}