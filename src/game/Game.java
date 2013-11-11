package game;

import gameObjects.IGameObject;
import gameObjects.LevelEditor;
import gameObjects.Obstacle;
import gameObjects.SampleBox;
import gameObjects.SampleObject;

import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static game.Params.*;

public class Game {
	
	private static long 	lastFrame = getTime();	// Used to calculate Delta Time
	private static long 	lastFPS = getTime();	// Used to calculate FPS
	private static int		fpsCounter = 0;			// Used to calculate FPS
	
	private static int 		deltaTime = 0;			// Time since last frame
	private static int		calculatedFPS = 0;		// Frames Per Second
	
	public static World world = new World(new Vec2(0f, -10f));				// jBox2d World
    public static final Set<IGameObject> gameObjects = new HashSet<IGameObject>();	// GameObjects
    
    public static Obstacle obst; //Temporary, for debugging
	
	// update game objects, render, manage timing
	public static void startGameLoop() {
		initialize();
		
		while (!Display.isCloseRequested()) {
			deltaTime = getDeltaTime();		// update timers: delta time and fps counter
			updateFPS();
			
			update();
			
			render();
		}
			
		Display.destroy();
	}
	
	public static void startEditorLoop(){
		LevelEditor.initialize();
		
		while (!Display.isCloseRequested()) {
			deltaTime = getDeltaTime();		// update timers: delta time and fps counter
			updateFPS();
			
			LevelEditor.update();
			LevelEditor.render();
		}
			
		Display.destroy();
	}
	
	// Initialize things
	private static void initialize() {
		gameObjects.add(new SampleBox(20, 33, false));
		gameObjects.add(new SampleBox(21, 15, true));
		
		gameObjects.add(new Obstacle(450, 30, new Vec2 [] {
				new Vec2(0, 0), new Vec2(90, 30), new Vec2(90, -30)
		}));
		
		Vec2 vertices [] = new Vec2 [7];
		float a = 0;
		for (int i = 0; a < Math.toRadians(360); a += Math.toRadians(52), i++) 
		       vertices[i] = new Vec2( (float) Math.sin(a) * 30, (float) Math.cos(a) * 30);
		
		
		gameObjects.add(new Obstacle(425, 200, vertices));

		//obst = new Obstacle
		
		for(IGameObject gameObject: gameObjects){
			gameObject.initialize();
		}
	}
	
	// update all game objects
	private static void update() {
	
		for (IGameObject gameObject : gameObjects){
			gameObject.update(deltaTime);
		}
		
		world.step((float) deltaTime / 1000f, 8, 3);
	}
	
	// render all game objects
	private static void render() {
		
		glClear(GL_COLOR_BUFFER_BIT);
		
		for (IGameObject gameObject : gameObjects){
			gameObject.render();
		}
		
        Display.setTitle("FPS: " + calculatedFPS); 	// Render FPS counter
        
		Display.update();						// Render buffer to screen
		Display.sync(Params.targetFPS);	// sync to xx frames per second
	}
	
	// Set up display / LWJGL
	private static void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Params.screenWidth, Params.screenHeight));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// Set up Projection Matrix
	private static void setUpMatrices () {
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, Params.projectionWidth, 0, Params.projectionHeight, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
	}
	
	// return System time in milliseconds
	public static long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	// calculate amount of time since last frame in milliseconds. CALL ONLY ONCE PER FRAME.
	public static int getDeltaTime() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	// update fps counter
	public static void updateFPS() {
	    if (getTime() - lastFPS > 1000) {
	        calculatedFPS = fpsCounter;
	    	fpsCounter = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fpsCounter++;
	}
	
	public static void main(String[] argv) {
		setUpDisplay();
		setUpMatrices();
		if(editorMode)
			startEditorLoop();
		else
			startGameLoop();
	}
}
