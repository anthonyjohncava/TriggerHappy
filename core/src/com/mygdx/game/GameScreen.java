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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import sprites.Enemy;
import sprites.EnemyLocation;

public class GameScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;

    // Variables for rendering the tiledMap.
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.


    float stateTime;                                        // The time the program has been running.
    int spawnCounter = 0; //time counter for spawn

    // Heart variables
    private Texture lifeImage;
    private int lives;
    private static int heart_height = 90;
    private static int heart_width = 75;
    private int locationSize = 0;

    private Texture gunTrigger;
    private Sound shootSound;
    private Sound gameOverSound;

    private Texture bloodshot;
    private Texture gameOverText;
    private Skin skin;
    private Stage stage;

    private Array<Enemy> enemies;
    private Array<EnemyLocation> enemyLocations;


    private ArrayList<Integer> available;
    Timer timer = new Timer();

    String state = "ok";
    int timeStart = 0;
    int spawnTimer = 0;


    // constructor to keep a reference to the main Game class
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        shootSound = Gdx.audio.newSound(Gdx.files.internal("gunshot.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOverVoice.wav"));



        batch = new SpriteBatch();

        this.available = new ArrayList<Integer>();

        //prepare locations
        enemyLocations = new Array<EnemyLocation>();
        //enemyLocations.add(new EnemyLocation(93,90));
        //enemyLocations.add(new EnemyLocation(225,90));
        //enemyLocations.add(new EnemyLocation(488,90));
        //enemyLocations.add(new EnemyLocation(718,90));
        //enemyLocations.add(new EnemyLocation(718,186));
        //enemyLocations.add(new EnemyLocation(488,186));
        enemyLocations.add(new EnemyLocation(225,206));
        //enemyLocations.add(new EnemyLocation(225,800));
        //enemyLocations.add(new EnemyLocation(225,400));
        //enemyLocations.add(new EnemyLocation(225,400));
        this.updateAvailableLocations();

        //prepare enemies
        enemies = new Array<Enemy>();

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

        gameOverText = new Texture(Gdx.files.internal("gameover.png"));
        bloodshot = new Texture(Gdx.files.internal("bloodstain.png"));
        gunTrigger = new Texture(Gdx.files.internal("explosion.png"));


        // Game over buttons
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();
        final TextButton exitBtn = new TextButton("EXIT", skin);
        exitBtn.setWidth(300f);
        exitBtn.setHeight(100f);
        exitBtn.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 120f);

        exitBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });

        stage.addActor(exitBtn);
        Gdx.input.setInputProcessor(stage);

    }

    public void render(float f) {
        //Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        // render tiledMap.
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors).
        stateTime += Gdx.graphics.getDeltaTime();


        batch.begin();

        if (state != "Game Over") {
            //Spawn enemies on every available location
            this.spawnEnemy(stateTime);


            //display enemy on locations
            for(EnemyLocation loc: enemyLocations) {
                Enemy e = loc.getEnemy();
                if (e != null && e.isAlive()) {
                    batch.draw(e.getEnemy(), loc.getX(), loc.getY());

                    if (e.update(Gdx.graphics.getDeltaTime()) == 1) {
                        lives -= 1;
                        state = "damaged";
                    }


                }
            }

            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), 480 - Gdx.input.getY(), 0);
                batch.draw(gunTrigger, touchPos.x - 30, touchPos.y - 30, 60, 60);
                shootSound.play();

                for(EnemyLocation l: enemyLocations) {
                    l.checkCollision(touchPos);
                }
            }

            // if player is damaged
            if (timeStart + 5 == (int)stateTime) {
                timeStart = (int)stateTime;
                state = "ok";
            }
            if (state == "damaged") {
                batch.draw(bloodshot, -100, -160, 1000, 800);
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

            if (lives < 1) {
                state = "Game Over";
                gameOverSound.setVolume(gameOverSound.play(), 200);
                gameOverSound.play();

            }
        } else {
            batch.draw(gameOverText, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }

        batch.end();

        if (state == "Game Over") {
            stage.draw();
        }

    }


    private void loadMusic() {

    }



    private void spawnEnemy(float dt){
        this.updateAvailableLocations();
        //we only spawn if locations are not full
        if(EnemyLocation.occupiedLocations < enemyLocations.size){
            //logic for spawning enemy every 3 seconds
            if (spawnTimer + 3 == (int)dt) {
                spawnTimer = (int)dt;
                Enemy spawned = null;
                EnemyLocation addToLocation;
                //create enemy
                spawned = new Enemy();
                enemies.add(spawned);
                int availableIndex = 0;

                if(this.available.size() > 0){
                    availableIndex = this.available.size() - 1;
                }

                //set enemy randomly on available locations
                if(spawned!=null){
                    int random = new Random().nextInt((availableIndex - 0) + 1) + 0;
                    addToLocation = enemyLocations.get(this.available.get(random));
                    addToLocation.setEnemy(spawned);
                }
            }
        }


    }

    private void updateAvailableLocations(){
        this.available.clear();
        for(int x=0;x<enemyLocations.size;x++) {
            if(enemyLocations.get(x).hasEnemy() == false){
                this.available.add(x);
            }
        }
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