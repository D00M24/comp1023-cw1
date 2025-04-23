package com.uon.saofteng;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
    private SpriteBatch batch;

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
//        setScreen(new BossAlertScreen(this, new LeaderboardScreen(this)));
          setScreen(new LeaderboardScreen(this));

    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
