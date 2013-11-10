package gameObjects;

import game.Params;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import util.InputHandler;

public class LevelEditor {
	private static ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private static ArrayList<Vec2> inputVertices = new ArrayList<Vec2>();
	
	private static Vec2 obstaclePosition = new Vec2();
	private static boolean addingObstacle = false;
	
	private LevelEditor(){
		//Private constructor completely prevents instantiation
	}
	
	public static void initialize(){
		InputHandler.create();
		
		//We'll be using the left shift and return keys
		InputHandler.watchKey(Keyboard.KEY_LSHIFT);
		InputHandler.watchKey(Keyboard.KEY_RETURN);
	}
	
	public static void update(){
		//Update input flags
		InputHandler.update();
		
		//If user presses left shift, begin adding an obstacle
		if(InputHandler.keyDownEvent(Keyboard.KEY_LSHIFT)){
			startAddingObstacle();
			System.out.println("shift down");
		}
		
		//If user releases left shift, (try to) create the new obstacle
		if(InputHandler.keyUpEvent(Keyboard.KEY_LSHIFT)){
			addObstacle();
			System.out.println("shift up");
		}
		
		//If user presses enter, print out the number of obstacles (unnecessary at this point)
		if(InputHandler.keyDownEvent(Keyboard.KEY_RETURN))
			System.out.println(obstacles.size());
		
		//If full click while adding an obstacle, add a new vertex
		if(addingObstacle && InputHandler.leftMouseDown())
			addVertex();
	}
	
	public static void render(){
		glClear(GL_COLOR_BUFFER_BIT);
		
		renderObstacles();
		
		renderVertices();
		
		Display.update();						// Render buffer to screen
		Display.sync(Params.targetFPS);	// sync to xx frames per second
	}
	
	public static void renderObstacles(){
		for(int a = 0; a < obstacles.size(); a++)
			obstacles.get(a).render();
	}
	
	public static void renderVertices(){
		//Render vertices while creating an obstacle
		
		glColor4f(0, 1, 0, 1);
		glBegin(GL_POINTS);
		for(int a = 0; a < inputVertices.size(); a++)
			glVertex2f(inputVertices.get(a).x + obstaclePosition.x, inputVertices.get(a).y + obstaclePosition.y);
		glEnd();
	}
	
	public static void startAddingObstacle(){
		//Self-explanatory
		
		addingObstacle = true;
		inputVertices.clear();
	}
	
	public static void addObstacle(){
		//Create a new obstacle using the current set of vertices
		
		//We need at least 3 vertices to define a polygon
		if(inputVertices.size() < 3)
			return;
		
		//Convert ArrayList into a regular array
		Vec2[] verts = inputVertices.toArray(new Vec2[inputVertices.size()]);
		
		obstacles.add(new Obstacle(obstaclePosition.x, obstaclePosition.y, verts));
		
		inputVertices.clear();
	}
	
	public static void addVertex(){
		//Add a vertex at current mouse location
		
		//Catch mouse position preemptively, so it doesn't change while we're using it
		Vec2 temp = new Vec2(Mouse.getX(), Mouse.getY());
		
		//If this is the first vertex, then use it as the obstacle position
		if(inputVertices.size() == 0)
			obstaclePosition = temp;
		
		//Add localized vertex (that is, relative to the obstacle's position)
		inputVertices.add(new Vec2(temp.x - obstaclePosition.x, temp.y - obstaclePosition.y));
	}
}
