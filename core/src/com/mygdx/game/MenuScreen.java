package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import sprites.EnemyLocation;

/**
 * This class implements the menu screen of the game.
 */
public class MenuScreen implements Screen{

    MyGdxGame game;
    private SpriteBatch batch;  // To render the title of the game.
    private Skin skin;          // The skin/style of the button.
    private Stage stage;        // To hold the button actors.
    private Texture gameName;   // To store the image of the game's name.

    /**
     * Constructor of the MenuScreen.
     */
    public MenuScreen(MyGdxGame game) { this.game = game; }

    public void create() {
        batch = new SpriteBatch();
        gameName = new Texture(Gdx.files.internal("title.png"));
        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();

        // Exit button. When pressed, it closes the app.
        final TextButton exit_btn = new TextButton("EXIT", skin);
        exit_btn.setWidth(300f);
        exit_btn.setHeight(100f);
        exit_btn.setPosition(Gdx.graphics.getWidth()/2 + 50f, Gdx.graphics.getHeight()/2 - 170f);
        exit_btn.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                EnemyLocation.resetOccupied();
                Gdx.app.exit();
            }
        });

        // Play button. When pressed, it opens the GameScreen.
        final TextButton play_btn = new TextButton("PLAY", skin);
        play_btn.setWidth(300f);
        play_btn.setHeight(100f);
        play_btn.setPosition(Gdx.graphics.getWidth()/2 - 400f, Gdx.graphics.getHeight()/2 - 170f);
        play_btn.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                game.setScreen(MyGdxGame.gameScreen);
                play_btn.remove();
                exit_btn.remove();
            }
        });

        // Adds the play and exit buttons to the stage to be rendered.
        stage.addActor(play_btn);
        stage.addActor(exit_btn);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Renders buttons and title.
        batch.begin();
        batch.draw(gameName, Gdx.graphics.getWidth() /2 - 400f, Gdx.graphics.getHeight()/2 - 50f, 700, 200);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
