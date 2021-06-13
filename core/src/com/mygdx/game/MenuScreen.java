package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
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

public class MenuScreen implements Screen{
    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"

    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    private Texture gameName;

    // constructor to keep a reference to the main Game class
    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }



    public void create() {
        batch = new SpriteBatch();
        Gdx.app.log("MenuScreen: ","menuScreen create");

        gameName = new Texture(Gdx.files.internal("title.png"));

        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();
        final TextButton button = new TextButton("PLAY", skin);
        button.setWidth(300f);
        button.setHeight(100f);
        button.setPosition(Gdx.graphics.getWidth()/2 - 400f, Gdx.graphics.getHeight()/2 - 170f);
//        button.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 0f);


        final TextButton exitBtn = new TextButton("EXIT", skin);
        exitBtn.setWidth(300f);
        exitBtn.setHeight(100f);
//        exitBtn.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 120f);
        exitBtn.setPosition(Gdx.graphics.getWidth()/2 + 50f, Gdx.graphics.getHeight()/2 - 170f);

        button.addListener(new ClickListener()
        {
            @Override
            public void clicked (InputEvent event, float x, float y)
            {
                game.setScreen(MyGdxGame.gameScreen);
                button.remove();
                exitBtn.remove();
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

        stage.addActor(button);
        stage.addActor(exitBtn);
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


        batch.begin();
        batch.draw(gameName, Gdx.graphics.getWidth() /2 - 400f, Gdx.graphics.getHeight()/2 - 50f, 700, 200);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
