package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.WindowController;
import danogl.util.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents a collection of fruit objects positioned around a given area, simulating fruits on a tree.
 * This class provides functionality to randomly generate fruits within a specified bounding box.
 */
public class Fruits implements Iterable<GameObject> {
    /**
     * constants that are used through the class
     */
    // Constant vector that is used to plant the fruits
    public static final Vector2 VECTOR_OF_40 = new Vector2(40, 40);
    // A list to hold all the generated fruit objects.
    private List<GameObject> fruits;
    private static final Random random = new Random(); // Random number
    // generator for determining fruit placement.

    /**
     * Constructor for the Fruits collection.
     * Randomly places fruit objects within a defined area around a central position.
     *
     * @param position The central position around which fruits will be generated.
     * @param size The size of each fruit object.
     * @param leavesNumber The number of leaves, indirectly influences the density of fruits.
     * @param gameObjects A reference to the game object collection, for adding fruits to the game world.
     */
    Fruits(Vector2 position, float size, int leavesNumber, GameObjectCollection gameObjects) {
        fruits = new ArrayList<>();

        // Calculate offsets to define the area around the central position where fruits can be placed.
        int leftXOffset = (int) position.subtract(VECTOR_OF_40).x();
        int rightXOffset = (int) position.add(VECTOR_OF_40).x();
        int yDownOffset = (int) position.subtract(VECTOR_OF_40).y();
        int yUpOffset = (int) position.add(new Vector2(0, 0)).y();

        // Iterate over the defined area, randomly placing fruits based on a probability.
        for (int i = leftXOffset; i < rightXOffset; i += 10) {
            for (int j = yDownOffset; j < yUpOffset; j += 10) {
                if (random.nextFloat() < 0.1f) { // 10% chance to place a fruit at each location.
                    Fruit fruit = new Fruit(new Vector2(i, j), size, gameObjects);
                    fruits.add(fruit);
                }
            }
        }
    }

    /**
     * Provides an iterator over the fruit objects, enabling iteration through each fruit in the collection.
     *
     * @return An Iterator over the GameObject instances representing the fruits.
     */
    @Override
    public Iterator<GameObject> iterator() {
        return fruits.iterator();
    }
}
