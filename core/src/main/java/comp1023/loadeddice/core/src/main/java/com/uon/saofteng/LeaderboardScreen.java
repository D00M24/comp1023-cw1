package com.uon.saofteng;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;

public class LeaderboardScreen implements Screen {

    private MainGame game;
    private Stage stage;
    private Texture background;
    private BitmapFont font;
    private BitmapFont boldFont;
    private ArrayList<ScoreEntry> scores;
    private Texture backButtonTexture;

    public LeaderboardScreen(MainGame game) {
        this.game = game;

        // Create stage with the same viewport dimensions as main menu
        stage = new Stage(new FitViewport(1850, 1250));
        Gdx.input.setInputProcessor(stage);

        // Load textures
        background = new Texture("ui/Leaderboard_background.png");
        backButtonTexture = new Texture("ui/back_button.png");

        // Set up background
        Image backgroundImage = new Image(background);
        backgroundImage.setFillParent(true);
        backgroundImage.setScaling(Scaling.stretch);
        stage.addActor(backgroundImage);

        // Create fonts
        font = new BitmapFont();
        boldFont = new BitmapFont();

        // Scale fonts based on viewport size
        float fontScale = stage.getViewport().getWorldHeight() / 400f;
        font.getData().setScale(fontScale);
        boldFont.getData().setScale(fontScale);
        boldFont.setColor(Color.GOLD);

        // Create back button
        ImageButton backButton = createBackButton();
        backButton.setPosition(10, 2); // Position in bottom left
        stage.addActor(backButton);

        loadScores();
        createScoreTable();
    }

    private ImageButton createBackButton() {
        TextureRegion region = new TextureRegion(backButtonTexture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);
        ImageButton button = new ImageButton(drawable);

        // Scale the button
        float scale = 0.4f;
        float width = backButtonTexture.getWidth() * scale;
        float height = backButtonTexture.getHeight() * scale;
        button.setSize(width, height);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        return button;
    }

    private void createScoreTable() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        float padding = stage.getViewport().getWorldHeight() / 25f; // Dynamic padding
        table.padTop(padding * 3); // More padding at top for title

        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);

            Table rowTable = new Table();

            // Add rank
            com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle regularStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle(font, Color.WHITE);
            com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle boldStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle(boldFont, Color.GOLD);

            rowTable.add(new com.badlogic.gdx.scenes.scene2d.ui.Label((i + 1) + ". ", regularStyle)).padRight(padding);
            rowTable.add(new com.badlogic.gdx.scenes.scene2d.ui.Label(entry.score + " pts", boldStyle)).padRight(padding);
            rowTable.add(new com.badlogic.gdx.scenes.scene2d.ui.Label("- " + entry.date, regularStyle));

            table.add(rowTable).pad(padding/2).row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        backButtonTexture.dispose();
        font.dispose();
        boldFont.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    private void loadScores() {
        FileHandle file = Gdx.files.internal("scores.txt");
        String[] lines = file.readString().split("\n");

        scores = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.trim().split(",");
            if (parts.length == 2) {
                try {
                    int score = Integer.parseInt(parts[0]);
                    String date = parts[1];
                    scores.add(new ScoreEntry(score, date));
                } catch (NumberFormatException ignored) {}
            }
        }
    }

    // Helper class for score + date
    static class ScoreEntry {
        int score;
        String date;

        ScoreEntry(int score, String date) {
            this.score = score;
            this.date = date;
        }
    }
}
