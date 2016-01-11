package com.pieter_jan.game_2048;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @OnClick(R.id.button_continue)
    public void continueGame(View v)
    {
        Intent intent = new Intent(WelcomeActivity.this, PlayActivity.class);
        intent.putExtra(PlayActivity.NEWGAME, false);
        startActivity(intent);
    }

    @OnClick(R.id.button_newgame)
    public void newGame(View v)
    {
        Intent intent = new Intent(WelcomeActivity.this, PlayActivity.class);
        intent.putExtra(PlayActivity.NEWGAME, true);
        startActivity(intent);
    }

    @OnClick(R.id.button_about)
    public void showAbout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_about).setMessage(R.string.text_about).show();
    }

    @OnClick(R.id.button_exit)
    public void exit(View v) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }
}
