package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.trees.Fruit;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a playable avatar character with energy-based movement and jumping abilities.
 * The avatar's energy level affects its capabilities, with interactions that can change energy levels.
 */
public class Avatar extends GameObject {

    // Constants defining the avatar's behavior and assets.
    private static final String AVATAR_IMAGE_PATH = "assets/idle_0.png"; // Default image path for the avatar.
    private static final float GRAVITY = 500; // Gravitational acceleration affecting the avatar.
    private static final float VELOCITY_X = 400; // Horizontal movement speed.
    private static final float VELOCITY_Y = -650; // Initial velocity for jumps.
    private int energy; // Current energy level of the avatar.
    private Consumer<Integer> energyUpdateCallback; // Callback to notify on energy changes.
    private ImageReader imageReader; // Utility for reading images from assets.

    // Animation assets for different states.
    private AnimationRenderable idleAnimation;
    private AnimationRenderable movingRightAnimation;
    private AnimationRenderable jumpingAnimation;

    private GameObjectCollection gameObjects; // Reference to the game's object collection for interaction.
    private List<Runnable> jumpCallbacks = new ArrayList<>(); // Callbacks triggered upon jumping.
    private UserInputListener inputListener;

    /**
     * Constructs an Avatar instance with specified parameters.
     *
     * @param pos The starting position of the avatar.
     * @param inputListener Listener for user input.
     * @param imageReader Reader for loading image assets.
     * @param energyUpdateCallback Callback for when the energy level changes.
     * @param gameObjects Collection of game objects for interaction and modification.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader,
                  Consumer<Integer> energyUpdateCallback, GameObjectCollection gameObjects) {
        super(pos, Vector2.ONES.mult(50), imageReader.readImage(AVATAR_IMAGE_PATH, true));
        this.inputListener = inputListener;
        this.energy = 100; // Initial energy level set to 100%.
        this.gameObjects = gameObjects;
        this.energyUpdateCallback = energyUpdateCallback;
        this.imageReader = imageReader;
        this.energyUpdateCallback.accept(this.energy); // Update the displayed energy initially.

        // Load animations for different avatar states.
        idleAnimation = new AnimationRenderable(loadFrames("idle", 4), 0.5f);
        movingRightAnimation = new AnimationRenderable(loadFrames("run", 6), 0.1f);
        jumpingAnimation = new AnimationRenderable(loadFrames("jump", 4), 0.5f);

        // Apply gravity to the avatar for realistic falling.
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
    }

    /**
     * Loads animation frames from the assets folder.
     *
     * @param name The base name of the animation frames.
     * @param count The number of frames in the animation.
     * @return An array of Renderable objects representing the animation frames.
     */
    private Renderable[] loadFrames(String name, int count) {
        Renderable[] frames = new Renderable[count];
        for (int i = 0; i < count; i++) {
            String path = "assets/" + name + "_" + i + ".png";
            frames[i] = imageReader.readImage(path, true);
        }
        return frames;
    }

    /**
     * Updates the avatar's energy level and triggers the energy update callback.
     * Ensures the energy level stays within the bounds of 0 to 100.
     *
     * @param newEnergy The amount of energy to add or subtract.
     */
    public void updateEnergy(int newEnergy) {
        if (energy + newEnergy >= 0 && energy + newEnergy <= 100) {
            energy += newEnergy;
            energyUpdateCallback.accept(energy);
        }
    }

    /**
     * Handles interactions when the avatar collides with other objects.
     * Specifically, regenerates energy when colliding with fruits and respawns the fruits.
     *
     * @param other The object the avatar collided with.
     * @param collision Details about the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Fruit) {
            updateEnergy(+10); // Gain energy on fruit collision.
            gameObjects.removeGameObject(other); // Remove the collided fruit.
            // Schedule a task to respawn the fruit after a delay.
            new ScheduledTask(this, 30, false, () -> {
                Fruit newFruit = new Fruit(((Fruit) other).getPosition(),
                        ((Fruit) other).getSize(), gameObjects);
                gameObjects.addGameObject(newFruit, Layer.DEFAULT);
            });
        }
    }

    /**
     * Updates the avatar's state based on user input and current energy level.
     * Handles movement, jumping, and energy consumption/regeneration.
     *
     * @param deltaTime The time elapsed since the last update call.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0; // Reset horizontal velocity.
        boolean isJumping = false; // Flag to track jumping state.
        AnimationRenderable newAnimation = this.idleAnimation; // Default to idle animation.

        // Handle left movement.
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= 1) {
            xVel -= VELOCITY_X;
            updateEnergy(-1); // Consume energy for moving.
            newAnimation = this.movingRightAnimation;
            renderer().setIsFlippedHorizontally(true); // Flip animation for leftward movement.
        }

        // Handle right movement.
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy >= 1) {
            xVel += VELOCITY_X;
            updateEnergy(-1); // Consume energy for moving.
            newAnimation = this.movingRightAnimation;
            renderer().setIsFlippedHorizontally(false); // Normal orientation for rightward movement.
        }

        // Handle jumping.
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                Math.abs(getVelocity().y()) < 0.01f && energy >= 10) {
            transform().setVelocityY(VELOCITY_Y); // Apply vertical velocity for the jump.
            updateEnergy(-10); // Consume energy for jumping.
            isJumping = true;
        }

        // Apply jumping animation and trigger jump callbacks if jumping.
        if (isJumping) {
            newAnimation = this.jumpingAnimation;
            triggerJumpReactions();
        }

        // Update the avatar's animation if it has changed.
        if (renderer().getRenderable() != newAnimation) {
            renderer().setRenderable(newAnimation);
        }

        // Apply horizontal velocity.
        transform().setVelocityX(xVel);

        // Regenerate energy when idle or not actively moving, but not to exceed 100.
        if (getVelocity().y() == 0 && xVel == 0 && energy < 100) {
            updateEnergy(+1);
        }
    }

    /**
     * Adds a callback to be triggered when the avatar jumps.
     *
     * @param callback The runnable to be executed on jump.
     */
    public void addJumpCallback(Runnable callback) {
        jumpCallbacks.add(callback);
    }

    /**
     * Triggers all registered jump callbacks.
     */
    private void triggerJumpReactions() {
        for (Runnable reaction : jumpCallbacks) {
            reaction.run();
        }
    }
}


