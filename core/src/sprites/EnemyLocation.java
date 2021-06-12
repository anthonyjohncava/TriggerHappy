package sprites;

import com.badlogic.gdx.math.Vector3;

public class EnemyLocation {
    private boolean isOccupied = false;
    private Vector3 position;
    private Enemy enemyContained;

    public EnemyLocation(Enemy e){
        this.enemyContained = new Enemy();
    }

    public float getX(){
        return this.position.x;
    }

    public float getY(){
        return this.position.y;
    }

    public Vector3 getLocation(){
        return this.position;
    }

}
