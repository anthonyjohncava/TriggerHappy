package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class GameScreen implements Screen {

    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"

    // Variables for rendering the tiledMap.
    private TiledMap tiledMap;                              // Loads the tiledMap.
    private OrthogonalTiledMapRenderer tiledMapRenderer;    // Renders the tiledMap.
    private OrthographicCamera camera;                      // Camera to show a specific portion of the world to the player.

    SpriteBatch spriteBatch;
    SpriteBatch uiBatch;

    float dt; //delta time

    // constructor to keep a reference to the main Game class
    public GameScreen(MyGdxGame game) {
        this.game = game;
    }

    public void create() {
        // Loads the tiledMap. ---------------------------------------------------------------------
        tiledMap = new TmxMapLoader().load("testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Sets the camera. ------------------------------------------------------------------------
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth()*3-700, Gdx.graphics.getHeight()*3-400);
        camera.position.set(Gdx.graphics.getWidth()+70, Gdx.graphics.getHeight()+70,0);


    }

    public void render(float f) {
        //Clear screen
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        // render tiledMap.
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
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