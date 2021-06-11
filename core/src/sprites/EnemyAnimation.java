package sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class EnemyAnimation {

    private Array<TextureRegion> frames;
    private float maxFrameTime; //time it takes to stay in view before switching
    private float currentFrameTime; //time the animation has been in current frame
    private int frameCount; //number of frames in animation
    private int frame; //current frame
    private boolean looping;

    public EnemyAnimation(TextureRegion region, int frameCount, float cycleTime,int frameRow,boolean loop){
        frames = new Array<TextureRegion>();
        looping = loop;
        int frameWidth = region.getRegionWidth()/frameCount;
        for(int i = 0; i < frameCount; i++){
            frames.add(new TextureRegion(region,i * frameWidth, 0, frameWidth, region.getRegionHeight()/frameRow));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime/frameCount;
        frame = 0;
    }


    public void update(float dt){
        currentFrameTime += dt;
        if(currentFrameTime > maxFrameTime){
            frame++;
            currentFrameTime = 0;
        }

        if(looping){
            if(frame >= frameCount){
                frame = 0;
            }
        }else{
            frame = frameCount-1;
        }
    }

    public TextureRegion getFrame(){
        return frames.get(frame);
    }


}
