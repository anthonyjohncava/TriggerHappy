package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
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

/**
 * This class implements the GameScreen, which renders the entire game.
 */
public class GameScreen implements Screen {
    MyGdxGame game;
    SpriteBatch batch;                                      // Spritebatch for rendering
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.
    private float stateTime;                                // The time the program has been running.
    private Texture lifeImage;                              // Image of player's life.
    private Texture gunTrigger;                             // Image when player fires.
    private Texture bloodshot;                              // Image when player gets shot.
    private Texture gameOverText;                           // Image when player loses.
    private Texture congratsText;                           // Image when player wins.
    private Sound shootSound;                               // Sound when player wins.
    private Sound winSound;                                 // Sound when player wins.
    private Sound gameOverSound;                            // Sound when player dies.
    private Music backgroundMusic;                          // Background music while playing the game.
    private Skin skin;                                      // The skin/style of the buttons.
    private Stage stage;                                    // This will hold the button Actors.
    private Array<EnemyLocation> enemyLocations;            // Window locations in the game.
    private ArrayList<Integer> available;                   // Number of available locations for enemies to spawn.
    private String state;                                   // State of the player.
    private int timeStart;                                  // To track how long the bloodshot shows.
    private int spawnTimer;                                 // Timer to spawn enemies/hearts.
    private int lives;                                      // Player's number of lives.
    private static final int heart_height = 90;             // Size of the heart in lives.
    private static final int heart_width = 75;              // Size of the heart in lives.
    private int heartSpawnCount;                            // Counter to check how many hearts have spawned.
    private int targeted;                                   // To check if the target is a heart or enemy.
    private int kills;                                      // To count the number of enemies killed.
    private int killLimit;                                  // The total number of enemies to kill to win the game.
    private TextButton retry_btn;                           // Retry button.
    private TextButton exit_btn;                            // Exit button.

    /**
     * Constructor of the GameScreen.
     */
    public GameScreen(MyGdxGame game) { this.game = game;}

