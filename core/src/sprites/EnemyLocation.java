package sprites;

import com.badlogic.gdx.math.Vector3;

public class EnemyLocation {
    private boolean isOccupied = false;
    private Vector3 position;
    private Enemy enemyContained = null;

    public EnemyLocation(float x,float y){
        this.position = new Vector3(x,y,0);
    }

    public boolean setEnemy(Enemy e){
        if(e == null){
            this.isOccupied = false;
            return false;
        }
        this.enemyContained = e;
        this.isOccupied = true;
        return true;
    }

    public Enemy getEnemy(){
        return this.enemyContained;
    }

    public boolean hasEnemy(){
        return this.isOccupied;
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

    public void checkCollision(Vector3 mouseClick) {
        //we only check if there is an enemy contained
        if(this.enemyContained != null){
            if (mouseClick.x >= this.position.x && mouseClick.x <= this.position.x + this.enemyContained.getWidth()) {
                if (mouseClick.y >= this.position.y && mouseClick.y <= this.position.y + this.enemyContained.getHeight()) {
                    //kill the enemy and empty the location
                    this.enemyContained.hit();
                    this.enemyContained = null;
                    this.isOccupied = false;
                }
            }
        }
    }

}
