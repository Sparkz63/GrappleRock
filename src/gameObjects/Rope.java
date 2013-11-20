package gameObjects;

import static game.Params.pixelsToMeters;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RopeJointDef;

import static game.Global.*;
import static game.Params.*;
import game.Game;
import util.Renderer;
import static org.lwjgl.opengl.GL11.*;

public class Rope{
	private Vec2 	_head, 			//Stored in pixels. Head is the "grapple" part of the rope
					_tail, 			//Stored in meters. Tail is where it connects to the player (follows the player's position)
					normDirection;	//Stored in pixels. Normalized vector in the direction the rope was fired
	
	private Body playerBody, obstacleBody;
	
	private Joint joint;			//The rope joint
	
	private boolean attached;		//Whether rope is attached to obstacle yet
	
	public Rope(){
		_head = new Vec2();
		_tail = new Vec2();
		normDirection = new Vec2();
	}
	
	public void initialize(Body player, Vec2 direction){
		
		//If there was a previous joint, destroy it
		if(joint != null)
			Game.world.destroyJoint(joint);
		
		
		playerBody = player;
		attached = false;
		//Store reference to player's position in _tail. Can't convert to pixels without overwriting the reference
		_tail = playerBody.getPosition();
		
		
		_head = _tail.clone().mul(metersToPixels);
		normDirection = direction.sub(tailPixels());
		normDirection.normalize();
	}
	
	//Because _head and _tail are stored in different coordinate spaces, I decided to just make functions that access them explicitly
	//in each coordinate space to save headaches
			
	private Vec2 tailMeters(){
		return _tail;
	}
	
	private Vec2 tailPixels(){
		return _tail.mul(metersToPixels);
	}
	
	private Vec2 headMeters(){
		return _head.mul(pixelsToMeters);
	}
	
	private Vec2 headPixels(){
		return _head;
	}
	
	public void update(long deltaTime){
		if(attached)
			attachedUpdate();
		else
			detachedUpdate();
	}
	
	public void attachedUpdate(){
		
	}
	
	public void detachedUpdate(){
		
		//Move the head along. The multiplicative factor (75) is completely arbitrary at this time. It controls the speed of the rope firing
		_head.x += normDirection.x * 75;
		_head.y += normDirection.y * 75;
		
		checkAttachment();
	}
	
	public void checkAttachment(){
		//Check if rope head has run into an obstacle yet.
		//This function definitely needs some work, because it allows the rope to sometimes pass over an obstacle if
		//the movement interval is larger than the width of the obstacle
		
		int a;
		
		//Test whether head lies inside any obstacles. After the for loop, "a" gives the index of the obstacle (if any)
		for(a = 0; a < Terrain.obstacles.size(); a++)
			if(Terrain.obstacles.get(a).box.getFixtureList().testPoint(headMeters()))
				break;
		
		//If "a" is a valid index. Invalid implies that there were no collisions
		if(a < Terrain.obstacles.size()){
			attached = true;
			obstacleBody = Terrain.obstacles.get(a).box;
			createJoint();
		}
	}
	
	public void createJoint(){
		//Make a rope joint between the player and the obstacle
		
		RopeJointDef rj = new RopeJointDef();
		rj.bodyA = playerBody;
		rj.bodyB = obstacleBody;
		
		//Player anchor is just at the origin. In the future this will probably be the local position of the player's arm
		rj.localAnchorA.set(0, 0);
		
		//head - obstaclePosition gives the localized head with respect to the obstacle
		rj.localAnchorB.set(headMeters().sub(obstacleBody.getPosition()));
		
		//Set maxLength as the current rope length
		rj.maxLength = headMeters().sub(tailMeters()).length();
		
		joint = Game.world.createJoint(rj);
	}
	
	public void render(){
		glBegin(GL_LINES);
		glVertex2f(headPixels().x, headPixels().y);
		glVertex2f(tailPixels().x, tailPixels().y);
		glEnd();
	}
	
	public void destroy(){
		
	}
	
}