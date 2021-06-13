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

import sprites.Enemy;
import sprites.EnemyLocation;
import sprites.Heart;

public class GameScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;

    // Variables for rendering the tiledMap.
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.


    float stateTime;                                        // The time the program has been running.

    private Music backgroundMusic;                          // Background music while playing the game.

    // Heart variables
    private Texture lifeImage;
    private int lives;
    private static int heart_height = 90;
    private static int heart_width = 75;

    private Texture gunTrigger;
    private Sound shootSound;
    private Sound winSound;
    private Sound gameOverSound;

    private Texture bloodshot;
    private Texture gameOverText;
    private Texture congratsText;
    private Skin skin;
    private Stage stage;

    private Array<EnemyLocation> enemyLocations;


    private ArrayList<Integer> available;


    String state;
    int timeStart;
    int spawnTimer;
    private int targeted;
    private boolean heartHit;

    private int kills;
    private int killLimit;

    private TextButton button;
    private TextButton exitBtn;

    // constructor to keep a reference to the main Game class
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        loadMusic();
        shootSound = Gdx.audio.newSound(Gdx.files.internal("gunshot.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOverVoice.wav"));

        this.killLimit = 10;
        this.kills = 0;
        this.heartHit = false;
        this.targeted = -1;
        state = "ok";
        timeStart = 0;
        spawnTimer = 0;

        batch = new SpriteBatch();

        this.available = new ArrayList<Integer>();

        //prepare locations
        enemyLocations = new Array<EnemyLocation>();
        enemyLocations.add(new EnemyLocation(93,90));
        enemyLocations.add(new EnemyLocation(225,90));
        enemyLocations.add(new EnemyLocation(488,90));
        enemyLocations.add(new EnemyLocation(718,90));
        enemyLocations.add(new EnemyLocation(718,186));
        enemyLocations.add(new EnemyLocation(488,186));
        enemyLocations.add(new EnemyLocation(225,218));
        enemyLocations.add(new EnemyLocation(93,218));
        enemyLocations.add(new EnemyLocation(225,315));
        enemyLocations.add(new EnemyLocation(93,315));
        this.updateAvailableLocations();


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
        congratsText = new Texture(Gdx.files.internal("congrats.png"));
        bloodshot = new Texture(Gdx.files.internal("bloodstain.png"));
        gunTrigger = new Texture(Gdx.files.internal("explosion.png"));


        // Game over buttons
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();

        button = new TextButton("RETRY", skin);
        button.setWidth(250f);
        button.setHeight(100f);
        button.setPosition(Gdx.graphics.getWidth()/2 - 250f, Gdx.graphics.getHeight()/2 - 170f);


        exitBtn = new TextButton("EXIT", skin);
        exitBtn.setWidth(250f);
        exitBtn.setHeight(100f);
        exitBtn.setPosition(Gdx.graphics.getWidth()/2 + 10f, Gdx.graphics.getHeight()/2 - 170f);

        button.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                EnemyLocation.resetOccupied();
                backgroundMusic.stop();
                stateTime = 0;
                game.setScreen(MyGdxGame.gameScreen);
            }
        });

        exitBtn.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });


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

        if (state == "ok" || state == "damaged") {

            //Spawn enemies on every available location
            this.spawnEnemy(stateTime);


            //display enemy on locations
            for(EnemyLocation loc: enemyLocations) {
                if(loc.getEnemy() != null){
                    Enemy e = loc.getEnemy();
                    if (e != null && e.isAlive()) {
                        batch.draw(e.getEnemy(), loc.getX(), loc.getY());

                        if (e.update(Gdx.graphics.getDeltaTime()) == 1) {
                            lives -= 1;
                            state = "damaged";
                        }
                    }
                }

                if(loc.getHeart() != null){
                    Heart h = loc.getHeart();
                    if (h != null) {
                        batch.draw(h.getHeart(), loc.getX(), loc.getY());
                    }
                }

            }

            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), 480 - Gdx.input.getY(), 0);
                batch.draw(gunTrigger, touchPos.x - 30, touchPos.y - 30, 60, 60);
                shootSound.play();

                for(EnemyLocation l: enemyLocations) {
                    this.targeted = l.checkCollision(touchPos);
                    if(this.targeted == 1){
                        if(lives < 3){
                            lives += 1;
                        }
                    }else if(targeted == 0){
                        kills++;
                    }
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



            //draw lives
            int heartX = 640;
            for(int z=1;z<=lives;z++){
                batch.draw(lifeImage, heartX, 400, heart_width, heart_height);
                heartX+=50;
            }


            if (lives < 1) {
                state = "Game Over";
                gameOverSound.setVolume(gameOverSound.play(), 200);
                gameOverSound.play();
            }


            //Game condition
            if(kills >= killLimit){
                //once we reach the kill limit, we win the game
                state = "Congratulations";
                winSound.play();
            }


        }else if(state == "Congratulations"){
            batch.draw(congratsText, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }else if(state == "Game Over") {
            batch.draw(gameOverText, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }

        batch.end();

        if (state == "Game Over") {
            stage.addActor(button);
            stage.addActor(exitBtn);
            stage.draw();

        }else if(state == "Congratulations"){
            stage.addActor(button);
            stage.addActor(exitBtn);
            stage.draw();
        }

    }


    private void loadMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }



    private void spawnEnemy(float dt){

        this.updateAvailableLocations();
        //we only spawn if locations are not full
        if(EnemyLocation.occupiedLocations < enemyLocations.size){
            //logic for spawning enemy every x seconds
            if (spawnTimer + 1 == (int)dt) {
                spawnTimer = (int)dt;
                Enemy spawned = null;
                Heart spawnHeart = null;
                EnemyLocation addToLocation;

                //1 out of 5 chance to spawn a heart
                int heartRandom = new Random().nextInt((5 - 1) + 1) + 1;
                int availableIndex = 0;
                int random = -1;

                if(heartRandom == 1){
                    //we create a heart
                    //create enemy
                    spawnHeart = new Heart();

                    if(this.available.size() > 0){
                        availableIndex = this.available.size() - 1;
                    }

                    //set enemy randomly on available locations
                    if(spawnHeart!=null){
                        random = new Random().nextInt((availableIndex - 0) + 1) + 0;
                        addToLocation = enemyLocations.get(this.available.get(random));
                        addToLocation.setHeart(spawnHeart);
                    }
                }else{
                    //create enemy
                    spawned = new Enemy();

                    if(this.available.size() > 0){
                        availableIndex = this.available.size() - 1;
                    }

                    //set enemy randomly on available locations
                    if(spawned!=null){
                        random = new Random().nextInt((availableIndex - 0) + 1) + 0;
                        addToLocation = enemyLocations.get(this.available.get(random));
                        addToLocation.setEnemy(spawned);
                    }
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