package gameObjects;

import static game.Params.*;
import game.Params;
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
	
	private static int grabbedObstacle = -1;
	
	private LevelEditor(){
		//Private constructor completely prevents instantiation
	}
	
	public static void initialize(){
		InputHandler.create();
		
		//We'll be using the left shift, d key, and return keys
		InputHandler.watchKey(Keyboard.KEY_LSHIFT);
		InputHandler.watchKey(Keyboard.KEY_RETURN);
		InputHandler.watchKey(Keyboard.KEY_D);
		
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
		
		//Delete the currently selected obstacle
		if(InputHandler.keyDownEvent(Keyboard.KEY_D) && grabbedObstacle != -1){
			delObstacle();
		}
				
		//If user presses enter, print out the number of obstacles (unnecessary at this point)
		if(InputHandler.keyDownEvent(Keyboard.KEY_RETURN))
			System.out.println(obstacles.size());
	}
	
	public static void movingUpdate(){
		//Moving state
		
		Vec2 newPosition = new Vec2(InputHandler.mouse().x - movingOffset.x, InputHandler.mouse().y - movingOffset.y);
		newPosition = snapToGrid(newPosition);
		
		obstacles.get(grabbedObstacle).setPosition(newPosition);

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
		
		renderGrid();
		
		Display.update();						// Render buffer to screen
		Display.sync(targetFPS);				// sync to xx frames per second
	}
	
	private static Vec2 snapToGrid(Vec2 position) {
		// Snaps a given position to the nearest node on a coordinate plane with smallest unit Params.editorSnapValue
		
		int x = (int) (position.x);
		int y = (int) (position.y);
		
		int xMod = x % editorSnapValue;
		int yMod = y % editorSnapValue;
		
		if (xMod > editorSnapValue / 2)
			x += editorSnapValue - xMod;
		else
			x -= xMod;
		
		if (yMod > Params.editorSnapValue / 2)
			y += editorSnapValue - yMod;
		else
			y -= yMod;
		
		return new Vec2(x, y);
	}

	public static void renderObstacles(){
		for(int a = 0; a < obstacles.size(); a++)
			obstacles.get(a).render();
	}
	
	public static void renderVertices(){
		//Render vertices while creating an obstacle
		
		glColor4f(obstacleOutlineColor4f[0], obstacleOutlineColor4f[1], obstacleOutlineColor4f[2], obstacleOutlineColor4f[3]);
				
		glBegin(GL_LINES);
		for(int a = 0; a < inputVertices.size() - 1; a++) {
			glVertex2f(inputVertices.get(a).x + obstaclePosition.x, inputVertices.get(a).y + obstaclePosition.y);
			glVertex2f(inputVertices.get(a+1).x + obstaclePosition.x, inputVertices.get(a+1).y + obstaclePosition.y);
		}
		
		if (inputVertices.size() > 0) {
			glVertex2f(inputVertices.get(inputVertices.size() - 1).x + obstaclePosition.x, inputVertices.get(inputVertices.size() - 1).y + obstaclePosition.y);
			Vec2 snappedMousePosition = snapToGrid(new Vec2 (InputHandler.mouse()));
			glVertex2f(snappedMousePosition.x, snappedMousePosition.y);
		}

		
		if (inputVertices.size() > 2) {
			glVertex2f(inputVertices.get(inputVertices.size() - 1).x + obstaclePosition.x, inputVertices.get(inputVertices.size() - 1).y + obstaclePosition.y);
			glVertex2f(inputVertices.get(0).x + obstaclePosition.x, inputVertices.get(0).y + obstaclePosition.y);
		}
		
		glEnd();
		
		glColor3f(1f, 1f, 1f);
	}
	
	private static void renderGrid() {
		// Render snap grid for obstacles
		
		glColor4f(editorSnapGridColor4f[0], editorSnapGridColor4f[1], editorSnapGridColor4f[2], editorSnapGridColor4f[3]);
		glPointSize(editorGridPointSize);
		glBegin(GL_POINTS);
		
		for (int x = 0; x < projectionWidth; x += editorSnapValue) {
			for (int y = 0; y < projectionHeight; y += editorSnapValue) {
				glVertex2i(x, y);
			}
		}
		
		glEnd();		
		glColor3f(1f, 1f, 1f);
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

		//Snap mouse position to grid
		Vec2 temp = new Vec2(InputHandler.mouse().x, InputHandler.mouse().y);

		//If this is the first vertex, then use it as the obstacle position
		if(inputVertices.size() == 0)
			obstaclePosition = snapToGrid(new Vec2(InputHandler.mouse()));
		
		temp = snapToGrid(temp);	
		temp.x -= obstaclePosition.x;
		temp.y -= obstaclePosition.y;
		
		//Add localized vertex (that is, relative to the obstacle's position)
		inputVertices.add(temp);
	}
	
	public static void delObstacle(){
		//Delete an obstacle that has been selected
		
		//Delete currently grabbed obstacle
		obstacles.remove(grabbedObstacle);
	}
}
