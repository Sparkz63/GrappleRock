package game;

import org.jbox2d.common.Vec2;
import static game.Params.*;

public class Global {
	public static Vec2[] vectorPixelsToMeters(Vec2[] vectors){
		for(int a = 0; a < vectors.length; a++){
			vectors[a].x *= pixelsToMeters;
			vectors[a].y *= pixelsToMeters;
		}
		return vectors;	
	}
}
