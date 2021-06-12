package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Enemy {
    //Sprites
    private Texture texture;
    private TextureRegion textureRegion;
    private int height = 39;
    private int width = 24;

    //Enemy Status
    private boolean isAlive;
    private int accuracy; //rate to hit the player

    //Enemy States
    public enum EnemyState { IDLE,FIRING }
    public EnemyState currentState;

    private int timeStart = 0;
    private float stateTime;

    public Enemy(){
        this.currentState = EnemyState.IDLE;
        this.isAlive = true;
        this.accuracy = 100; //100 accuracy will always hit
        this.texture = new Texture("enemy_sprite.png");
        this.textureRegion = new TextureRegion(texture,0,0,this.width,this.height);
    }


    public void hit() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }


    public int update(float dt){

        stateTime += dt;

        if (timeStart + 5 == (int)stateTime) {
            timeStart = (int)stateTime;
            return fire();
        } else {
            this.textureRegion = new TextureRegion(texture,0,0,this.width,this.height);
        }

        Gdx.app.log("_test: (deltaTime)", String.valueOf(dt));
        Gdx.app.log("_test: (stateTime)", String.valueOf(stateTime));

        return 0;
    }

    public int fire(){
        this.textureRegion = new TextureRegion(texture,24,0,this.width,this.height);
        // accuracy at 50%
        int random = new Random().nextInt((1-0) + 1)+0;

        Gdx.app.log("_fire: ", String.valueOf(random));

        return random;
    }

    public void idle(){
        this.textureRegion = new TextureRegion(texture,0,0,this.width,this.height);
    }

    public TextureRegion getEnemy() {
        return this.textureRegion;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

}
