package sprites;

import com.badlogic.gdx.math.Vector3;

public class EnemyLocation {
    private boolean isOccupied = false;
    private Vector3 position;
    private Enemy enemyContained = null;
    private Heart heartContained = null;
    public static int occupiedLocations = 0;

    public EnemyLocation(float x,float y){
        this.position = new Vector3(x,y,0);
    }

    public static void resetOccupied(){
        occupiedLocations = 0;
    }

    public boolean setEnemy(Enemy e){
        if(e == null){
            this.isOccupied = false;
            return false;
        }
        this.enemyContained = e;
        this.isOccupied = true;
        occupiedLocations+=1;
        return true;
    }

    public boolean setHeart(Heart h){
        if(h == null){
            this.isOccupied = false;
            return false;
        }
        this.heartContained = h;
        this.isOccupied = true;
        occupiedLocations+=1;
        return true;
    }

    public Enemy getEnemy(){
        return this.enemyContained;
    }

    public Heart getHeart(){
        return this.heartContained;
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

    public int checkCollision(Vector3 mouseClick) {
        int targeted = -1; //0 if enemy 1 if heart
        //we only check if there is an enemy contained
        if(this.enemyContained != null){
            if (mouseClick.x >= this.position.x && mouseClick.x <= this.position.x + this.enemyContained.getWidth()) {
                if (mouseClick.y >= this.position.y && mouseClick.y <= this.position.y + this.enemyContained.getHeight()) {
                    //kill the enemy and empty the location
                    this.enemyContained.hit();
                    this.enemyContained = null;
                    this.occupiedLocations-=1;
                    this.isOccupied = false;
                    targeted = 0;
                }
            }
        }

        //if it contains heart
        if(this.heartContained != null){
            if (mouseClick.x >= this.position.x && mouseClick.x <= this.position.x + this.heartContained.getWidth()) {
                if (mouseClick.y >= this.position.y && mouseClick.y <= this.position.y + this.heartContained.getHeight()) {
                    //kill the enemy and empty the location
                    this.heartContained = null;
                    this.occupiedLocations-=1;
                    this.isOccupied = false;
                    targeted = 1;
                }
            }

        }

        return targeted;
    }

}
