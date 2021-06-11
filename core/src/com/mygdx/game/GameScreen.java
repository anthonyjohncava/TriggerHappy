package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

import sprites.Enemy;

public class GameScreen implements Screen {

    private Music backgroundMusic;                          // Background music while playing the game.

    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    SpriteBatch spriteBatch;
    SpriteBatch uiBatch;

    private Enemy enemy1;

    // Variables for rendering the tiledMap.
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.

    // Main character variables.
    Texture runningSheet;                                   // Texture to hold the spritesheet.
    TextureRegion[] runFrames;                              // Texture array for the running frames.
    private static final int FRAME_COLS = 2;                // Number of columns of the running spritesheet.
    private static final int FRAME_ROWS = 1;                // Number of rows of the running spritesheet.
    private static int character_height = 39;              // Height of the character.
    private final int character_width = 34;                // Width of the character.
    private static int heart_height = 90;
    private static int heart_width = 75;
    private static int characterX = 220;                          // Character's X position.
    private static int characterY = 89;                          // Character's Y position.
    // Variables for the character running animation.
    Animation runAnimation;		                            // Stores the array containing all of runFrames. It will also have the defined duration (in seconds) for each frame.
    TextureRegion currentFrame;                             // Current frame to display.
    float stateTime;                                        // The time the program has been running.

    // Heart variables
    private Texture lifeImage;

    // Firing variables
    private Texture gunTrigger;

    float dt; //delta time

    private int lives;

    // Test
    private static int height;
    private static int width;

    // Get position of click
    private int click_location_X;
    private int click_location_Y;

    // constructor to keep a reference to the main Game class
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {

        //loadMusic();

        spriteBatch = new SpriteBatch();

        // Loads the tiledMap. ---------------------------------------------------------------------
        tiledMap = new TmxMapLoader().load("testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Sets the camera. ------------------------------------------------------------------------
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()*3-700, Gdx.graphics.getHeight()*3-400);
        camera.position.set(Gdx.graphics.getWidth()+70, Gdx.graphics.getHeight()+70,0);

        //Enemy
        enemy1 = new Enemy(0,410);

        // Heart
        lifeImage = new Texture(Gdx.files.internal("heart.png"));

        // Trigger
        gunTrigger = new Texture(Gdx.files.internal("explosion.png"));


        lives = 3;

        // Test
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
    }

    public void render(float f) {
        //Clear screen
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        // render tiledMap.
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors).
        stateTime += Gdx.graphics.getDeltaTime();

        
        //click_location_X =  Gdx.input.getX();
        //click_location_Y =  Gdx.input.getY();


        spriteBatch.begin();

        spriteBatch.draw(enemy1.getEnemy(),enemy1.getPosition().x,enemy1.getPosition().y);



        if (lives == 3) {
            spriteBatch.draw(lifeImage, 640, 400, heart_width, heart_height);
            spriteBatch.draw(lifeImage, 690, 400, heart_width, heart_height);
            spriteBatch.draw(lifeImage, 740, 400, heart_width, heart_height);
        }

        if (lives == 2) {
            spriteBatch.draw(lifeImage, 640, 400, heart_width, heart_height);
            spriteBatch.draw(lifeImage, 690, 400, heart_width, heart_height);
        }

        if (lives == 1) {
            spriteBatch.draw(lifeImage, 640, 400, heart_width, heart_height);
        }


        spriteBatch.end();
    }




    private void loadMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        // Starts the background music.
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
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
        Gdx.app.log("GameScreen: ","gameScreen show called");
        create();
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen: ","gameScreen hide called");
    }
}