    /**
     * This method is called initially when the GameScreen starts to initialise the variables.
     */
    public void create() {

        // Initialize game variables.
        this.heartSpawnCount = 0;
        this.killLimit = 30;
        this.kills = 0;
        this.targeted = -1;
        this.state = "ok";
        this.timeStart = 0;
        this.spawnTimer = 0;
        this.batch = new SpriteBatch();
        this.available = new ArrayList<Integer>();
        this.lives = 3;                                     //we have 3 lives at the start, 3 is the max number of lives

        // Loads the background music and sound effects.
        loadMusic();

        // Prepare locations.
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

        // Loads the tiledMap.
        tiledMap = new TmxMapLoader().load("testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Sets the camera.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()*3-700, Gdx.graphics.getHeight()*3-400);
        camera.position.set(Gdx.graphics.getWidth()+70, Gdx.graphics.getHeight()+70,0);

        // Prepare textures.
        lifeImage = new Texture(Gdx.files.internal("heart.png"));
        gameOverText = new Texture(Gdx.files.internal("gameover.png"));
        congratsText = new Texture(Gdx.files.internal("congrats.png"));
        bloodshot = new Texture(Gdx.files.internal("bloodstain.png"));
        gunTrigger = new Texture(Gdx.files.internal("explosion.png"));

        // Game over buttons.
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();

        retry_btn = new TextButton("RETRY", skin);
        retry_btn.setWidth(250f);
        retry_btn.setHeight(100f);
        retry_btn.setPosition(Gdx.graphics.getWidth()/2 - 250f, Gdx.graphics.getHeight()/2 - 170f);

        exit_btn = new TextButton("EXIT", skin);
        exit_btn.setWidth(250f);
        exit_btn.setHeight(100f);
        exit_btn.setPosition(Gdx.graphics.getWidth()/2 + 10f, Gdx.graphics.getHeight()/2 - 170f);

        retry_btn.addListener(new ClickListener()
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

        exit_btn.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                EnemyLocation.resetOccupied();
                Gdx.app.exit();
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * The render method loops continuously, and is called after the create() method.
     */
    public void render(float f) {

        // Clear screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Render tiledMap.
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // Updates the stateTime using the deltaTime (to have the same time across all devices with different processors).
        stateTime += Gdx.graphics.getDeltaTime();

        batch.begin();

        // Checking the game states.
        if (state == "ok" || state == "damaged") {

            //Spawn enemies on every available location
            this.spawnEnemy(stateTime);

            // Display enemy on locations.
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

            // Collision detection when enemy is hit.
            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), 480 - Gdx.input.getY(), 0);
                batch.draw(gunTrigger, touchPos.x - 30, touchPos.y - 30, 60, 60);
                shootSound.play();

                for(EnemyLocation l: enemyLocations) {
                    this.targeted = l.checkCollision(touchPos);
                    if(this.targeted == 1){

                        // We lessen the spawned heart count.
                        this.heartSpawnCount--;
                        if(lives < 3){
                            lives += 1;
                        }
                    } else if(targeted == 0){
                        kills++;
                    }
                }
            }

            // If player is damaged.
            if (timeStart + 5 == (int)stateTime) {
                timeStart = (int)stateTime;
                state = "ok";
            }
            if (state == "damaged") {
                batch.draw(bloodshot, -100, -160, 1000, 800);
            }

            // Draw heart depending on number of lives.
            int heartX = 640;
            for(int z=1;z<=lives;z++){
                batch.draw(lifeImage, heartX, 400, heart_width, heart_height);
                heartX+=50;
            }

            // Game over if lives are less than 1.
            if (lives < 1) {
                state = "Game Over";
                gameOverSound.setVolume(gameOverSound.play(), 200);
                gameOverSound.play();
            }

            // Game condition to win when kill limit reached.
            if(kills >= killLimit){

                // Once we reach the kill limit, we win the game.
                state = "Congratulations";
                winSound.play();
            }
        } else if(state == "Congratulations"){
            batch.draw(congratsText, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        } else if(state == "Game Over") {
            batch.draw(gameOverText, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }
        batch.end();

        // Renders buttons of the screen when the game ends.
        if (state == "Game Over" || state == "Congratulations") {
            stage.addActor(retry_btn);
            stage.addActor(exit_btn);
            stage.draw();
        }
    }

    /**
     *  This method loads the background music and sound effects.
     */
    private void loadMusic() {
        shootSound = Gdx.audio.newSound(Gdx.files.internal("gunshot.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOverVoice.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    /**
     * Spawning Enemy or Heart on a location
     * @param dt
     */
    private void spawnEnemy(float dt){

        // Update locations where enemy or heart can spawn.
        this.updateAvailableLocations();

        // We only spawn if locations are not full.
        if(EnemyLocation.occupiedLocations < enemyLocations.size){

            // Logic for spawning enemy every x seconds.
            if (spawnTimer + 1 == (int)dt) {
                spawnTimer = (int)dt;
                Enemy spawned = null;
                Heart spawnHeart = null;
                EnemyLocation addToLocation;

                // 1 out of 5 chance to spawn a heart.
                int heartRandom = new Random().nextInt((5 - 1) + 1) + 1;
                int availableIndex = 0;
                int random = -1;

                // We force to create enemy if 3 hearts are currently spawned.
                if(this.heartSpawnCount >= 3){
                    heartRandom = 2;
                }

                if(heartRandom == 1){
                    spawnHeart = new Heart();

                    if(this.available.size() > 0){
                        availableIndex = this.available.size() - 1;
                    }

                    // Set enemy randomly on available locations.
                    if(spawnHeart!=null){
                        random = new Random().nextInt((availableIndex - 0) + 1) + 0;
                        addToLocation = enemyLocations.get(this.available.get(random));
                        addToLocation.setHeart(spawnHeart);
                        this.heartSpawnCount++;
                    }
                } else{

                    // Create enemy.
                    spawned = new Enemy();

                    if(this.available.size() > 0){
                        availableIndex = this.available.size() - 1;
                    }

                    // Set enemy randomly on available locations.
                    if(spawned!=null){
                        random = new Random().nextInt((availableIndex - 0) + 1) + 0;
                        addToLocation = enemyLocations.get(this.available.get(random));
                        addToLocation.setEnemy(spawned);
                    }
                }
            }
        }
    }

    /**
     * Updates the available locations where enemy or heart can spawn.
     */
    private void updateAvailableLocations(){
        this.available.clear();

        for(int x=0;x<enemyLocations.size;x++) {
            if(enemyLocations.get(x).hasEnemy() == false){
                this.available.add(x);
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    /**
     * This method calls the create() method after being called from a different page/menu.
     */
    @Override
    public void show() {
        create();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        batch.dispose();
        gunTrigger.dispose();
        congratsText.dispose();
        gameOverSound.dispose();
        gameOverSound.dispose();
        lifeImage.dispose();
        stage.dispose();
        shootSound.dispose();
    }
}