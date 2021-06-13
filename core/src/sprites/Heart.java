package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Heart Class
 * Contains data about the heart of the game and its methods.
 */
public class Heart {
    private Texture texture;
    private TextureRegion textureRegion;
    private int height = 21;
    private int width = 24;
    private float stateTime;
    private int timeStart;

    public Heart(){
        this.stateTime = 0;
        this.timeStart = 0;
        this.texture = new Texture("extra_life.png");
        this.textureRegion = new TextureRegion(texture,0,0, this.width, this.height);
    }

    public TextureRegion getHeart() {
        return this.textureRegion;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }


}
