package util;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

class KeyEvent{
	private boolean isDown, downEvent, upEvent;
	
	public KeyEvent(){
		isDown = false;
		downEvent = false;
		upEvent = false;
	}
	
	public void updateEvents(boolean newState){
		//This method is called constantly. newState should be the most current state of the key (down == true, up == false)
		
		//If the key was down but is now up, then fire the up event
		if(isDown && !newState)
			upEvent = true;
		
		//If vice versa, fire the down event
		else if(!isDown && newState)
			downEvent = true;
		
		//Update the main flag
		isDown = newState;
	}
	
	public boolean isDown(){
		return isDown;
	}
	
	public boolean downEvent(){
		//(TestAndSet algorithm) Return the value of the downEvent flag, but set it to false first.
		//This prevents an event from being handled more than once
		
		boolean temp = downEvent;
		downEvent = false;
		upEvent = false;
		return temp;
	}
	
	public boolean upEvent(){
		//Same as above
		
		boolean temp = upEvent;
		upEvent = false;
		downEvent = false;
		return temp;
	}
}

public class InputHandler {
	
	private static boolean leftButton = false;
	private static boolean clickRegistered = false;
	private static Set<Integer> watching = new HashSet<Integer>();
	private static KeyEvent keyEvents[] = new KeyEvent[256];
	private static KeyEvent leftMouseEvent = new KeyEvent();
	
	public static void create(){
		//Basically a constructor that must be called explicitly before InputHandler can be used
		
		for(int a = 0; a < keyEvents.length; a++)
			keyEvents[a] = new KeyEvent();
	}
	
	private InputHandler(){
		//Private constructor prevents instantiation
	}
	
	public static void update(){
		checkMouse();
		
		checkKeys();
	}
	
	public static void checkKeys(){
		//Update event flags for all keys being watched
		
		//Iterate over the set
		for(Iterator<Integer> i = watching.iterator(); i.hasNext();){
			
			//We need to use 'a' twice, but don't want to call i.next() twice
			int a = i.next();
			
			keyEvents[a].updateEvents(Keyboard.isKeyDown(a));
		}
	}
	
	public static void checkMouse(){
		leftMouseEvent.updateEvents(Mouse.isButtonDown(0));
	}
	
	public static void watchKey(int key){
		//Add this key to the list of keys that we care about. The reason for this is efficiency.
		//The alternative would be to watch the state of every single key on the keyboard. Fuck that
		
		watching.add(key);
	}

	public static boolean leftMouseDown(){
		return leftMouseEvent.downEvent();
	}
	
	public static boolean leftMouseUp(){
		return leftMouseEvent.upEvent();
	}
	
	public static boolean keyIsDown(int key){
		return keyEvents[key].isDown();
	}
	
	public static boolean keyDownEvent(int key){
		return keyEvents[key].downEvent();
	}
	
	public static boolean keyUpEvent(int key){
		return keyEvents[key].upEvent();
	}
}
