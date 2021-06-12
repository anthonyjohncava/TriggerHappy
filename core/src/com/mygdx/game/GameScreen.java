package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import sprites.Enemy;

public class GameScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;

    // Variables for rendering the tiledMap.
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.


    // Variables for the character running animation.
    Animation runAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame.
    TextureRegion currentFrame;                             // Current frame to display.
    float stateTime;                                        // The time the program has been running.

    // Heart variables
    private Texture lifeImage;
    private int lives;
    private static int heart_height = 90;
    private static int heart_width = 75;


    private Texture gunTrigger;
    private Sound shootSound;

    private Array<Enemy> enemies;



    // constructor to keep a reference to the main Game class
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        shootSound = Gdx.audio.newSound(Gdx.files.internal("gunshot.wav"));
        batch = new SpriteBatch();

        enemies = new Array<Enemy>();
        this.spawnEnemy();

        // Loads the tiledMap. ---------------------------------------------------------------------
        tiledMap = new TmxMapLoader().load("testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Sets the camera. ------------------------------------------------------------------------
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()*3-700, Gdx.graphics.getHeight()*3-400);
        camera.position.set(Gdx.graphics.getWidth()+70, Gdx.graphics.getHeight()+70,0);

        // Heart
        lifeImage = new Texture(Gdx.files.internal("heart.png"));
        lives = 3;

        gunTrigger = new Texture(Gdx.files.internal("explosion.png"));

    }

    public void render(float f) {
        //Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        // render tiledMap.
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors).
        stateTime += Gdx.graphics.getDeltaTime();

        batch.begin();
        //Draw enemies

        for(Enemy enemy: enemies){
            if(enemy.isAlive()){
                batch.draw(enemy.getEnemy(),enemy.getPosition().x,enemy.getPosition().y);
                //trigger firing after 10 statetime
                if(stateTime > 10){
                    enemy.fire();
                }
            }
        }


        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), 480 - Gdx.input.getY(), 0);
            batch.draw(gunTrigger, touchPos.x - 30, touchPos.y - 30, 60, 60);
            shootSound.play();

            for(Enemy enemy: enemies){
                enemy.checkCollision(touchPos);
            }

        }


        if (lives == 3) {
            batch.draw(lifeImage, 640, 400, heart_width, heart_height);
            batch.draw(lifeImage, 690, 400, heart_width, heart_height);
            batch.draw(lifeImage, 740, 400, heart_width, heart_height);
        }

        if (lives == 2) {
            batch.draw(lifeImage, 640, 400, heart_width, heart_height);
            batch.draw(lifeImage, 690, 400, heart_width, heart_height);
        }

        if (lives == 1) {
            batch.draw(lifeImage, 640, 400, heart_width, heart_height);
        }


        batch.end();

    }


    private void loadMusic() {

    }

    private void spawnEnemy(){
        //225,90
        //enemy1 = new Enemy(93, 90 );
        enemies.add(new Enemy());
    }

    @Override
    public void dispose() { }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() {
        create();
    }

    @Override
    public void hide() {
        //Gdx.app.log("GameScreen: ","gameScreen hide called");
    }
}