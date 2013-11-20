package game;

import org.jbox2d.common.Vec2;
import static game.Params.*;

public class Global {
	
	// Convert all pixel measurements to meters in an array of Vec2
	public static Vec2[] vectorPixelsToMeters(Vec2[] vecs){
		Vec2[] vectors = vecs.clone();
		
		for(int a = 0; a < vectors.length; a++){
			vectors[a].x *= pixelsToMeters;
			vectors[a].y *= pixelsToMeters;
		}
		return vectors;	
	}
	
	public static Vec2 vectorPixelsToMeters(Vec2 vec){
		Vec2 vector = vec.clone();
		
		vector.x *= pixelsToMeters;
		vector.y *= pixelsToMeters;

		return vector;	
	}
	
	public static float dist(Vec2 v1, Vec2 v2){
		return (float)Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y)* (v1.y - v2.y));
	}
}
