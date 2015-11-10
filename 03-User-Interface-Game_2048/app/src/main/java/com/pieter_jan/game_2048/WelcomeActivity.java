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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
