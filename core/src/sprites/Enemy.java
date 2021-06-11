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

    private Animation fireAnimation;

    public Enemy(int x,int y){
        this.isAlive = true;
        this.hitpoints = 100;
        this.accuracy = 100; //100 accuracy will always hit
        this.texture = new Texture("firing_half.png");
        this.textureRegion = new TextureRegion(texture);
        this.position = new Vector3(x,y,0);

    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getEnemy() {
        return this.textureRegion;
    }

}
