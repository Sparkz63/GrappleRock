package gameObjects;

import static game.Params.*;
import game.Params;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import util.Camera;
import util.GRMouse;
import gameObjects.CameraController;
import util.InputHandler;

enum State{Normal, Creating, Moving, Modifying}

public class LevelEditor {
	//private static ArrayList<Obstacle> Terrain.obstacles = new ArrayList<Obstacle>();
	private static ArrayList<Vec2> inputVertices = new ArrayList<Vec2>();
	
	//private static Camera camera = new Camera(projectionWidth / 2.0f, projectionHeight / 2.0f);
	//private static CameraController cameraController = new CameraController(camera);
	
	private static Vec2 obstaclePosition = new Vec2();
	private static Vec2 movingOffset = new Vec2();
	private static State state = State.Normal;
	
	private static int grabbedObstacle = -1;
	
	private LevelEditor(){
		//Private constructor completely prevents instantiation
	}
	
	public static void initialize(){
		//GRMouse.setCamera(camera);
		
		//We'll be using the left shift, l key, and return keys
		InputHandler.watchKey(Keyboard.KEY_LSHIFT);
		InputHandler.watchKey(Keyboard.KEY_RETURN);
		InputHandler.watchKey(Keyboard.KEY_L);
		
		glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
	}
	
	public static void update(long deltaTime){
		//Update input flags
		InputHandler.update();
		
		if(InputHandler.keyDownEvent(Keyboard.KEY_TAB))
			editorMode = false;
		
		CameraController.followKeys(deltaTime);
		
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
				movingOffset.x = GRMouse.x() - Terrain.obstacles.get(grabbedObstacle).getPosition().x;
				movingOffset.y = GRMouse.y() - Terrain.obstacles.get(grabbedObstacle).getPosition().y;
				
				state = State.Moving;
			}
		}
		
		//Delete the currently selected obstacle
		if(InputHandler.keyDownEvent(Keyboard.KEY_L) && grabbedObstacle != -1){
			delObstacle();
		}
	}
	
	public static void movingUpdate(){
		//Moving state
		
		Vec2 newPosition = new Vec2(GRMouse.x() - movingOffset.x, GRMouse.y() - movingOffset.y);
		newPosition = snapToGrid(newPosition);
		
		Terrain.obstacles.get(grabbedObstacle).setPosition(newPosition);

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
			inputVertices.clear();
			state = State.Normal;
		}
	}
	
	public static void render(){
		glClear(GL_COLOR_BUFFER_BIT);
		
		Camera.adjustViewMatrix();
		
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
		for(int a = 0; a < Terrain.obstacles.size(); a++)
			Terrain.obstacles.get(a).render();
	}
	
	public static void renderVertices(){
		//Render vertices while creating an obstacle
		
		glColor4f(editorObstacleCreationColor4f[0], editorObstacleCreationColor4f[1], editorObstacleCreationColor4f[2], editorObstacleCreationColor4f[3]);
		glLineWidth(editorGridLineWidth);
		glBegin(GL_LINES);
		for(int a = 0; a < inputVertices.size() - 1; a++) {
			glVertex2f(inputVertices.get(a).x + obstaclePosition.x, inputVertices.get(a).y + obstaclePosition.y);
			glVertex2f(inputVertices.get(a+1).x + obstaclePosition.x, inputVertices.get(a+1).y + obstaclePosition.y);
		}
		
		if (inputVertices.size() > 0) {
			glVertex2f(inputVertices.get(inputVertices.size() - 1).x + obstaclePosition.x, inputVertices.get(inputVertices.size() - 1).y + obstaclePosition.y);
			Vec2 snappedMousePosition = snapToGrid(new Vec2 (GRMouse.x(), GRMouse.y()));
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
		// Render snap grid for Terrain.obstacles
		
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
		
		while(a < Terrain.obstacles.size() && !Terrain.obstacles.get(a).testPoint(new Vec2(GRMouse.x(), GRMouse.y())))
			a++;
		
		if(a < Terrain.obstacles.size())
			return a;
		else
			return -1;
	}
	
	public static void startAddingObstacle(){
		//Self-explanatory
		
		inputVertices.clear();
	}
	
	public static void addObstacle(){
		//Create a new obstacle using the current set of vertices
		
		//We need at least 3 vertices to define a polygon
		if(inputVertices.size() < 3)
			return;
		
		//Convert ArrayList into a regular array
		Vec2[] verts = inputVertices.toArray(new Vec2[inputVertices.size()]);
		
		Terrain.obstacles.add(new Obstacle(obstaclePosition.x, obstaclePosition.y, verts));

		inputVertices.clear();
	}
	
	public static void addVertex(){
		//Add a vertex at current mouse location

		//Snap mouse position to grid
		Vec2 temp = new Vec2(GRMouse.x(), GRMouse.y());

		//If this is the first vertex, then use it as the obstacle position
		if(inputVertices.size() == 0)
			obstaclePosition = snapToGrid(new Vec2(GRMouse.x(), GRMouse.y()));
		
		temp = snapToGrid(temp);	
		temp.x -= obstaclePosition.x;
		temp.y -= obstaclePosition.y;
		
		//Add localized vertex (that is, relative to the obstacle's position)
		inputVertices.add(temp);
	}
	
	public static void delObstacle(){
		//Delete an obstacle that has been selected
		
		//Delete currently grabbed obstacle
		Terrain.obstacles.remove(grabbedObstacle);
	}
}
