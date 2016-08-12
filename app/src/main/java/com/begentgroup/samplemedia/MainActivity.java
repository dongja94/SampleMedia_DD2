package com.begentgroup.samplemedia;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    enum PlayState {
        IDLE,
        INITIALIED,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        ERROR,
        RELEASED
    }

    PlayState mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer = MediaPlayer.create(this, R.raw.winter_blues);
        mState = PlayState.PREPARED;

        Button btn = (Button)findViewById(R.id.btn_play);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        btn = (Button)findViewById(R.id.btn_pause);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });

    }

    private void play() {
        if (mState == PlayState.INITIALIED || mState == PlayState.STOPPED) {
            try {
                mPlayer.prepare();
                mState = PlayState.PREPARED;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mState == PlayState.PREPARED || mState == PlayState.PAUSED) {
            mPlayer.start();
            mState = PlayState.STARTED;
        }
    }

    private void pause() {
        if (mState == PlayState.STARTED) {
            mPlayer.pause();
            mState = PlayState.PAUSED;
        }
    }

    private void stop() {
        if (mState == PlayState.STARTED || mState == PlayState.PAUSED) {
            mPlayer.stop();
            mState = PlayState.STOPPED;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        mState = PlayState.RELEASED;
        mPlayer = null;
    }
}
