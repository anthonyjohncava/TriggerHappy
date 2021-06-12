package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Enemy {
    //Sprites
    private Texture texture;
    private TextureRegion textureRegion;
    private int height = 39;
    private int width = 24;

    //Enemy Status
    private boolean isAlive;
    private int accuracy; //rate to hit the player
    private Vector3 position;

    //Enemy States
    public enum EnemyState { IDLE,FIRING }
    public EnemyState currentState;


    public Enemy(){
        this.currentState = EnemyState.IDLE;
        this.isAlive = true;
        this.accuracy = 100; //100 accuracy will always hit
        this.texture = new Texture("enemy_sprite.png");
        this.textureRegion = new TextureRegion(texture,0,0,this.width,this.height);
        this.position = new Vector3(93,90,0);
    }


    public void hit() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void checkCollision(Vector3 mouseClick) {
        if (mouseClick.x >= this.position.x && mouseClick.x <= this.position.x + this.width) {
            if (mouseClick.y >= this.position.y && mouseClick.y <= this.position.y + this.height) {
                this.hit();
            }
        }
    }


    public Vector3 getPosition() {
        return position;
    }

    public void update(float dt){

    }

    public void fire(){
        this.textureRegion = new TextureRegion(texture,24,0,this.width,this.height);
    }

    public void idle(){
        this.textureRegion = new TextureRegion(texture,0,0,this.width,this.height);
    }

    public TextureRegion getEnemy() {
        return this.textureRegion;
    }

}
