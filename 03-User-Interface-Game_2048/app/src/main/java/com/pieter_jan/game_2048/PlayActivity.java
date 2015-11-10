package com.pieter_jan.game_2048;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity
{
    private Board board;
    private SharedPreferences mPrefs;
    private boolean newGame;
    private boolean alreadyWon = false;

    public static String SCORE = "SCORE";
    public static String BOARD = "BOARD";
    public static String NEWGAME = "NEWGAME";
    public static String ALREADYWON = "ALREADYWON";

    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.score)
    TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        newGame = intent.getBooleanExtra(NEWGAME, false);
        alreadyWon = intent.getBooleanExtra(ALREADYWON, false);
        intent.removeExtra(NEWGAME);
        setupActionBar();
        initBoard(newGame);
        container.addView(board);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putString(BOARD, board.getJSON());
        ed.putInt(SCORE, board.getScore());
        ed.putBoolean(ALREADYWON, alreadyWon);
        ed.apply();
    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void initBoard(boolean startNewGame)
    {
        mPrefs = getSharedPreferences("board", MODE_PRIVATE);
        String json = startNewGame ? null : mPrefs.getString(BOARD, null);
        int score = startNewGame ? 0 : mPrefs.getInt(SCORE, 0);
        board = new Board(this, json, score);
        updateScore();
        SwipeListener listener = new SwipeListener(this)
        {
            @Override
            public void onSwipeRight()
            {
                swipe(Board.Direction.RIGHT);
            }

            @Override
            public void onSwipeLeft()
            {
                swipe(Board.Direction.LEFT);
            }

            @Override
            public void onSwipeUp()
            {
                swipe(Board.Direction.UP);
            }

            @Override
            public void onSwipeDown()
            {
                swipe(Board.Direction.DOWN);
            }
        };
        board.setOnTouchListener(listener);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        board.setLayoutParams(params);
    }

    private void swipe(Board.Direction direction)
    {
        if (!board.execute(direction).canMove())
        {
            loseGame();
        }
        else if (!alreadyWon && board.wonGame())
        {
            winGame();
        }
        updateScore();
    }

    private void updateScore()
    {
        scoreTextView.setText(getResources().getString(R.string.score) + " " + board.getScore());
    }

    private void loseGame()
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.game_over))
                .setMessage(getResources().getString(R.string.text_game_over))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        board.resetBoard();
                        updateScore();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                })
                .setIcon(R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }

    private void winGame()
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.game_won))
                .setMessage(getResources().getString(R.string.text_game_won))
                .setPositiveButton(R.string.action_continue, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .setNegativeButton(R.string.action_stop, new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                })
                .setIcon(R.drawable.btn_star_big_off)
                .setCancelable(false)
                .show();
    }

    public void reset(MenuItem item)
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.reset))
                .setMessage(getResources().getString(R.string.text_reset))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        board.resetBoard();
                        updateScore();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .setIcon(R.drawable.ic_menu_revert)
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_8)
            swipe(Board.Direction.UP);
        else if (keyCode == KeyEvent.KEYCODE_2)
            swipe(Board.Direction.DOWN);
        else if (keyCode == KeyEvent.KEYCODE_4)
            swipe(Board.Direction.LEFT);
        else if (keyCode == KeyEvent.KEYCODE_6)
            swipe(Board.Direction.RIGHT);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
