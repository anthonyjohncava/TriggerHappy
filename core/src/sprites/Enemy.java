package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Enemy {

    private boolean isAlive;
    private int hitpoints;
    private int accuracy; //rate to hit the player
    private Texture texture;
    private TextureRegion textureRegion;
    private Vector3 position;
    private int height;
    private int width;


    private EnemyAnimation fireAnimation;

    public Enemy(int x,int y){
        this.isAlive = true;
        this.hitpoints = 100;
        this.accuracy = 100; //100 accuracy will always hit
        this.texture = new Texture("enemy_sprite.png");
        fireAnimation = new EnemyAnimation(new TextureRegion(this.texture),2,3f, 1,true);
        this.position = new Vector3(x,y,0);
        this.height = 39;
        this.width = 24;

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
        fireAnimation.update(dt);
    }

    public TextureRegion getEnemy() {
        return fireAnimation.getFrame();
    }

}
