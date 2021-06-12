package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen{
    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"

    private Skin skin;
    private Stage stage;

    // constructor to keep a reference to the main Game class
    public MenuScreen(MyGdxGame game) {
        this.game = game;
    }



    public void create() {
        Gdx.app.log("MenuScreen: ","menuScreen create");

        skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        stage = new Stage();
        final TextButton button = new TextButton("PLAY", skin);
        button.setWidth(300f);
        button.setHeight(100f);
        button.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 0f);


        final TextButton exitBtn = new TextButton("EXIT", skin);
        exitBtn.setWidth(300f);
        exitBtn.setHeight(100f);
        exitBtn.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 120f);

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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
