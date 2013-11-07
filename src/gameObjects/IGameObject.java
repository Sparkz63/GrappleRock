package gameObjects;

public interface IGameObject {
	public void initialize();
	public void update(long deltaTime);
	public void render();
	public void destroy();
}
