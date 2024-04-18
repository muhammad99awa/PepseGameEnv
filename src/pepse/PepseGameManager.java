package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Component;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.*;

import java.util.List;

/**
 * Extends GameManager to create a custom game initialization and setup for the PEPSE game.
 * This class manages the game's initialization, including the creation of the game's environment,
 * the avatar, and interactive objects like trees, leaves, and fruits.
 */
public class PepseGameManager extends GameManager {
    /**
     * Main method to start the game.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game elements such as the terrain, sky, sun, avatar, and interactive objects.
     *
     * @param imageReader      Interface to read images from disk.
     * @param soundReader      Interface to read sounds from disk.
     * @param inputListener    Listener for user input.
     * @param windowController Controls aspects of the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // Create the sky and add it to the background layer.
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        // Generate the terrain and populate it with blocks.
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), 0);
        List<Block> blocks = terrain.createInRange(0, (int) Math.floor(
                windowController.getWindowDimensions().x()));
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        // Create the night overlay and add it to the background.
        GameObject night = Night.create(windowController.getWindowDimensions(), 30);

        gameObjects().addGameObject(night, Layer.BACKGROUND);
        // Create the sun and its halo, then align the halo to the sun's position
        GameObject sun = Sun.create(windowController.getWindowDimensions(), 60);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        GameObject sunHalo = SunHalo.create(sun);

        sunHalo.addComponent(new Component() {
            @Override
            public void update(float deltaTime) {
                sunHalo.setCenter(sun.getCenter());
            }
        });
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);


        // Initialize the avatar's position and create the avatar object.
        Vector2 initialPosition = new Vector2(0, terrain.groundHeightAt(0) - 30);

        GameObject energyDisplay = new EnergyDisplay(Vector2.ZERO, new Vector2(40, 40), 100);
        gameObjects().addGameObject(energyDisplay);
        GameObject avatar = new Avatar(initialPosition, inputListener, imageReader, newEnergy ->
                ((EnergyDisplay) energyDisplay).updateEnergy(newEnergy), gameObjects());
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        // Create flora (trees) and set interactions with the avatar.
        Flora flora = new Flora(windowController, terrain, gameObjects());
        List<GameObject> trees = flora.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (GameObject tree : trees) {
            //Create tree trunk
            gameObjects().addGameObject(((Tree) tree).getTrunk(), Layer.DEFAULT);
            // add callback to react to avatar jump
            ((Avatar) avatar).addJumpCallback(((Tree) tree).getTrunk()::changeColor);
            //create tree leaves
            TreeLeaves leaves = ((Tree) tree).getTreeLeaves();
            for (GameObject leaf : leaves) {


                gameObjects().addGameObject(leaf, Layer.STATIC_OBJECTS);
                // add callback to react to avatar jump

                ((Avatar) avatar).addJumpCallback(() -> ((Leaf) leaf).reactToJump());
            }
            Fruits fruits = (((Tree) tree).getFruits());
            for (GameObject fruit : fruits) {
                //create tree fruits
                gameObjects().addGameObject(fruit, Layer.DEFAULT);
                // add callback to react to avatar jump

                ((Avatar) avatar).addJumpCallback(() -> ((Fruit) fruit).reactToJump());
            }

        }


    }
}

