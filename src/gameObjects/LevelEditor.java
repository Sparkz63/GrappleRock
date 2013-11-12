package gameObjects;

import static game.Params.*;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import util.InputHandler;

enum State{Normal, Creating, Moving, Modifying}

public class LevelEditor {
	private static ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private static ArrayList<Vec2> inputVertices = new ArrayList<Vec2>();
	
	private static Vec2 obstaclePosition = new Vec2();
	private static Vec2 movingOffset = new Vec2();
	private static boolean addingObstacle = false;
	private static State state = State.Normal;
	
	private static int grabbedObstacle;
	
	private LevelEditor(){
		//Private constructor completely prevents instantiation
	}
	
	public static void initialize(){
		InputHandler.create();
		
		//We'll be using the left shift and return keys
		InputHandler.watchKey(Keyboard.KEY_LSHIFT);
		InputHandler.watchKey(Keyboard.KEY_RETURN);
		
		glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
	}
	
	public static void update(){
		//Update input flags
		InputHandler.update();
		
		switch(state){
		case Modifying:
		case Normal:
			normalUpdate();
			break;
		case Creating:
			creatingUpdate();
			break;
		case Moving:
			movingUpdate();
			break;
		}
	}
	
	public static void normalUpdate(){
		//Normal state

		if(InputHandler.keyDownEvent(Keyboard.KEY_LSHIFT)){
			startAddingObstacle();
			state = State.Creating;
		}
		
		//Click activates Moving state
		if(InputHandler.leftMouseDown()){
			
			//If click occurs on an obstacle
			if((grabbedObstacle = grabObstacle()) != -1){
				
				//Get the vector offset between obstacle's position and mouse position
				movingOffset.x = InputHandler.mouse().x - obstacles.get(grabbedObstacle).getPosition().x;
				movingOffset.y = InputHandler.mouse().y - obstacles.get(grabbedObstacle).getPosition().y;
				
				state = State.Moving;
			}
		}
		
		//If user presses enter, print out the number of obstacles (unnecessary at this point)
		if(InputHandler.keyDownEvent(Keyboard.KEY_RETURN))
			System.out.println(obstacles.size());
				
	}
	
	public static void movingUpdate(){
		//Moving state
		
		//Set position of obstacle relative to mouse (using previously calculated offset)
		obstacles.get(grabbedObstacle).setPosition(new Vec2(InputHandler.mouse().x - movingOffset.x, InputHandler.mouse().y - movingOffset.y));
		
		if(InputHandler.leftMouseUp())
			state = State.Normal;
	}
	
	public static void creatingUpdate(){
		
		//If full click while adding an obstacle, add a new vertex
		if(InputHandler.leftMouseDown())
			addVertex();
		
		//If user releases left shift, (try to) create the new obstacle
		if(InputHandler.keyUpEvent(Keyboard.KEY_LSHIFT)){
			addObstacle();
			state = State.Normal;
		}
	}
	
	public static void render(){
		glClear(GL_COLOR_BUFFER_BIT);
		
		renderObstacles();
		
		renderVertices();
		
		Display.update();						// Render buffer to screen
		Display.sync(targetFPS);	// sync to xx frames per second
	}
	
	public static void renderObstacles(){
		for(int a = 0; a < obstacles.size(); a++)
			obstacles.get(a).render();
	}
	
	public static void renderVertices(){
		//Render vertices while creating an obstacle
		
		if(inputVertices.size() < 1)
			return;
		
		glColor4f(0, 0, 0, 1);

		glBegin(GL_LINES);
		for(int a = 0; a < inputVertices.size(); a++){
			glVertex2f(inputVertices.get(a).x + obstaclePosition.x, inputVertices.get(a).y + obstaclePosition.y);
			glVertex2f(inputVertices.get((a + 1) % inputVertices.size()).x + obstaclePosition.x, inputVertices.get((a + 1) % inputVertices.size()).y + obstaclePosition.y);
		}
		glVertex2f(inputVertices.get(inputVertices.size() - 1).x + obstaclePosition.x, inputVertices.get(inputVertices.size() - 1).y + obstaclePosition.y);
		glVertex2f(InputHandler.mouse().x, InputHandler.mouse().y);
		glEnd();
	}
	
	public static int grabObstacle(){
		//Test whether the mouse location is inside an obstacle, and if so, returns the index of that obstacle
		//(if not, returns -1)
		
		int a = 0;
		
		while(a < obstacles.size() && !obstacles.get(a).testPoint(new Vec2(InputHandler.mouse().x, InputHandler.mouse().y)))
			a++;
		
		if(a < obstacles.size())
			return a;
		else
			return -1;
	}
	
	public static void startAddingObstacle(){
		//Self-explanatory
		
		addingObstacle = true;
		inputVertices.clear();
	}
	
	public static void addObstacle(){
		//Create a new obstacle using the current set of vertices
		
		addingObstacle = false;
		
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

		//If this is the first vertex, then use it as the obstacle position
		if(inputVertices.size() == 0)
			obstaclePosition = new Vec2(InputHandler.mouse());

		//Add localized vertex (that is, relative to the obstacle's position)
		inputVertices.add(new Vec2(InputHandler.mouse().x - obstaclePosition.x, InputHandler.mouse().y - obstaclePosition.y));
	}
}
